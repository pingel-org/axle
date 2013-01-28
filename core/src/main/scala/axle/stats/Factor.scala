package axle.stats

// import axle._
import axle.IndexedCrossProduct
import axle.matrix.JblasMatrixFactory._
import collection._

/* Technically a "Distribution" is probably a table that sums to 1, which is not
 * always true in a Factor.  They should be siblings rather than parent/child.
 */

object Factor {

  def apply(varList: Seq[RandomVariable[_]], values: Map[Seq[CaseIs[_]], Double]): Factor =
    new Factor(varList, values)

  def spaceFor(varSeq: Seq[RandomVariable[_]]): Iterator[List[CaseIs[_]]] = {
    val x = varSeq.map(_.values.getOrElse(Nil).toIndexedSeq)
    val kaseIt = IndexedCrossProduct(x).iterator
    kaseIt.map(kase => kase.zipWithIndex.map({
      case (v, i: Int) => {
        val rv = varSeq(i).asInstanceOf[RandomVariable[Any]] // TODO: remove cast
        CaseIs(rv, v)
      }
    }).toList)
  }

}

class Factor(varList: Seq[RandomVariable[_]], values: Map[Seq[CaseIs[_]], Double]) {

  lazy val cp = new IndexedCrossProduct(varList.map(
    _.values.getOrElse(Nil.toIndexedSeq)
  ))

  lazy val elements = (0 until cp.size).map(i => values.get(caseOf(i)).getOrElse(0.0)).toArray

  def variables() = varList

  // assume prior and condition are disjoint, and that they are
  // each compatible with this table
  def evaluate(prior: Seq[CaseIs[_]], condition: Seq[CaseIs[_]]): Double = {
    import axle.algebra._
    implicit val foo = Monoid.tuple2Monoid[Double, Double]()
    val pw = cases().map(c => {
      if (isSupersetOf(c, prior)) {
        if (isSupersetOf(c, condition)) {
          (this(c), this(c))
        } else {
          (this(c), 0.0)
        }
      } else {
        (0.0, 0.0)
      }
    }).reduce(_ |+| _)

    pw._1 / pw._2
  }

  def indexOf(cs: Seq[CaseIs[_]]): Int = {
    val rvvs: Seq[(RandomVariable[_], Any)] = cs.map(ci => (ci.rv, ci.v))
    val rvvm = rvvs.toMap
    cp.indexOf(varList.map(rvvm(_)))
  }

  private def caseOf(i: Int): Seq[CaseIs[_]] =
    varList.zip(cp(i)).map({ case (variable: RandomVariable[_], value) => CaseIs(variable, value) })

  def cases(): Iterator[Seq[CaseIs[_]]] = (0 until elements.length).iterator.map(caseOf(_))

  // def update(c: Seq[CaseIs[_]], d: Double): Unit = elements(indexOf(c)) = d

  def apply(c: Seq[CaseIs[_]]): Double = elements(indexOf(c))

  override def toString(): String =
    varList.map(rv => rv.name.padTo(rv.charWidth, " ").mkString("")).mkString(" ") + "\n" +
      cases.map(kase =>
        kase.map(ci => ci.v.toString.padTo(ci.rv.charWidth, " ").mkString("")).mkString(" ") +
          " " + "%f".format(this(kase))
      ).mkString("\n")

  def toHtml(): xml.Node =
    <table border={ "1" }>
      <tr>{ varList.map(rv => <td>{ rv.name }</td>): xml.NodeSeq }<td>P</td></tr>
      {
        cases.map(kase => <tr>
                            { kase.map(ci => <td>{ ci.v.toString }</td>) }
                            <td>{ "%f".format(this(kase)) }</td>
                          </tr>)
      }
    </table>

  // Chapter 6 definition 6
  def maxOut[T](variable: RandomVariable[T]): Factor = {
    val newVars = variables.filter(!variable.equals(_))
    new Factor(newVars,
      Factor.spaceFor(newVars)
        .map(kase => (kase, variable.values.getOrElse(Nil).map(value => this(kase)).max))
        .toMap
    )
  }

  def projectToOnly(remainingVars: List[RandomVariable[_]]): Factor =
    new Factor(remainingVars,
      Factor.spaceFor(remainingVars).toList
        .map(kase => (projectToVars(kase, remainingVars.toSet), this(kase)))
        .groupBy(_._1)
        .map({ case (k, v) => (k, v.map(_._2).sum) })
        .toMap
    )

  def tally[A, B](a: RandomVariable[A], b: RandomVariable[B]): Matrix[Double] = {
    val aValues = a.values.getOrElse(Nil).toIndexedSeq
    val bValues = b.values.getOrElse(Nil).toIndexedSeq
    matrix[Double](
      aValues.size,
      bValues.size,
      (r: Int, c: Int) => cases().filter(isSupersetOf(_, List(a eq aValues(r), b eq bValues(c)))).map(this(_)).sum
    )
  }

  def Σ[T](varToSumOut: RandomVariable[T]): Factor = sumOut(varToSumOut)

  // depending on assumptions, this may not be the best way to remove the vars
  def sumOut(gone: RandomVariable[_]): Factor = {
    val position = varList.indexOf(gone)
    val newVars = varList.filter(!_.equals(gone)).toList
    new Factor(newVars,
      Factor.spaceFor(newVars)
        .map(kase => (kase,
          gone.values.getOrElse(Nil).map(gv => {
            val ciGone = List(CaseIs(gone.asInstanceOf[RandomVariable[Any]], gv)) // TODO cast
            this(kase.slice(0, position) ++ ciGone ++ kase.slice(position, kase.length))
          }).reduce(_ + _)
        )).toMap
    )
  }

  def Σ(varsToSumOut: Set[RandomVariable[_]]): Factor = sumOut(varsToSumOut)

  def sumOut(varsToSumOut: Set[RandomVariable[_]]): Factor =
    varsToSumOut.foldLeft(this)((result, v) => result.sumOut(v))

  // as defined on chapter 6 page 15
  def projectRowsConsistentWith(eOpt: Option[List[CaseIs[_]]]): Factor = {
    val e = eOpt.get
    new Factor(variables(),
      Factor.spaceFor(e.map(_.rv)).map(kase => (kase, if (isSupersetOf(kase, e)) this(kase) else 0.0)).toMap
    )
  }

  def *(other: Factor): Factor = {
    val newVars = (variables().toSet union other.variables().toSet).toList
    new Factor(newVars.toList, Factor.spaceFor(newVars).map(kase => (kase, this(kase) * other(kase))).toMap)
  }

  def mentions(variable: RandomVariable[_]) = variables.exists(v => variable.name.equals(v.name))

  def isSupersetOf(left: Seq[CaseIs[_]], right: Seq[CaseIs[_]]): Boolean = {
    val ll: Seq[(RandomVariable[_], Any)] = left.map(ci => (ci.rv, ci.v))
    val lm = ll.toMap
    right.forall((rightCaseIs: CaseIs[_]) => lm.contains(rightCaseIs.rv) && (rightCaseIs.v == lm(rightCaseIs.rv)))
  }

  def projectToVars(cs: Seq[CaseIs[_]], pVars: Set[RandomVariable[_]]): Seq[CaseIs[_]] =
    cs.filter(ci => pVars.contains(ci.rv))

}

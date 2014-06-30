package axle.stats

import spire.algebra.Field
import spire.algebra.Order
import spire.implicits.additiveSemigroupOps
import spire.implicits.eqOps
import spire.implicits.literalIntAdditiveGroupOps
import spire.implicits.multiplicativeGroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.orderOps
import spire.random.Dist
import spire.random.mutable.Cmwc5
import spire.compat.ordering

class ConditionalProbabilityTable0[A, N: Field: Order: Dist](p: Map[A, N], _name: String = "unnamed")
  extends Distribution0[A, N] {

  def name: String = _name
  
  val field = implicitly[Field[N]]

  def map[B](f: A => B): ConditionalProbabilityTable0[B, N] =
    new ConditionalProbabilityTable0[B, N](
      values
        .map({ v => f(v) -> probabilityOf(v) })
        .groupBy(_._1)
        .mapValues(_.map(_._2).reduce(field.plus)))

  def flatMap[B](f: A => Distribution0[B, N]): ConditionalProbabilityTable0[B, N] =
    new ConditionalProbabilityTable0[B, N](
      values
        .flatMap(a => {
          val p = probabilityOf(a)
          val subDistribution = f(a)
          subDistribution.values.map(b => {
            b -> (p * subDistribution.probabilityOf(b))
          })
        })
        .groupBy(_._1)
        .mapValues(_.map(_._2).reduce(field.plus)))

  // TODO Is there a version of scanLeft that is more like a reduce?
  // This would allow me to avoid having to construct the initial dummy element
  val bars = p.scanLeft((null.asInstanceOf[A], field.zero))((x, y) => (y._1, x._2 + y._2))

  val rng = Cmwc5()

  def is(v: A): CaseIs[A, N] = CaseIs(this, v)

  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)
  
  def observe(): A = {
    val r = rng.next[N]
    bars.find(_._2 > r).getOrElse(throw new Exception("malformed distribution"))._1
  }

  def values: IndexedSeq[A] = p.keys.toVector

  def probabilityOf(a: A): N = p.get(a).getOrElse(field.zero)

  def show(implicit order: Order[A]): String =
    s"$name\n" + 
    values.sorted.map(a => {
      val aString = a.toString
      (aString + (1 to (charWidth - aString.length)).map(i => " ").mkString("") + " " + probabilityOf(a).toString)
    }).mkString("\n")

}

class ConditionalProbabilityTable2[A, G1, G2, N: Field: Order](p: Map[(G1, G2), Map[A, N]], _name: String = "unnamed")
  extends Distribution2[A, G1, G2, N] {

  def name: String = _name
  
  lazy val _values = p.values.map(_.keySet).reduce(_ union _).toVector

  def values: IndexedSeq[A] = _values

  def is(v: A): CaseIs[A, N] = CaseIs(this, v)

  def isnt(v: A): CaseIsnt[A, N] = CaseIsnt(this, v)
  
  def observe(): A = ???

  def observe(gv1: G1, gv2: G2): A = ???

  def probabilityOf(a: A): N = ???

  def probabilityOf(a: A, given1: Case[G1, N], given2: Case[G2, N]): N = ???

}

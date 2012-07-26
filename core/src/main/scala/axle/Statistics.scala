package axle

object Statistics {

  import collection._
  import axle.Enrichments._
  import axle.quanta.Information

  implicit def rv2it[K](rv: RandomVariable[K]) = rv.getValues.get

  trait Case[A] {
    def ∧[B](right: Case[B]): Case[(A, B)] = CaseAnd[A, B](this, right)
    def ∨[B](right: Case[B]): Case[(A, B)] = CaseOr[A, B](this, right)
    def |[B](given: Case[B]): Case[(A, B)] = CaseGiven[A, B](this, given)
    def probability[B](given: Option[Case[B]] = None): Double
  }

  case class CaseAndGT[A](conjuncts: GenTraversable[Case[A]]) extends Case[List[A]] {
    def probability[B](given: Option[Case[B]] = None): Double = conjuncts.Π((c: Case[A]) => () => c.probability(given))
  }

  case class CaseAnd[A, B](left: Case[A], right: Case[B]) extends Case[(A, B)] {
    def probability[C](given: Option[Case[C]] = None): Double = left.probability(given) * right.probability(given)
  }

  case class CaseOr[A, B](left: Case[A], right: Case[B]) extends Case[(A, B)] {
    def probability[C](given: Option[Case[C]] = None): Double = left.probability(given) + right.probability(given)
  }

  // TODO: use phantom types to ensure that only one "given" clause is specified
  case class CaseGiven[A, B](c: Case[A], given: Case[B]) extends Case[(A, B)] {
    def probability[C](givenArg: Option[Case[C]] = None): Double = {
      assert(givenArg.isEmpty)
      c.probability(Some(given))
    }
  }

  // TODO: may want CaseIs{With, No] classes to avoid the run-time type-checking below
  case class CaseIs[A](rv: RandomVariable[A], v: A) extends Case[A] {
    def probability[B](given: Option[Case[B]]): Double = rv match {
      case rvNo: RandomVariableNoInput[A] => given
        .map(g => throw new Exception("caller provided a 'given' to a random variable that has no inputs"))
        .getOrElse(rvNo.probability(v))
      case rvWith: RandomVariableWithInput[A, B] => given
        .map(g => rvWith.probability(v, g))
        .getOrElse(rvWith.probability(v))
    }
  }

  case class CaseIsnt[A](rv: RandomVariable[A], v: A) extends Case[A] {
    def probability[B](given: Option[Case[B]] = None): Double = 1.0 - (rv match {
      case rvNo: RandomVariableNoInput[A] => given
        .map(g => throw new Exception("caller provided a 'given' to a random variable that has no inputs"))
        .getOrElse(rvNo.probability(v))
      case rvWith: RandomVariableWithInput[A, B] => given
        .map(g => rvWith.probability(v, g))
        .getOrElse(rvWith.probability(v))
    })
  }

  case class EnrichedCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) {
    def ∧(): Case[List[A]] = CaseAndGT(cgt)
  }

  implicit def enrichCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) = EnrichedCaseGenTraversable(cgt)

  trait RandomVariable[A] {
    def getName(): String
    def getValues(): Option[Set[A]]
    def eq(v: A): Case[A]
    def ne(v: A): Case[A]
    def probability(a: A): Double
  }

  case class RandomVariableNoInput[A](name: String, values: Option[Set[A]] = None, distribution: Option[DistributionNoInput[A]] = None)
    extends RandomVariable[A] {
    def getName() = name
    def getValues() = values
    def eq(v: A): Case[A] = CaseIs(this, v)
    def ne(v: A): Case[A] = CaseIsnt(this, v)
    def probability(a: A): Double = distribution.get.probabilityOf(a)
  }

  case class RandomVariableWithInput[A, G](name: String, values: Option[Set[A]] = None, distribution: Option[DistributionWithInput[A, G]] = None)
    extends RandomVariable[A] {
    def getName() = name
    def getValues() = values
    def eq(v: A): Case[A] = CaseIs(this, v)
    def ne(v: A): Case[A] = CaseIsnt(this, v)
    def probability(a: A): Double = -1.0 // TODO
    def probability(a: A, given: Case[G]): Double = distribution.get.probabilityOf(a, given)
  }

  case class P[A](c: Case[A]) extends Function0[Double] {
    def *(right: Double) = PMultiply(this, () => right)
    def *(right: () => Double) = PMultiply(this, right)
    def apply(): Double = c.probability()
  }

  case class PMultiply[A](left: P[A], right: () => Double) extends Function0[Double] {
    def apply() = left() * right()
  }

  def bayes[A](p: P[A]): () => Double = p.c match {
    // TODO: also check that "left" and "right" have no "given"
    case CaseAnd(left, right) => P(left | right) * bayes(P(right))
    case _ => P(p.c)
  }

  trait DistributionNoInput[A] {

    def getObjects(): Set[A]

    // val d: Map[A, Double]

    def choose(): A

    def probabilityOf(a: A): Double

    def entropy(): Information#UOM

    def H_:(): Information#UOM = entropy()

    def huffmanCode[S](alphabet: Set[S]): Map[A, Seq[S]] = {
      // TODO
      // http://en.wikipedia.org/wiki/Huffman_coding
      Map()
    }

  }

  trait DistributionWithInput[A, G] {

    def getObjects(): Set[A]

    def probabilityOf(a: A): Double

    def probabilityOf(a: A, given: Case[G]): Double

  }

  class TallyDistributionNoInput[A, G](rv: RandomVariable[A], tally: Map[A, Int])
    extends DistributionNoInput[A] {

    val totalCount = tally.values.sum

    def getObjects(): Set[A] = rv.getValues.get

    def choose() = null.asInstanceOf[A] // TODO

    def probabilityOf(a: A): Double = tally(a).toDouble / totalCount

    def entropy() = null // TODO
  }

  class TallyDistributionWithInput[A, G](rv: RandomVariable[A], grv: RandomVariable[G], tally: Map[(A, G), Int])
    extends DistributionWithInput[A, G] {

    def getObjects(): Set[A] = rv.getValues.get

    val totalCount = tally.values.sum

    def probabilityOf(a: A): Double = grv.getValues.get.map(gv => tally((a, gv))).sum / totalCount

    def probabilityOf(a: A, given: Case[G]): Double = given match {
      case CaseIs(argGrv, gv) => tally((a, gv)).toDouble / totalCount
      case CaseIsnt(argGrv, gv) => 1.0 - (tally((a, gv)).toDouble / totalCount)
      case _ => throw new Exception("unhandled case in TallyDistributionWithInput.probabilityOf")
    }

  }

}
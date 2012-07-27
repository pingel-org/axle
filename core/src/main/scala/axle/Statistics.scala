package axle

object Statistics {

  import collection._
  import scala.util.Random
  import axle.Enrichments._

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
      case rvNo: RandomVariable0[A] => given
        .map(g => throw new Exception("caller provided a 'given' to a random variable that has no inputs"))
        .getOrElse(rvNo.probability(v))
      case rvWith: RandomVariable1[A, B] => given
        .map(g => rvWith.probability(v, g))
        .getOrElse(rvWith.probability(v))
    }
  }

  case class CaseIsnt[A](rv: RandomVariable[A], v: A) extends Case[A] {
    def probability[B](given: Option[Case[B]] = None): Double = 1.0 - (rv match {
      case rvNo: RandomVariable0[A] => given
        .map(g => throw new Exception("caller provided a 'given' to a random variable that has no inputs"))
        .getOrElse(rvNo.probability(v))
      case rvWith: RandomVariable1[A, B] => given
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
    def choose(): A
  }

  case class RandomVariable0[A](name: String, values: Option[Set[A]] = None, distribution: Option[Distribution0[A]] = None)
    extends RandomVariable[A] {

    def getName() = name
    def getValues() = values
    def eq(v: A): Case[A] = CaseIs(this, v)
    def ne(v: A): Case[A] = CaseIsnt(this, v)
    def probability(a: A): Double = distribution.get.probabilityOf(a)
    def choose(): A = distribution.get.choose
  }

  case class RandomVariable1[A, G1](name: String, values: Option[Set[A]] = None,
    grv: RandomVariable[G1], distribution: Option[Distribution1[A, G1]] = None)
    extends RandomVariable[A] {

    def getName() = name
    def getValues() = values
    def eq(v: A): Case[A] = CaseIs(this, v)
    def ne(v: A): Case[A] = CaseIsnt(this, v)
    def probability(a: A): Double = -1.0 // "TODO"
    def probability(a: A, given: Case[G1]): Double = distribution.get.probabilityOf(a, given)
    def choose(): A = choose(grv.choose)
    def choose(gv: G1): A = distribution.get.choose(gv)
  }

  case class RandomVariable2[A, G1, G2](name: String, values: Option[Set[A]] = None,
    grv1: RandomVariable[G1], grv2: RandomVariable[G2], distribution: Option[Distribution2[A, G1, G2]] = None)
    extends RandomVariable[A] {

    def getName() = name
    def getValues() = values
    def eq(v: A): Case[A] = CaseIs(this, v)
    def ne(v: A): Case[A] = CaseIsnt(this, v)
    def probability(a: A): Double = -1.0 // "TODO"
    def probability(a: A, given1: Case[G1], given2: Case[G2]): Double = distribution.get.probabilityOf(a, given1, given2)
    def choose(): A = choose(grv1.choose, grv2.choose)
    def choose(gv1: G1, gv2: G2): A = distribution.get.choose(gv1, gv2)
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

  trait Distribution0[A] {
    def choose(): A
    def probabilityOf(a: A): Double
  }

  trait Distribution1[A, G1] {
    def choose(gv: G1): A
    def probabilityOf(a: A): Double
    def probabilityOf(a: A, given: Case[G1]): Double
  }

  trait Distribution2[A, G1, G2] {
    def choose(gv1: G1, gv2: G2): A
    def probabilityOf(a: A): Double
    def probabilityOf(a: A, given1: Case[G1], given2: Case[G2]): Double
  }

  // TODO: division by zero

  class ConditionalProbabilityTable0[A](p: Map[A, Double]) extends Distribution0[A] {

    // def randomStream(): Stream[Double] = Stream.cons(math.random, randomStream())

    // TODO Is there a version of scanLeft that is more like a reduce?
    // This would allow me to avoid having to construct the initial dummy element
    val bars = p.scanLeft((null.asInstanceOf[A], 0.0))((x, y) => (y._1, x._2 + y._2))

    def choose(): A = {
      val r = math.random
      bars.find(_._2 > r).getOrElse(throw new Exception("malformed distribution"))._1
    }

    def probabilityOf(a: A): Double = p(a)
  }

  class ConditionalProbabilityTable2[A, G1, G2](p: Map[(G1, G2), Map[A, Double]]) extends Distribution2[A, G1, G2] {

    // def randomStream(): Stream[Double] = Stream.cons(math.random, randomStream())

    // TODO Is there a version of scanLeft that is more like a reduce?
    // This would allow me to avoid having to construct the initial dummy element
    // val barsByA = p.scanLeft((null.asInstanceOf[A], 0.0))((x, y) => (y._1._1, x._2 + y._2))

    def choose(): A = null.asInstanceOf[A] // TODO

    def choose(gv1: G1, gv2: G2): A = null.asInstanceOf[A] // TODO

    def probabilityOf(a: A): Double = -1.0 // TODO

    def probabilityOf(a: A, given1: Case[G1], given2: Case[G2]): Double = -1.0 // TODO

  }

  class TallyDistribution0[A](tally: Map[A, Int])
    extends Distribution0[A] {

    val totalCount = tally.values.sum

    val bars = tally.scanLeft((null.asInstanceOf[A], 0.0))((x, y) => (y._1, x._2 + y._2))

    def choose(): A = {
      val r = Random.nextInt(totalCount + 1)
      bars.find(_._2 > r).getOrElse(throw new Exception("malformed distribution"))._1
    }

    def probabilityOf(a: A): Double = tally(a).toDouble / totalCount
  }

  class TallyDistribution1[A, G](tally: Map[(A, G), Int])
    extends Distribution1[A, G] {

    val gvs = tally.keys.map(k => k._2).toSet

    val totalCount = tally.values.sum

    def choose(): A = null.asInstanceOf[A] // TODO

    def choose(gv: G): A = null.asInstanceOf[A] // TODO

    def probabilityOf(a: A): Double = gvs.map(gv => tally((a, gv))).sum / totalCount

    def probabilityOf(a: A, given: Case[G]): Double = given match {
      case CaseIs(argGrv, gv) => tally((a, gv)).toDouble / tally.filter(_._1._2 == gv).map(_._2).sum
      case CaseIsnt(argGrv, gv) => 1.0 - (tally((a, gv)).toDouble / tally.filter(_._1._2 == gv).map(_._2).sum)
      case _ => throw new Exception("unhandled case in TallyDistributionWithInput.probabilityOf")
    }

  }

  def coin(pHead: Double = 0.5) = RandomVariable0("coin",
    values = Some(Set('HEAD, 'TAIL)),
    distribution = Some(new ConditionalProbabilityTable0(Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))))

}
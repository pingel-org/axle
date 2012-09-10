package axle.stats

trait RandomVariable[A] {
  def getName(): String
  def getValues(): Option[IndexedSeq[A]]
  def eq(v: A): CaseIs[A]
  def ne(v: A): CaseIsnt[A]
  def probability(a: A): Double
  def observe(): A
  lazy val charWidth: Int = (getName().length :: getValues().map(vs => vs.map(_.toString.length).toList).getOrElse(Nil)).reduce(math.max)
}

case class RandomVariable0[A](name: String, values: Option[IndexedSeq[A]] = None, distribution: Option[Distribution0[A]] = None)
  extends RandomVariable[A] {

  def getName() = name
  def getValues() = values
  def eq(v: A): CaseIs[A] = CaseIs(this, v)
  def ne(v: A): CaseIsnt[A] = CaseIsnt(this, v)
  def probability(a: A): Double = distribution.map(_.probabilityOf(a)).getOrElse(0.0)
  def observe(): A = distribution.get.observe

}

case class RandomVariable1[A, G1](name: String, values: Option[IndexedSeq[A]] = None,
  grv: RandomVariable[G1], distribution: Option[Distribution1[A, G1]] = None)
  extends RandomVariable[A] {

  def getName() = name
  def getValues() = values
  def eq(v: A): CaseIs[A] = CaseIs(this, v)
  def ne(v: A): CaseIsnt[A] = CaseIsnt(this, v)
  def probability(a: A): Double = -1.0 // "TODO"
  def probability(a: A, given: Case[G1]): Double = distribution.map(_.probabilityOf(a, given)).getOrElse(0.0)
  def observe(): A = observe(grv.observe)
  def observe(gv: G1): A = distribution.get.observe(gv)

}

case class RandomVariable2[A, G1, G2](name: String, values: Option[IndexedSeq[A]] = None,
  grv1: RandomVariable[G1], grv2: RandomVariable[G2], distribution: Option[Distribution2[A, G1, G2]] = None)
  extends RandomVariable[A] {

  def getName() = name
  def getValues() = values
  def eq(v: A): CaseIs[A] = CaseIs(this, v)
  def ne(v: A): CaseIsnt[A] = CaseIsnt(this, v)
  def probability(a: A): Double = -1.0 // "TODO"
  def probability(a: A, given1: Case[G1], given2: Case[G2]): Double = distribution.map(_.probabilityOf(a, given1, given2)).getOrElse(0.0)
  def observe(): A = observe(grv1.observe, grv2.observe)
  def observe(gv1: G1, gv2: G2): A = distribution.get.observe(gv1, gv2)

}

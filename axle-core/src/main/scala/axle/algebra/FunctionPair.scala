package axle.algebra

/**
 * A FunctionPair is not necessarily a bijection, but it could be.
 *
 */

trait FunctionPair[A, B] {

  f =>

  def apply(a: A): B

  def unapply(b: B): A

  def compose[C](g: FunctionPair[B, C]): FunctionPair[A, C] = new FunctionPair[A, C] {

    lazy val fog = f.compose(g)

    def apply(a: A): C = fog(a)

    lazy val ufog = (f.unapply _).compose(g.unapply _)

    def unapply(c: C): A = ufog(c)
  }
}

object FunctionPair {

  implicit val doubleIdentity: FunctionPair[Double, Double] = new FunctionPair[Double, Double] {
    def apply(d: Double) = d
    def unapply(t: Double) = t
  }
  
}
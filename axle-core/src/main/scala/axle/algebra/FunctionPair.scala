package axle.algebra

import scala.annotation.implicitNotFound

/**
 * A FunctionPair is not necessarily a bijection, but it could be.
 *
 */

@implicitNotFound("Witness not found for FunctionPair[${A}, ${B}]")
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

  final def apply[A, B](implicit ev: FunctionPair[A, B]): FunctionPair[A, B] = ev

  implicit val convertDouble: FunctionPair[Double, Double] = new FunctionPair[Double, Double] {
    def apply(d: Double) = d
    def unapply(t: Double) = t
  }

  implicit val convertInt: FunctionPair[Double, Int] = new FunctionPair[Double, Int] {
    def apply(d: Double) = d.toInt
    def unapply(t: Int) = t.toDouble
  }

  implicit val convertBoolean: FunctionPair[Double, Boolean] = new FunctionPair[Double, Boolean] {
    def apply(d: Double) = d != 0d
    def unapply(t: Boolean) = t match { case true => 0d case false => 1d }
  }

}
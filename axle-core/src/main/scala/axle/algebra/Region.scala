
package axle.algebra

import cats.Show
import cats.implicits._

trait Region[A] extends Function1[A, Boolean] {

  def apply(a: A): Boolean
}

object Region {

  // TODO HList instead of Tuple2
  def combine[A, B](left: Region[A], right: Region[B]): Region[(A, B)] =
    RegionAnd[A, B](left, right)

  def showRegion[A: Show]: Show[Region[A]] = new Show[Region[A]] {

    def show(ra: Region[A]): String = ra match {
      case re @ RegionEq(x) => show"_ === ${x}"
      case rs @ RegionSet(xs) => show"_ in ${xs}"
      case rr @ RegionRange(min, max) => show"${min} <= _ < ${max}"
      case ra @ RegionAnd(left, right) => left.toString + " && (" + right.toString + ")" // TODO
    }
  }

}
import cats.kernel.Eq
import cats.kernel.Order

case class RegionEq[A](x: A)(implicit val eqA: Eq[A]) extends Region[A] {

  def apply(y: A): Boolean = eqA.eqv(x, y)
}

object RegionEq {

  implicit def reqEq[A]: cats.kernel.Eq[RegionEq[A]] = new cats.kernel.Eq[RegionEq[A]] {
    def eqv(left: RegionEq[A], right: RegionEq[A]): Boolean = left.eqA.eqv(left.x, right.x)
  }
}

case class RegionSet[A](xs: Set[A]) extends Region[A] {

  def apply(x: A): Boolean = xs(x)
}

case class RegionRange[A](min: A, max: A)(implicit ordA: Order[A]) extends Region[A] {

  def apply(x: A): Boolean = ordA.gteqv(x, min) && ordA.lt(x, max)
}

case class RegionAnd[A, B](left: Region[A], right: Region[B]) extends Region[(A, B)] {

  def apply(xy: (A, B)): Boolean = left(xy._1) && right(xy._2)
}
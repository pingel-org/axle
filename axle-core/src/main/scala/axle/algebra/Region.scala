
package axle.algebra

import cats.Show
import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._

trait Region[A] extends Function1[A, Boolean] {

  def apply(a: A): Boolean

  def and(right: Region[A]): Region[A] = RegionAnd[A](this, right)

  def or(right: Region[A]): Region[A] = RegionOr[A](this, right)

}

object Region {

  def eqRegionIterable[A](itA: Iterable[A]): Eq[Region[A]] =
    new Eq[Region[A]] {
      def eqv(left: Region[A], right: Region[A]): Boolean = 
        itA.forall(a => left(a) === right(a))
    }

  implicit def showRegion[A: Show]: Show[Region[A]] = new Show[Region[A]] {

    def show(ra: Region[A]): String = ra match {
      case re @ RegionEq(x) => show"_ === ${x}"
      // case re @ RegionEqTuple1of2(x) => show"_._1 === ${x}"
      // case re @ RegionEqTuple2of2(x) => show"_._2 === ${x}"
      case rs @ RegionSet(xs) => show"_ in ${xs}"
      case rr @ RegionRange(min, max) => show"${min} <= _ < ${max}"
      case ra @ RegionAnd(left, right) => "(" + left.toString + ") ⋀ (" + right.toString + ")"
      case ro @ RegionOr(left, right) => "(" + left.toString + ") ⋁ (" + right.toString + ")"
      case RegionEmpty() => "∅"
      case RegionAll() => "ξ"
    }
  }

}

case class RegionEmpty[A]() extends Region[A] {
  def apply(x: A): Boolean = false
}

case class RegionAll[A]() extends Region[A] {
  def apply(x: A): Boolean = true
}

case class RegionEq[A](x: A)(implicit val eqA: Eq[A]) extends Region[A] {
  def apply(y: A): Boolean = eqA.eqv(x, y)
}

object RegionEq {

  implicit def reqEq[A]: Eq[RegionEq[A]] = new Eq[RegionEq[A]] {
    def eqv(left: RegionEq[A], right: RegionEq[A]): Boolean = left.eqA.eqv(left.x, right.x)
  }
}

case class RegionEqTuple1of2[A, B](a: A)(implicit val eqA: Eq[A]) extends Region[(A, B)] {
  def apply(pair: (A, B)): Boolean = eqA.eqv(a, pair._1)
}

case class RegionEqTuple2of2[A, B](b: B)(implicit val eqB: Eq[B]) extends Region[(A, B)] {
  def apply(pair: (A, B)): Boolean = eqB.eqv(b, pair._2)
}

case class RegionSet[A](xs: Set[A]) extends Region[A] {

  def apply(x: A): Boolean = xs(x)
}

case class RegionRange[A](min: A, max: A)(implicit ordA: Order[A]) extends Region[A] {

  def apply(x: A): Boolean = ordA.gteqv(x, min) && ordA.lt(x, max)
}

case class RegionAnd[A](left: Region[A], right: Region[A]) extends Region[A] {

  def apply(a: A): Boolean = left(a) && right(a)
}

case class RegionOr[A](left: Region[A], right: Region[A]) extends Region[A] {

  def apply(a: A): Boolean = left(a) || right(a)
}
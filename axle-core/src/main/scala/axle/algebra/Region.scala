
package axle.algebra

import cats.Show
import cats.kernel.Eq
import cats.kernel.Order
import cats.implicits._

sealed trait Region[A] extends Function1[A, Boolean] {

  //def apply(a: A): Boolean

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
      case RegionEmpty() => "∅"
      case RegionAll() => "ξ"
      case re @ RegionEq(x) => show"_ === ${x}"
      case rn @ RegionNegate(r) => show"not (${r})"
      case ra @ RegionAnd(left, right) => "(" + left.toString + ") ⋀ (" + right.toString + ")"
      case ro @ RegionOr(left, right) => "(" + left.toString + ") ⋁ (" + right.toString + ")"
      case ri @ RegionIf(_) => "if(…)"
      case rs @ RegionSet(xs) => show"_ in ${xs}"
      case rg @ RegionGTE(min) => show"${min} <= _"
      case rl @ RegionLTE(max) => show"_ <= ${max}"
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

case class RegionIf[A](filter: A => Boolean) extends Region[A] {

  def apply(x: A): Boolean = filter(x)
}

case class RegionSet[A](xs: Set[A]) extends Region[A] {

  def apply(x: A): Boolean = xs(x)
}

case class RegionNegate[A](sub: Region[A]) extends Region[A] {

  def apply(x: A): Boolean = !sub(x)
}

case class RegionGTE[A](cutoff: A)(implicit ordA: Order[A]) extends Region[A] {

  def apply(x: A): Boolean = ordA.gteqv(x, cutoff)
}

case class RegionLTE[A](cutoff: A)(implicit ordA: Order[A]) extends Region[A] {

  def apply(x: A): Boolean = ordA.lteqv(x, cutoff)
}

case class RegionAnd[A](left: Region[A], right: Region[A]) extends Region[A] {

  def apply(a: A): Boolean = left(a) && right(a)
}

case class RegionOr[A](left: Region[A], right: Region[A]) extends Region[A] {

  def apply(a: A): Boolean = left(a) || right(a)
}
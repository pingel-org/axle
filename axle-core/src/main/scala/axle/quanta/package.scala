package axle

import scala.language.reflectiveCalls

import axle.algebra.DirectedGraph
import axle.algebra.LengthSpace
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.algebra.Zero
import axle.quanta.Quantum
import axle.quanta.UnitOfMeasurement
import axle.quanta.UnittedQuantity
import axle.quanta.Quantum3
import axle.quanta.UnitOfMeasurement3
import axle.quanta.UnittedQuantity3
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.Module
import spire.algebra.Order
import spire.algebra.Rng
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.signedOps
import spire.implicits._

package object quanta {

  // Note: this is need for "def conversions"
  implicit def edgeEq[N: Eq]: Eq[N => N] = new Eq[N => N] {
    def eqv(x: N => N, y: N => N): Boolean = ???
  }

  implicit def modulize[Q <: Quantum, N, DG[_, _]: DirectedGraph](implicit fieldn: Field[N], eqn: Eq[N], cg: DG[UnitOfMeasurement[Q, N], N => N]): Module[UnittedQuantity[Q, N], N] =
    new Module[UnittedQuantity[Q, N], N] {

      def negate(x: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] = UnittedQuantity(-x.magnitude, x.unit) // AdditiveGroup

      def zero: UnittedQuantity[Q, N] = ??? // UnittedQuantity("zero", "zero", None) // AdditiveMonoid

      def plus(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] =
        UnittedQuantity((x in y.unit).magnitude + y.magnitude, y.unit) // AdditiveSemigroup

      implicit def scalar: Rng[N] = fieldn // Module

      def timesl(r: N, v: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] = UnittedQuantity(v.magnitude * r, v.unit)
    }

  implicit def modulize3[Q <: Quantum3, N, DG[_, _]: DirectedGraph](implicit fieldn: Field[N], eqn: Eq[N], cg: DG[UnitOfMeasurement3[Q, N], N => N]): Module[UnittedQuantity3[Q, N], N] =
    new Module[UnittedQuantity3[Q, N], N] {

      def negate(x: UnittedQuantity3[Q, N]): UnittedQuantity3[Q, N] = UnittedQuantity3(-x.magnitude, x.unit) // AdditiveGroup

      def zero: UnittedQuantity3[Q, N] = ??? // UnittedQuantity("zero", "zero", None) // AdditiveMonoid

      def plus(x: UnittedQuantity3[Q, N], y: UnittedQuantity3[Q, N]): UnittedQuantity3[Q, N] =
        UnittedQuantity3((x in y.unit).magnitude + y.magnitude, y.unit) // AdditiveSemigroup

      implicit def scalar: Rng[N] = fieldn // Module

      def timesl(r: N, v: UnittedQuantity3[Q, N]): UnittedQuantity3[Q, N] = UnittedQuantity3(v.magnitude * r, v.unit)
    }

  def unit[Q <: Quantum, N: Field: Eq](name: String, symbol: String, linkOpt: Option[String] = None): UnitOfMeasurement[Q, N] =
    UnitOfMeasurement(name, symbol, linkOpt)

  implicit def uqPlottable[Q <: Quantum, N: Field: Eq: Plottable]: Plottable[UnittedQuantity[Q, N]] =
    new Plottable[UnittedQuantity[Q, N]] {

      override def isPlottable(t: UnittedQuantity[Q, N]): Boolean = implicitly[Plottable[N]].isPlottable(t.magnitude)
    }

  implicit def uq3Plottable[Q <: Quantum3, N: Field: Eq: Plottable]: Plottable[UnittedQuantity3[Q, N]] =
    new Plottable[UnittedQuantity3[Q, N]] {

      override def isPlottable(t: UnittedQuantity3[Q, N]): Boolean = implicitly[Plottable[N]].isPlottable(t.magnitude)
    }

  implicit def unitOrder[Q <: Quantum, N: Field: Order, DG[_, _]: DirectedGraph](implicit base: UnitOfMeasurement[Q, N], cg: DG[UnitOfMeasurement[Q, N], N => N]) =
    new Order[UnittedQuantity[Q, N]] {

      val underlying = implicitly[Order[N]]

      def compare(u1: UnittedQuantity[Q, N], u2: UnittedQuantity[Q, N]): Int =
        underlying.compare((u1 in base).magnitude, (u2 in base).magnitude)
    }

  implicit def unit3Order[Q <: Quantum3, N: Field: Order, DG[_, _]: DirectedGraph](implicit base: UnitOfMeasurement3[Q, N], cg: DG[UnitOfMeasurement3[Q, N], N => N]) =
    new Order[UnittedQuantity3[Q, N]] {

      val underlying = implicitly[Order[N]]

      def compare(u1: UnittedQuantity3[Q, N], u2: UnittedQuantity3[Q, N]): Int =
        underlying.compare((u1 in base).magnitude, (u2 in base).magnitude)
    }

  implicit def unittedZero[Q <: Quantum, N: Field, DG[_, _]: DirectedGraph](implicit base: UnitOfMeasurement[Q, N], cg: DG[UnitOfMeasurement[Q, N], N => N]) =
    new Zero[UnittedQuantity[Q, N]] {

      val field = implicitly[Field[N]]

      def zero: UnittedQuantity[Q, N] = field.zero *: base

    }

  implicit def unitted3Zero[Q <: Quantum3, N: Field, DG[_, _]: DirectedGraph](implicit base: UnitOfMeasurement3[Q, N], cg: DG[UnitOfMeasurement3[Q, N], N => N]) =
    new Zero[UnittedQuantity3[Q, N]] {

      val field = implicitly[Field[N]]

      def zero: UnittedQuantity3[Q, N] = field.zero *: base

    }

  implicit def unittedTics[Q <: Quantum, N: Field: Eq: Tics: Show, DG[_, _]: DirectedGraph](implicit base: UnitOfMeasurement[Q, N], cg: DG[UnitOfMeasurement[Q, N], N => N]) =
    new Tics[UnittedQuantity[Q, N]] {

      def tics(from: UnittedQuantity[Q, N], to: UnittedQuantity[Q, N]): Seq[(UnittedQuantity[Q, N], String)] =
        implicitly[Tics[N]].tics((from in base).magnitude, (to in base).magnitude) map {
          case (v, label) => {
            val vu = UnittedQuantity[Q, N](v, base)
            (vu, string(v))
          }
        }
    }

  implicit def unitted3Tics[Q <: Quantum3, N: Field: Eq: Tics: Show, DG[_, _]: DirectedGraph](implicit base: UnitOfMeasurement3[Q, N], cg: DG[UnitOfMeasurement3[Q, N], N => N]) =
    new Tics[UnittedQuantity3[Q, N]] {

      def tics(from: UnittedQuantity3[Q, N], to: UnittedQuantity3[Q, N]): Seq[(UnittedQuantity3[Q, N], String)] =
        implicitly[Tics[N]].tics((from in base).magnitude, (to in base).magnitude) map {
          case (v, label) => {
            val vu = UnittedQuantity3[Q, N](v, base)
            (vu, string(v))
          }
        }
    }

  implicit def unittedLengthSpace[Q <: Quantum, N: Field: Order, DG[_, _]: DirectedGraph](
    implicit base: UnitOfMeasurement[Q, N], space: LengthSpace[N, Double],
    cg: DG[UnitOfMeasurement[Q, N], N => N],
    module: Module[UnittedQuantity[Q, N], N]) =
    new LengthSpace[UnittedQuantity[Q, N], UnittedQuantity[Q, N]] {

      val field = implicitly[Field[N]]

      def distance(v: UnittedQuantity[Q, N], w: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] = ???
      //(field.minus((v in base).magnitude, (w in base).magnitude).abs) *: base

      def onPath(left: UnittedQuantity[Q, N], right: UnittedQuantity[Q, N], p: Double): UnittedQuantity[Q, N] = ???
      //((field.minus((right in base).magnitude, (left in base).magnitude)) * p + (left in base).magnitude) *: base

      def portion(left: UnittedQuantity[Q, N], v: UnittedQuantity[Q, N], right: UnittedQuantity[Q, N]): Double = ???
      // space.portion((left in base).magnitude, (v in base).magnitude, (right in base).magnitude)

    }

  implicit def unitted3LengthSpace[Q <: Quantum3, N: Field: Order, DG[_, _]: DirectedGraph](
    implicit base: UnitOfMeasurement3[Q, N], space: LengthSpace[N, Double],
    cg: DG[UnitOfMeasurement3[Q, N], N => N],
    module: Module[UnittedQuantity3[Q, N], N]) =
    new LengthSpace[UnittedQuantity3[Q, N], UnittedQuantity3[Q, N]] {

      val field = implicitly[Field[N]]

      def distance(v: UnittedQuantity3[Q, N], w: UnittedQuantity3[Q, N]): UnittedQuantity3[Q, N] =
        (field.minus((v in base).magnitude, (w in base).magnitude).abs) *: base

      def onPath(left: UnittedQuantity3[Q, N], right: UnittedQuantity3[Q, N], p: Double): UnittedQuantity3[Q, N] =
        ((field.minus((right in base).magnitude, (left in base).magnitude)) * p + (left in base).magnitude) *: base

      def portion(left: UnittedQuantity3[Q, N], v: UnittedQuantity3[Q, N], right: UnittedQuantity3[Q, N]): Double =
        space.portion((left in base).magnitude, (v in base).magnitude, (right in base).magnitude)

    }

}
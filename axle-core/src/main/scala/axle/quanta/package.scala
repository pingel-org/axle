package axle

import scala.language.reflectiveCalls

import axle.algebra.DirectedGraph
import axle.algebra.LengthSpace
import axle.algebra.Plottable
import axle.algebra.Tics
import axle.algebra.Zero
import axle.algebra.Vertex
import axle.algebra.Bijection
import spire.algebra.AdditiveMonoid
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.Module
import spire.algebra.MultiplicativeMonoid
import spire.algebra.Order
import spire.algebra.Rng
import spire.implicits.additiveGroupOps
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.signedOps
import spire.implicits._

package object quanta {

  type CG[Q <: Quantum4[N], DG[_, _], N] = DG[UnitOfMeasurement4[Q, N], N => N]

  implicit def modulize4[N, Q <: Quantum4[N], DG[_, _]: DirectedGraph](implicit fieldn: Field[N], eqn: Eq[N], cg: DG[UnitOfMeasurement4[Q, N], N => N]): Module[UnittedQuantity4[Q, N], N] =
    new Module[UnittedQuantity4[Q, N], N] {

      def negate(x: UnittedQuantity4[Q, N]): UnittedQuantity4[Q, N] = UnittedQuantity4(-x.magnitude, x.unit) // AdditiveGroup

      def zero: UnittedQuantity4[Q, N] = ??? // UnittedQuantity("zero", "zero", None) // AdditiveMonoid

      def plus(x: UnittedQuantity4[Q, N], y: UnittedQuantity4[Q, N]): UnittedQuantity4[Q, N] =
        UnittedQuantity4((x in y.unit).magnitude + y.magnitude, y.unit) // AdditiveSemigroup

      implicit def scalar: Rng[N] = fieldn // Module

      def timesl(r: N, v: UnittedQuantity4[Q, N]): UnittedQuantity4[Q, N] = UnittedQuantity4(v.magnitude * r, v.unit)
    }

  //  def unit[Q <: Quantum, N](name: String, symbol: String, linkOpt: Option[String] = None): UnitOfMeasurement[Q, N] =
  //    UnitOfMeasurement(name, symbol, linkOpt)

  implicit def uqPlottable[Q <: Quantum4[N], N: Plottable]: Plottable[UnittedQuantity4[Q, N]] =
    new Plottable[UnittedQuantity4[Q, N]] {

      override def isPlottable(t: UnittedQuantity4[Q, N]): Boolean = implicitly[Plottable[N]].isPlottable(t.magnitude)
    }

  implicit def unitOrder[Q <: Quantum4[N], N: MultiplicativeMonoid: Order, DG[_, _]: DirectedGraph](implicit base: UnitOfMeasurement4[Q, N], cg: DG[UnitOfMeasurement4[Q, N], N => N]) =
    new Order[UnittedQuantity4[Q, N]] {

      val underlying = implicitly[Order[N]]

      def compare(u1: UnittedQuantity4[Q, N], u2: UnittedQuantity4[Q, N]): Int =
        underlying.compare((u1 in base).magnitude, (u2 in base).magnitude)
    }

  implicit def unittedZero[Q <: Quantum4[N], N: AdditiveMonoid, DG[_, _]: DirectedGraph](
    implicit base: UnitOfMeasurement4[Q, N],
    cg: DG[UnitOfMeasurement4[Q, N], N => N]): Zero[UnittedQuantity4[Q, N]] =
    new Zero[UnittedQuantity4[Q, N]] {

      val am = implicitly[AdditiveMonoid[N]]

      def zero: UnittedQuantity4[Q, N] = am.zero *: base

    }

  implicit def unittedTics[Q <: Quantum4[N], N: Field: Eq: Tics: Show, DG[_, _]: DirectedGraph](
    implicit base: UnitOfMeasurement4[Q, N],
    cg: DG[UnitOfMeasurement4[Q, N], N => N]): Tics[UnittedQuantity4[Q, N]] =
    new Tics[UnittedQuantity4[Q, N]] {

      def tics(from: UnittedQuantity4[Q, N], to: UnittedQuantity4[Q, N]): Seq[(UnittedQuantity4[Q, N], String)] =
        implicitly[Tics[N]].tics((from in base).magnitude, (to in base).magnitude) map {
          case (v, label) => {
            val vu = UnittedQuantity4[Q, N](v, base)
            (vu, string(v))
          }
        }
    }

  implicit def unittedLengthSpace[Q <: Quantum4[N], N: Field: Order, DG[_, _]: DirectedGraph](
    implicit base: UnitOfMeasurement4[Q, N], space: LengthSpace[N, Double],
    cg: DG[UnitOfMeasurement4[Q, N], N => N],
    module: Module[UnittedQuantity4[Q, N], N]) =
    new LengthSpace[UnittedQuantity4[Q, N], UnittedQuantity4[Q, N]] {

      val field = implicitly[Field[N]]

      def distance(v: UnittedQuantity4[Q, N], w: UnittedQuantity4[Q, N]): UnittedQuantity4[Q, N] =
        (field.minus((v in base).magnitude, (w in base).magnitude).abs) *: base

      def onPath(left: UnittedQuantity4[Q, N], right: UnittedQuantity4[Q, N], p: Double): UnittedQuantity4[Q, N] =
        ((field.minus((right in base).magnitude, (left in base).magnitude)) * p + (left in base).magnitude) *: base

      def portion(left: UnittedQuantity4[Q, N], v: UnittedQuantity4[Q, N], right: UnittedQuantity4[Q, N]): Double =
        space.portion((left in base).magnitude, (v in base).magnitude, (right in base).magnitude)

    }

}
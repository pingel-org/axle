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
//import spire.implicits._

package object quanta {

  type CG[Q, DG[_, _], N] = DG[UnitOfMeasurement[Q, N], N => N]

  implicit def modulize[N, Q, DG[_, _]: DirectedGraph](implicit fieldn: Field[N], eqn: Eq[N], cg: DG[UnitOfMeasurement[Q, N], N => N]): Module[UnittedQuantity[Q, N], N] =
    new Module[UnittedQuantity[Q, N], N] {

      def negate(x: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] = UnittedQuantity(-x.magnitude, x.unit) // AdditiveGroup

      def zero: UnittedQuantity[Q, N] = ??? // UnittedQuantity("zero", "zero", None) // AdditiveMonoid

      def plus(x: UnittedQuantity[Q, N], y: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] =
        UnittedQuantity((x in y.unit).magnitude + y.magnitude, y.unit) // AdditiveSemigroup

      implicit def scalar: Rng[N] = fieldn // Module

      def timesl(r: N, v: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] = UnittedQuantity(v.magnitude * r, v.unit)
    }

  implicit def uqPlottable[Q, N: Plottable]: Plottable[UnittedQuantity[Q, N]] =
    new Plottable[UnittedQuantity[Q, N]] {

      override def isPlottable(t: UnittedQuantity[Q, N]): Boolean = implicitly[Plottable[N]].isPlottable(t.magnitude)
    }

  implicit def unitOrder[Q, N: MultiplicativeMonoid: Order, DG[_, _]: DirectedGraph](implicit base: UnitOfMeasurement[Q, N], cg: DG[UnitOfMeasurement[Q, N], N => N]) =
    new Order[UnittedQuantity[Q, N]] {

      val underlying = implicitly[Order[N]]

      def compare(u1: UnittedQuantity[Q, N], u2: UnittedQuantity[Q, N]): Int =
        underlying.compare((u1 in base).magnitude, (u2 in base).magnitude)
    }

  implicit def unittedZero[Q, N: AdditiveMonoid, DG[_, _]: DirectedGraph](
    implicit base: UnitOfMeasurement[Q, N],
    cg: DG[UnitOfMeasurement[Q, N], N => N]): Zero[UnittedQuantity[Q, N]] =
    new Zero[UnittedQuantity[Q, N]] {

      val am = implicitly[AdditiveMonoid[N]]

      def zero: UnittedQuantity[Q, N] = am.zero *: base

    }

  implicit def unittedTics[Q, N: Field: Eq: Tics: Show, DG[_, _]: DirectedGraph](
    implicit base: UnitOfMeasurement[Q, N],
    cg: DG[UnitOfMeasurement[Q, N], N => N]): Tics[UnittedQuantity[Q, N]] =
    new Tics[UnittedQuantity[Q, N]] {

      def tics(from: UnittedQuantity[Q, N], to: UnittedQuantity[Q, N]): Seq[(UnittedQuantity[Q, N], String)] =
        implicitly[Tics[N]].tics((from in base).magnitude, (to in base).magnitude) map {
          case (v, label) => {
            val vu = UnittedQuantity[Q, N](v, base)
            (vu, string(v))
          }
        }
    }

  implicit def unittedLengthSpace[Q, N: Field: Order, DG[_, _]: DirectedGraph](
    implicit base: UnitOfMeasurement[Q, N], space: LengthSpace[N, Double],
    cg: DG[UnitOfMeasurement[Q, N], N => N],
    module: Module[UnittedQuantity[Q, N], N]) =
    new LengthSpace[UnittedQuantity[Q, N], UnittedQuantity[Q, N]] {

      val field = implicitly[Field[N]]

      def distance(v: UnittedQuantity[Q, N], w: UnittedQuantity[Q, N]): UnittedQuantity[Q, N] =
        (field.minus((v in base).magnitude, (w in base).magnitude).abs) *: base

      def onPath(left: UnittedQuantity[Q, N], right: UnittedQuantity[Q, N], p: Double): UnittedQuantity[Q, N] =
        ((field.minus((right in base).magnitude, (left in base).magnitude)) * p + (left in base).magnitude) *: base

      def portion(left: UnittedQuantity[Q, N], v: UnittedQuantity[Q, N], right: UnittedQuantity[Q, N]): Double =
        space.portion((left in base).magnitude, (v in base).magnitude, (right in base).magnitude)

    }

  private def conversions[Q, N, DG[_, _]](
    vps: Seq[UnitOfMeasurement[Q, N]],
    ef: Seq[Vertex[UnitOfMeasurement[Q, N]]] => Seq[(Vertex[UnitOfMeasurement[Q, N]], Vertex[UnitOfMeasurement[Q, N]], N => N)])(
      implicit evDG: DirectedGraph[DG]): DG[UnitOfMeasurement[Q, N], N => N] =
    evDG.make[UnitOfMeasurement[Q, N], N => N](vps, ef)

  private def cgn[Q, N, DG[_, _]: DirectedGraph](
    units: List[UnitOfMeasurement[Q, N]],
    links: Seq[(UnitOfMeasurement[Q, N], UnitOfMeasurement[Q, N], Bijection[N, N])]): CG[Q, DG, N] =
    conversions[Q, N, DG](
      units,
      (vs: Seq[Vertex[UnitOfMeasurement[Q, N]]]) => {
        val name2vertex = vs.map(v => (v.payload.name, v)).toMap
        links.flatMap({
          case (x, y, bijection) => {
            val xv = name2vertex(x.name)
            val yv = name2vertex(y.name)
            List((xv, yv, bijection.apply _), (yv, xv, bijection.unapply _))
          }
        })
      })

  implicit def conversionGraph[Q, N: Field: Eq, DG[_, _]: DirectedGraph](implicit meta: QuantumMetadata[Q, N]) =
    cgn(meta.units, meta.links(implicitly[Field[N]]))

}
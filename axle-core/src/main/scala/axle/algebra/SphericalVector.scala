package axle.algebra

import axle.cosine
import axle.quanta.Angle
import axle.quanta.AngleConverter
import axle.quanta.Distance
import axle.quanta.UnittedQuantity
import axle.sine
import spire.algebra.Eq
import spire.algebra.Module
import spire.algebra.MultiplicativeMonoid
import spire.implicits.moduleOps
import spire.math.ConvertableFrom

/**
 *
 * Uses the physics conventions instead of mathematical conventions.
 *
 * http://en.wikipedia.org/wiki/Spherical_coordinate_system
 *
 */
case class SphericalVector[N]( // Field: Order
  ρ: UnittedQuantity[Distance, N], // radius
  θ: UnittedQuantity[Angle, N], // 90 - latitude N, aka "co-latitude"
  φ: UnittedQuantity[Angle, N] // longitude E
  ) {

  type P = (UnittedQuantity[Distance, N], UnittedQuantity[Distance, N], UnittedQuantity[Distance, N])

  def toPosition(
    implicit ac: AngleConverter[N],
    mmn: MultiplicativeMonoid[N],
    eqn: Eq[N],
    cfn: ConvertableFrom[N],
    modn: Module[UnittedQuantity[Distance, N], Double],
    pos: Position3DSpace[N, P]): P =
    (
      ρ :* (sine(θ) * cosine(φ)),
      ρ :* (sine(θ) * sine(φ)),
      ρ :* cosine(θ))

  // TODO could also define a toCertesianVector method that would just call toPosition for the
  // "to" half, and use a "zero" position for the "x"

}

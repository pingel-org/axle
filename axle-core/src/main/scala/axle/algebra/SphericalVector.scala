package axle.algebra

import cats.kernel.Eq
import spire.algebra.MultiplicativeMonoid
import spire.algebra.Trig
import spire.algebra.CModule
import spire.implicits.multiplicativeSemigroupOps
import spire.implicits.rightModuleOps
import axle.math.cosine
import axle.quanta.Angle
import axle.quanta.AngleConverter
import axle.quanta.Distance
import axle.quanta.UnittedQuantity
import axle.math.sine

/**
 *
 * Uses the physics conventions instead of mathematical conventions.
 *
 * http://en.wikipedia.org/wiki/Spherical_coordinate_system
 *
 */
case class SphericalVector[N](
  ρ: UnittedQuantity[Distance, N], // radius
  θ: UnittedQuantity[Angle, N], // 90 - latitude N, aka "co-latitude"
  φ: UnittedQuantity[Angle, N] // longitude E
) {

  type P = (UnittedQuantity[Distance, N], UnittedQuantity[Distance, N], UnittedQuantity[Distance, N])

  /**
   *
   * toPosition could also use a Position3DSpace[N, P] ?
   */

  def toPosition(
    implicit
    ac:   AngleConverter[N],
    mult: MultiplicativeMonoid[N],
    eqn:  Eq[N],
    trig: Trig[N],
    modn: CModule[UnittedQuantity[Distance, N], N]): P =
    (
      ρ :* (sine(θ) * cosine(φ)),
      ρ :* (sine(θ) * sine(φ)),
      ρ :* cosine(θ))

  // TODO could also define a toCertesianVector method that would just call toPosition for the
  // "to" half, and use a "zero" position for the "x"

}

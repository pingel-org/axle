package axle.algebra

import axle.quanta.Angle
import axle.quanta.AngleConverter
import axle.quanta.Distance
import axle.quanta.UnittedQuantity
import axle.quanta._
import axle.cosine
import axle.sine
import spire.implicits._
import spire.algebra.Eq
import spire.algebra.Field
import spire.algebra.Module
import spire.algebra.MultiplicativeMonoid
import spire.algebra.Order
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

  def toPosition(
    implicit ac: AngleConverter[N],
    mmn: MultiplicativeMonoid[N],
    eqn: Eq[N],
    cfn: ConvertableFrom[N],
    modn: Module[UnittedQuantity[Distance, N], Double]): Position3D[Distance, Distance, Distance, N] =
    Position3D(
      ρ :* (sine(θ) * cosine(φ)),
      ρ :* (sine(θ) * sine(φ)),
      ρ :* cosine(θ))
}

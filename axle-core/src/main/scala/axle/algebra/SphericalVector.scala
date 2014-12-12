package axle.algebra

import axle.quanta.Angle
import axle.quanta.Distance
import axle.quanta.UnittedQuantity
import spire.algebra.Field
import spire.algebra.Order

/**
 *
 * Uses the physics conventions instead of mathematical conventions.
 *
 * http://en.wikipedia.org/wiki/Spherical_coordinate_system
 *
 */
case class SphericalVector[N: Field: Order](
  ρ: UnittedQuantity[Distance.type, N], // radius
  θ: UnittedQuantity[Angle.type, N], // 90 - latitude N, aka "co-latitude"
  φ: UnittedQuantity[Angle.type, N] // longitude E
  )

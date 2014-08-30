package axle.visualize.gl

import axle.quanta2.Angle
import axle.quanta2.Distance
import axle.quanta2.Quantity
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
  ρ: Quantity[Distance, N], // radius
  θ: Quantity[Angle, N], // 90 - latitude N, aka "co-latitude"
  φ: Quantity[Angle, N] // longitude E
  )

package axle.visualize.gl

import axle.quanta2.Angle
import axle.quanta2.Distance
import axle.quanta2.UnittedQuantity
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
  ρ: UnittedQuantity[Distance, N], // radius
  θ: UnittedQuantity[Angle, N], // 90 - latitude N, aka "co-latitude"
  φ: UnittedQuantity[Angle, N] // longitude E
  )

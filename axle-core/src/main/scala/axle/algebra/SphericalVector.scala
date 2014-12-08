package axle.algebra

import axle.quanta.Angle3
import axle.quanta.Distance3
import axle.quanta.UnittedQuantity3
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
  ρ: UnittedQuantity3[Distance3, N], // radius
  θ: UnittedQuantity3[Angle3, N], // 90 - latitude N, aka "co-latitude"
  φ: UnittedQuantity3[Angle3, N] // longitude E
  )

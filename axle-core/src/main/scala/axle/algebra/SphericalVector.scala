package axle.algebra

import axle.quanta.Angle
import axle.quanta.Distance
import axle.quanta.UnittedQuantity4
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
  ρ: UnittedQuantity4[Distance[N], N], // radius
  θ: UnittedQuantity4[Angle[N], N], // 90 - latitude N, aka "co-latitude"
  φ: UnittedQuantity4[Angle[N], N] // longitude E
  )

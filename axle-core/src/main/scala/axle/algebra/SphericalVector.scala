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
case class SphericalVector[N: Field: Order, DG[_, _]: DirectedGraph](
  ρ: UnittedQuantity[Distance[DG], N], // radius
  θ: UnittedQuantity[Angle[DG], N], // 90 - latitude N, aka "co-latitude"
  φ: UnittedQuantity[Angle[DG], N] // longitude E
  )

package axle.visualize.gl

import axle.quanta.Angle
import axle.quanta.Distance

/**
 *
 * Uses the physics conventions instead of mathematical conventions.
 *
 * http://en.wikipedia.org/wiki/Spherical_coordinate_system
 *
 */
case class SphericalVector(
  ρ: Distance.Q, // radius
  θ: Angle.Q, // 90 - latitude N, aka "co-latitude"
  φ: Angle.Q // longitude E
  )

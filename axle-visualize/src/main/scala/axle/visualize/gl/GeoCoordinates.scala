package axle.visualize.gl

import axle.quanta._

case class GeoCoordinates(
  latitude: Angle.Q,
  longitude: Angle.Q) {

  def φ: Angle.Q = latitude
  def λ: Angle.Q = longitude
}

package axle.visualize.gl

import spire.algebra.Field
import spire.algebra.Order
import axle.quanta2.Quantity
import axle.quanta2.Angle

case class GeoCoordinates[N: Field: Order](latitude: Quantity[Angle, N], longitude: Quantity[Angle, N]) {

  def φ: Quantity[Angle, N] = latitude
  
  def λ: Quantity[Angle, N] = longitude
}

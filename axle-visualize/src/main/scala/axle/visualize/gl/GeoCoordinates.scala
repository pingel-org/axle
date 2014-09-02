package axle.visualize.gl

import spire.algebra.Field
import spire.algebra.Order
import axle.quanta2.UnittedQuantity
import axle.quanta2.Angle

case class GeoCoordinates[N: Field: Order](latitude: UnittedQuantity[Angle, N], longitude: UnittedQuantity[Angle, N]) {

  def φ: UnittedQuantity[Angle, N] = latitude
  
  def λ: UnittedQuantity[Angle, N] = longitude
}

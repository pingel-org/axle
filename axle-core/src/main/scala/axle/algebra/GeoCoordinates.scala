package axle.algebra

import spire.algebra.Field
import spire.algebra.Order
import axle.quanta.UnittedQuantity3
import axle.quanta.Angle3

case class GeoCoordinates[N: Field: Order](
  latitude: UnittedQuantity3[Angle3, N],
  longitude: UnittedQuantity3[Angle3, N]) {

  def φ: UnittedQuantity3[Angle3, N] = latitude

  def λ: UnittedQuantity3[Angle3, N] = longitude
}

package axle.algebra

import spire.algebra.Field
import spire.algebra.Order
import axle.quanta.UnittedQuantity
import axle.quanta.Angle3

case class GeoCoordinates[N: Field: Order](
  latitude: UnittedQuantity[Angle3, N],
  longitude: UnittedQuantity[Angle3, N]) {

  def φ: UnittedQuantity[Angle3, N] = latitude

  def λ: UnittedQuantity[Angle3, N] = longitude
}

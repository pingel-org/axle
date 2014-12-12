package axle.algebra

import spire.algebra.Field
import spire.algebra.Order
import axle.quanta.UnittedQuantity
import axle.quanta.Angle

case class GeoCoordinates[N: Field: Order](
  latitude: UnittedQuantity[Angle.type, N],
  longitude: UnittedQuantity[Angle.type, N]) {

  def φ: UnittedQuantity[Angle.type, N] = latitude

  def λ: UnittedQuantity[Angle.type, N] = longitude
}

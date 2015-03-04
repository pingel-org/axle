package axle.algebra

import spire.algebra.Field
import spire.algebra.Order
import axle.quanta.UnittedQuantity4
import axle.quanta.Angle

case class GeoCoordinates[N: Field: Order](
  latitude: UnittedQuantity4[Angle[N], N],
  longitude: UnittedQuantity4[Angle[N], N]) {

  def φ: UnittedQuantity4[Angle[N], N] = latitude

  def λ: UnittedQuantity4[Angle[N], N] = longitude
}

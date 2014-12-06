package axle.algebra

import spire.algebra.Field
import spire.algebra.Order
import axle.quanta.UnittedQuantity
import axle.quanta.Angle

case class GeoCoordinates[N: Field: Order, DG[_, _]: DirectedGraph](
  latitude: UnittedQuantity[Angle[DG], N],
  longitude: UnittedQuantity[Angle[DG], N]) {

  def φ: UnittedQuantity[Angle[DG], N] = latitude

  def λ: UnittedQuantity[Angle[DG], N] = longitude
}

package axle.algebra

import axle.quanta.Distance
import axle.quanta.UnittedQuantity
import spire.algebra.Field
import spire.algebra.Order

case class Position[N: Field: Order, DG[_, _]: DirectedGraph](
  x: UnittedQuantity[Distance[DG], N],
  y: UnittedQuantity[Distance[DG], N],
  z: UnittedQuantity[Distance[DG], N])

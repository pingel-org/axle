package axle.algebra

import axle.quanta.UnittedQuantity3
import axle.quanta.Distance3
import spire.algebra.Field
import spire.algebra.Order

case class Position[N: Field: Order](
  x: UnittedQuantity3[Distance3, N],
  y: UnittedQuantity3[Distance3, N],
  z: UnittedQuantity3[Distance3, N])

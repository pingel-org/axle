package axle.algebra

import axle.quanta.UnittedQuantity
import axle.quanta.Distance3
import spire.algebra.Field
import spire.algebra.Order

case class Position[N: Field: Order](
  x: UnittedQuantity[Distance3, N],
  y: UnittedQuantity[Distance3, N],
  z: UnittedQuantity[Distance3, N])

package axle.algebra

import axle.quanta.UnittedQuantity
import axle.quanta.Distance
import spire.algebra.Field
import spire.algebra.Order

case class Position[N: Field: Order](
  x: UnittedQuantity[Distance.type, N],
  y: UnittedQuantity[Distance.type, N],
  z: UnittedQuantity[Distance.type, N])

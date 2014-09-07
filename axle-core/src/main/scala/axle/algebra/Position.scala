package axle.algebra

import axle.quanta.Distance
import axle.quanta.UnittedQuantity
import spire.algebra.Field
import spire.algebra.Order

case class Position[N: Field: Order](x: UnittedQuantity[Distance, N], y: UnittedQuantity[Distance, N], z: UnittedQuantity[Distance, N])

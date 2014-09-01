package axle.visualize.gl

import axle.quanta2.Distance
import axle.quanta2.UnittedQuantity
import spire.algebra.Field
import spire.algebra.Order

case class Position[N: Field: Order](x: UnittedQuantity[Distance, N], y: UnittedQuantity[Distance, N], z: UnittedQuantity[Distance, N])

package axle.visualize.gl

import axle.quanta2.Distance
import axle.quanta2.Quantity
import spire.algebra.Field
import spire.algebra.Order

case class Position[N: Field: Order](x: Quantity[Distance, N], y: Quantity[Distance, N], z: Quantity[Distance, N])

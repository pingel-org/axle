package axle.algebra

import axle.quanta.UnittedQuantity
import axle.quanta.Distance

case class Position[N](
  x: UnittedQuantity[Distance[N], N],
  y: UnittedQuantity[Distance[N], N],
  z: UnittedQuantity[Distance[N], N])

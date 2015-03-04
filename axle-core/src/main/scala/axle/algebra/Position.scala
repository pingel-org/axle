package axle.algebra

import axle.quanta.UnittedQuantity4
import axle.quanta.Distance

case class Position[N](
  x: UnittedQuantity4[Distance[N], N],
  y: UnittedQuantity4[Distance[N], N],
  z: UnittedQuantity4[Distance[N], N])

package axle.algebra

import axle.quanta.UnittedQuantity
import axle.quanta.Distance

trait Position3D[X, Y, Z, P] {

  def x(p: P): X

  def y(p: P): Y

  def z(p: P): Z
}

trait Position3DSpace[N, P]
  extends Position3D[UnittedQuantity[Distance, N], UnittedQuantity[Distance, N], UnittedQuantity[Distance, N], P]

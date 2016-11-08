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

object Position3DSpace {

  type C[T] = (UnittedQuantity[Distance, T], UnittedQuantity[Distance, T], UnittedQuantity[Distance, T])

  implicit def tripleDistance[N]: Position3DSpace[N, C[N]] = new Position3DSpace[N, C[N]] {

    def x(p: C[N]): UnittedQuantity[Distance, N] = p._1

    def y(p: C[N]): UnittedQuantity[Distance, N] = p._2

    def z(p: C[N]): UnittedQuantity[Distance, N] = p._3
  }

}

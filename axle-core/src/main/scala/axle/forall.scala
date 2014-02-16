package axle

import scala.collection.GenTraversable

object forall {

  def apply[T](gt: GenTraversable[T])(predicate: T => Boolean): Boolean = gt.forall(predicate)

}
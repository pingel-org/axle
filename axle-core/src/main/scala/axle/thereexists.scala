package axle

import scala.collection.GenTraversable

object thereexists {

  def apply[T](gt: GenTraversable[T])(predicate: T => Boolean): Boolean = gt.exists(predicate)
}

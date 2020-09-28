package axle

import scala.reflect.ClassTag

case class EnrichedArray[T: ClassTag](arr: Array[T]) {

  def swap(i0: Int, i1: Int): Array[T] = {
    val result = arr.clone
    result(i0) = arr(i1)
    result(i1) = arr(i0)
    result
  }

}

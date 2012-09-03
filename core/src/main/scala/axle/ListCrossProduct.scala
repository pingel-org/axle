package axle

import collection._

class ListCrossProduct[E](lists: Seq[List[E]]) extends CrossProduct[E](lists) {

  val modulos = new Array[Int](lists.size + 1)
  modulos(lists.size) = 1
  for (j <- (lists.size - 1).to(0, -1)) {
    modulos(j) = modulos(j + 1) * lists(j).size
  }

  def indexOf(objects: List[E]): Int = {
    if (objects.size != lists.size) {
      throw new Exception("ListCrossProduct: objects.size() != lists.size()")
    }
    var i = 0
    for (j <- 0 until lists.size) {
      val z = lists(j).indexOf(objects(j))
      if (z == -1) {
        return -1
      }
      i += z * modulos(j + 1)
    }
    i
  }

  def apply(i: Int) = {
    var c = i
    val result = mutable.ArrayBuffer[E]()
    for (j <- 0 until lists.size) {
      result.append(lists(j)(c / modulos(j + 1)))
      c = c % modulos(j + 1)
    }
    result.toList
  }

  override def size() = modulos(0)

}

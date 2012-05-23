package axle.matrix

object ArrayMatrixFactory extends ArrayMatrixFactory {}

/**
 *
 * Discussion of ClassManifest here:
 *
 * http://www.scala-lang.org/docu/files/collections-api/collections_38.html
 *
 */

abstract class ArrayMatrixFactory extends MatrixFactory {

  type M[T] = ArrayMatrix[T]

  class ArrayMatrixImpl[T: ClassManifest](storage: Array[T], nRows: Int, nColumns: Int) extends ArrayMatrix[T] {

    def getStorage = storage

    def rows = nRows

    def columns = nColumns

    def apply(i: Int, j: Int): T = storage(i * nRows + j)

    def update(i: Int, j: Int, v: T) = getStorage(i * nRows + j) = v

    def toList(): List[T] = 0.until(storage.length).map(storage(_)).toList

    def column(c: Int) = {
      val vs = new Array[T](length)
      matrix(vs, nRows, 1)
    }

    def row(r: Int) = {
      val vs = new Array[T](length)
      matrix(vs, 1, nColumns)
    }

  }

  trait ArrayMatrix[T] extends Matrix[T] {

    type S = Array[T]

    def toList(): List[T]

  }

  def matrix[T: ClassManifest](arr: Array[T], r: Int, c: Int): ArrayMatrix[T] = new ArrayMatrixImpl(arr, r, c)

  def matrix[T: ClassManifest](r: Int, c: Int, default: T): ArrayMatrix[T] = {
    val length = r * c
    val arr = new Array[T](length)
    0.until(length).map(i => arr(i) = default)
    matrix(arr, r, c)
  }

}

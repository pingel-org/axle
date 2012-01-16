
package org.pingel.axle.matrix {

  import scala.collection._

  object Matrix {

    def sparse[T](numRows: Int, numCols: Int): Matrix[T] = {
      null // TODO
    }

    def dense[T](numRows: Int, numCols: Int): Matrix[T] = {
      null // TODO
    }

    def zeros[T](numRows: Int, numCols: Int): Matrix[T] = {
      null // TODO
    }

  }

  trait Matrix[T] {
    def numRows: Int
    def numCols: Int
    def valueAt(r: Int, c: Int): T
    def setValueAt(r: Int, c: Int, v: T): Unit = {} // TODO
  }

//  class DenseMatrix[T](nr: Int, nc: Int) extends Matrix[T] {
//    def numRows = nr
//    def numCols = nc
//    // TODO: initialize
//  }

}


// http://jblas.org/javadoc/index.html
// http://jblas.org/javadoc/index.html

package org.pingel.axle.matrix {

  import scala.collection._

  object DoubleMatrix {

    def zeros(m: Int, n: Int): Matrix[Double] = new DoubleJblasMatrix(org.jblas.DoubleMatrix.zeros(m, n))

    def ones(m: Int, n: Int): Matrix[Double] = new DoubleJblasMatrix(org.jblas.DoubleMatrix.ones(m, n))

    // evenly distributed from 0.0 to 1.0
    def rand(m: Int, n: Int): Matrix[Double] = new DoubleJblasMatrix(org.jblas.DoubleMatrix.rand(m, n))

    // normal distribution
    def randn(m: Int, n: Int): Matrix[Double] = new DoubleJblasMatrix(org.jblas.DoubleMatrix.randn(m, n))

    def eye(n: Int): Matrix[Double] = new DoubleJblasMatrix(org.jblas.DoubleMatrix.eye(n))

    def concatHorizontally(left: DoubleJblasMatrix, right: DoubleJblasMatrix) = new DoubleJblasMatrix(org.jblas.DoubleMatrix.concatHorizontally(left.jblas, right.jblas))

    def concatVertically(left: DoubleJblasMatrix, right: DoubleJblasMatrix) = new DoubleJblasMatrix(org.jblas.DoubleMatrix.concatVertically(left.jblas, right.jblas))

  }

  object IntMatrix {
    def zeros(m: Int, n: Int): Matrix[Int] = new IntJblasMatrix(org.jblas.DoubleMatrix.zeros(m, n))

    def ones(m: Int, n: Int): Matrix[Int] = new IntJblasMatrix(org.jblas.DoubleMatrix.ones(m, n))

    def eye(n: Int): Matrix[Int] = new IntJblasMatrix(org.jblas.DoubleMatrix.eye(n))

    def concatHorizontally(left: IntJblasMatrix, right: IntJblasMatrix) = new IntJblasMatrix(org.jblas.DoubleMatrix.concatHorizontally(left.jblas, right.jblas))

    def concatVertically(left: IntJblasMatrix, right: IntJblasMatrix) = new IntJblasMatrix(org.jblas.DoubleMatrix.concatVertically(left.jblas, right.jblas))
  }

  object BooleanMatrix {
    def falses(m: Int, n: Int): Matrix[Boolean] = new BooleanJblasMatrix(org.jblas.DoubleMatrix.zeros(m, n))

    def trues(m: Int, n: Int): Matrix[Boolean] = new BooleanJblasMatrix(org.jblas.DoubleMatrix.ones(m, n))

    def eye(n: Int): Matrix[Boolean] = new BooleanJblasMatrix(org.jblas.DoubleMatrix.eye(n))

    def concatHorizontally(left: BooleanJblasMatrix, right: BooleanJblasMatrix) = new BooleanJblasMatrix(org.jblas.DoubleMatrix.concatHorizontally(left.jblas, right.jblas))

    def concatVertically(left: BooleanJblasMatrix, right: BooleanJblasMatrix) = new BooleanJblasMatrix(org.jblas.DoubleMatrix.concatVertically(left.jblas, right.jblas))
  }

  object SetMatrix {

    def empties[T](m: Int, n: Int): Matrix[Set[T]] = new SetMatrix[T](m, n)

    def concatHorizontally[T](left: SetMatrix[T], right: SetMatrix[T]) = {
      // assert that the # of rows are equal ??
      var result = new SetMatrix[T](left.rows, left.columns + right.columns)
      // TODO
      result
    }

    def concatVertically[T](top: SetMatrix[T], bottom: SetMatrix[T]) = {
      // assert that the # columns are equal ??
      var result = new SetMatrix[T](top.rows + bottom.rows, top.columns)
      // TODO
      result
    }

  }

  trait Matrix[T] {

    def rows: Int

    def columns: Int

    def length: Int

    def valueAt(i: Int, j: Int): T

    def setValueAt(i: Int, j: Int, v: T): Unit

    def getColumn(j: Int): Matrix[T]

    def getRow(i: Int): Matrix[T]
  }

  abstract case class JblasMatrix[T](jblas: org.jblas.DoubleMatrix) extends Matrix[T] {

    def tToDouble(v: T): Double

    def doubleToT(d: Double): T

    def rows(): Int = jblas.rows

    def columns(): Int = jblas.columns

    def length(): Int = jblas.length

    def valueAt(i: Int, j: Int) = doubleToT(jblas.get(i, j))

    def setValueAt(i: Int, j: Int, v: T) = jblas.put(i, j, tToDouble(v))

    override def toString() = jblas.toString()
  }

  class DoubleJblasMatrix(jblas: org.jblas.DoubleMatrix) extends JblasMatrix[Double](jblas) {

    def tToDouble(v: Double) = v

    def doubleToT(d: Double) = d

    def getColumn(j: Int) = new DoubleJblasMatrix(jblas.getColumn(j))

    def getRow(i: Int) = new DoubleJblasMatrix(jblas.getRow(i))
  }

  class IntJblasMatrix(jblas: org.jblas.DoubleMatrix) extends JblasMatrix[Int](jblas) {

    def tToDouble(v: Int) = v

    def doubleToT(d: Double) = d.toInt

    def getColumn(j: Int) = new IntJblasMatrix(jblas.getColumn(j))

    def getRow(i: Int) = new IntJblasMatrix(jblas.getRow(i))
  }

  class BooleanJblasMatrix(jblas: org.jblas.DoubleMatrix) extends JblasMatrix[Boolean](jblas) {

    def doubleToT(d: Double) = d.toInt != 0

    def tToDouble(b: Boolean) = b match {
      case false => 0
      case true => 1
    }

    def getColumn(j: Int) = new BooleanJblasMatrix(jblas.getColumn(j))

    def getRow(i: Int) = new BooleanJblasMatrix(jblas.getRow(i))

  }

  class SetMatrix[U](m: Int, n: Int) extends Matrix[Set[U]] {

    var array = new Array[Set[U]](m * n)
    for (i <- 0 until m; j <- 0 until n) {
      array(i * m + j) = Set[U]()
    }

    def rows() = m

    def columns() = n

    def length() = m * n

    def valueAt(i: Int, j: Int) = array(i * m + j)

    def setValueAt(i: Int, j: Int, v: Set[U]) = {
      array(i * m + j) = v
    }

    def getColumn(j: Int) = {
      var result = new SetMatrix[U](m, 1)
      // TODO: copy column values
      result
    }

    def getRow(i: Int) = {
      var result = new SetMatrix[U](1, n)
      // TODO: copy row values
      result
    }

  }

}

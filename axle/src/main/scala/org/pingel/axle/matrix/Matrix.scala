
// http://jblas.org/javadoc/index.html

package org.pingel.axle.matrix {

  import scala.collection._

  trait Matrix[T, S, MF <: MatrixFactory[T, S, _]] {
    def factory: MF
    def rows: Int
    def columns: Int
    def length: Int
    def valueAt(i: Int, j: Int): T
    def setValueAt(i: Int, j: Int, v: T): Unit
    def getColumn(j: Int): Matrix[T, S, MF]
    def getRow(i: Int): Matrix[T, S, MF]
    def add(other: Matrix[T, S, MF]): Matrix[T, S, MF]
  }

  trait MatrixFactory[T, S, M <: Matrix[T, S, _]] {
    def storage: S
    def pure(s: S): M // Matrix[T, S]
    def zeros(m: Int, n: Int): Matrix[T, S, _]
    def concatHorizontally(left: M, right: M): M
    def concatVertically(left: M, right: M): M
  }

  abstract case class JblasMatrix[T](jblas: org.jblas.DoubleMatrix)
    extends Matrix[T, org.jblas.DoubleMatrix, JblasMatrixFactory[_, _] ] {

    def factory: JblasMatrixFactory[T, JblasMatrix[T]]

    def storage = jblas

    def tToDouble(v: T): Double

    def doubleToT(d: Double): T

    def rows(): Int = jblas.rows

    def columns(): Int = jblas.columns

    def length(): Int = jblas.length

    def valueAt(i: Int, j: Int) = doubleToT(jblas.get(i, j))

    def setValueAt(i: Int, j: Int, v: T) = jblas.put(i, j, tToDouble(v))

    def getColumn(j: Int) = factory.pure(jblas.getColumn(j))

    def getRow(i: Int) = factory.pure(jblas.getRow(i))

    // def add(other: JblasMatrix[T]) = factory.pure(storage().add(other.storage))

    override def toString() = jblas.toString()
  }

  trait JblasMatrixFactory[T, M <: JblasMatrix[T]] extends MatrixFactory[T, org.jblas.DoubleMatrix, M] {

    def zeros(m: Int, n: Int) = pure(org.jblas.DoubleMatrix.zeros(m, n))

    def ones(m: Int, n: Int) = pure(org.jblas.DoubleMatrix.ones(m, n))

    def eye(n: Int) = pure(org.jblas.DoubleMatrix.eye(n))

    def concatHorizontally(left: M, right: M) = pure(org.jblas.DoubleMatrix.concatHorizontally(left.jblas, right.jblas))

    def concatVertically(left: M, right: M) = pure(org.jblas.DoubleMatrix.concatVertically(left.jblas, right.jblas))
  }

  object DoubleMatrixFactory extends JblasMatrixFactory[Double, DoubleJblasMatrix] {
    def pure(jblas: org.jblas.DoubleMatrix) = new DoubleJblasMatrix(jblas)
    // evenly distributed from 0.0 to 1.0
    def rand(m: Int, n: Int) = pure(org.jblas.DoubleMatrix.rand(m, n))
    // normal distribution
    def randn(m: Int, n: Int) = pure(org.jblas.DoubleMatrix.randn(m, n))
  }

  object IntMatrixFactory extends JblasMatrixFactory[Int, IntJblasMatrix] {
    def pure(jblas: org.jblas.DoubleMatrix) = new IntJblasMatrix(jblas)
  }

  object BooleanMatrixFactory extends JblasMatrixFactory[Boolean, BooleanJblasMatrix] {
    def pure(jblas: org.jblas.DoubleMatrix) = new BooleanJblasMatrix(jblas)
    def falses(m: Int, n: Int) = zeros(m, n)
    def trues(m: Int, n: Int) = ones(m, n)
  }

  class SetMatrixFactory[T] extends MatrixFactory[T, Tuple3[Int, Int, Array[Set[T]]], SetMatrix[T]] {

    def pure[T](m: Int, n: Int) = {
      var array = new Array[Set[T]](m * n)
      for (i <- 0 until m; j <- 0 until n) {
        array(i * m + j) = Set[T]()
      }
      new SetMatrix[T](m, n, array)
    }

    def zeros[T](m: Int, n: Int) = pure[T](m, n)

    def concatHorizontally[T](left: SetMatrix[T], right: SetMatrix[T]) = {
      // assert that the # of rows are equal ??
      var result = pure[T](left.rows, left.columns + right.columns)
      // TODO
      result
    }

    def concatVertically[T](top: SetMatrix[T], bottom: SetMatrix[T]) = {
      // assert that the # columns are equal ??
      var result = pure[T](top.rows + bottom.rows, top.columns)
      // TODO
      result
    }

  }

  class DoubleJblasMatrix(jblas: org.jblas.DoubleMatrix) extends JblasMatrix[Double](jblas) {

    def factory(): JblasMatrixFactory[Double, DoubleJblasMatrix] = DoubleMatrixFactory

    def tToDouble(v: Double) = v

    def doubleToT(d: Double) = d
  }

  class IntJblasMatrix(jblas: org.jblas.DoubleMatrix) extends JblasMatrix[Int](jblas) {

    def factory(): JblasMatrixFactory[Int, IntJblasMatrix] = IntMatrixFactory

    def tToDouble(v: Int) = v

    def doubleToT(d: Double) = d.toInt

    def getColumn(j: Int) = factory.pure(jblas.getColumn(j))

    def getRow(i: Int) = factory.pure(jblas.getRow(i))
  }

  class BooleanJblasMatrix(jblas: org.jblas.DoubleMatrix) extends JblasMatrix[Boolean](jblas) {

    def factory(): JblasMatrixFactory[Boolean, BooleanJblasMatrix] = BooleanMatrixFactory

    def doubleToT(d: Double) = d.toInt != 0

    def tToDouble(b: Boolean) = b match {
      case false => 0
      case true => 1
    }

    def getColumn(j: Int) = factory.pure(jblas.getColumn(j))

    def getRow(i: Int) = factory.pure(jblas.getRow(i))

  }

  class SetMatrix[U](m: Int, n: Int, var array: Array[Set[U]])
  extends Matrix[Set[U], Tuple3[Int, Int, Array[Set[U]]], SetMatrixFactory[U]] {

    def factory() = new SetMatrixFactory[U]() // TODO inefficient?

    def storage = (m, n, array)

    def rows() = m

    def columns() = n

    def length() = m * n

    def valueAt(i: Int, j: Int) = array(i * m + j)

    def setValueAt(i: Int, j: Int, v: Set[U]) = {
      array(i * m + j) = v
    }

    def getColumn(j: Int) = {
      var cArray = new Array[Set[U]](m)
      var result = factory.pure((m, 1, cArray))
      // TODO: copy column values
      result
    }

    def getRow(i: Int) = {
      var rArray = new Array[Set[U]](n)
      var result = factory.pure((1, n, rArray))
      // TODO: copy row values
      result
    }

  }

}

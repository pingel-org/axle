
// http://jblas.org/javadoc/index.html

package org.pingel.axle.matrix {

  import scala.collection._

  trait Matrix[T] {
    
    def rows: Int

    def columns: Int

    def length: Int

    def valueAt(i: Int, j: Int): T

    def setValueAt(i: Int, j: Int, v: T): Unit

    def getColumn(j: Int): Matrix[T]

    def getRow(i: Int): Matrix[T]

    def add(other: Matrix[T]): Matrix[T]
  }

  trait BackedMatrix[T, S] extends Matrix[T] {

    def storage: S

    def getColumn(j: Int): BackedMatrix[T, S]

    def getRow(i: Int): BackedMatrix[T, S]
    
    def add(other: BackedMatrix[T, S]): BackedMatrix[T, S]
    
  }

  trait BackedMatrixFactory[T, S, BM <: BackedMatrix[T, S]] {
    
    def pure(s: S): Matrix[T]

    def zeros(m: Int, n: Int): Matrix[T]

    def concatHorizontally(left: BM, right: BM): BM
    
    def concatVertically(left: BM, right: BM): BM
  }
  
  abstract case class JblasMatrix[T](jblas: org.jblas.DoubleMatrix)
  extends BackedMatrix[T, org.jblas.DoubleMatrix] {

    def storage = jblas
    
    def tToDouble(v: T): Double

    def doubleToT(d: Double): T

    def rows(): Int = jblas.rows

    def columns(): Int = jblas.columns

    def length(): Int = jblas.length

    def valueAt(i: Int, j: Int) = doubleToT(jblas.get(i, j))

    def setValueAt(i: Int, j: Int, v: T) = jblas.put(i, j, tToDouble(v))

    def add(other: JblasMatrix[T]) = implicitly[JblasMatrixFactory[T]].pure(storage().add(other.storage))
    
    override def toString() = jblas.toString()
  }

  trait JblasMatrixFactory[T, M <: JblasMatrix[T]] extends BackedMatrixFactory[T, org.jblas.DoubleMatrix, M] {
    
    def zeros(m: Int, n: Int): Matrix[T] = pure(org.jblas.DoubleMatrix.zeros(m, n))

    def ones(m: Int, n: Int): Matrix[T] = pure(org.jblas.DoubleMatrix.ones(m, n))
    
    def eye(n: Int): Matrix[T] = pure(org.jblas.DoubleMatrix.eye(n))

    def concatHorizontally(left: M, right: M): Matrix[T] = pure(org.jblas.DoubleMatrix.concatHorizontally(left.jblas, right.jblas))

    def concatVertically(left: M, right: M): Matrix[T] = pure(org.jblas.DoubleMatrix.concatVertically(left.jblas, right.jblas))
  }
  
  object DoubleMatrix extends JblasMatrixFactory[Double, DoubleJblasMatrix] {
    def pure(jblas: org.jblas.DoubleMatrix) = new DoubleJblasMatrix(jblas)
    // evenly distributed from 0.0 to 1.0
    def rand(m: Int, n: Int): Matrix[Double] = pure(org.jblas.DoubleMatrix.rand(m, n))
    // normal distribution
    def randn(m: Int, n: Int): Matrix[Double] = pure(org.jblas.DoubleMatrix.randn(m, n))
  }

  object IntMatrix extends JblasMatrixFactory[Int, IntJblasMatrix] {
    def pure(jblas: org.jblas.DoubleMatrix) = new IntJblasMatrix(jblas)
  }

  object BooleanMatrix extends JblasMatrixFactory[Boolean, BooleanJblasMatrix] {
    def pure(jblas: org.jblas.DoubleMatrix) = new BooleanJblasMatrix(jblas)
    def falses(m: Int, n: Int) = zeros(m, n)
    def trues(m: Int, n: Int) = ones(m, n)
  }

  class DoubleJblasMatrix(jblas: org.jblas.DoubleMatrix) extends JblasMatrix[Double](jblas) {

    def tToDouble(v: Double) = v

    def doubleToT(d: Double) = d

    def getColumn(j: Int) = DoubleMatrix.pure(jblas.getColumn(j))

    def getRow(i: Int) = DoubleMatrix.pure(jblas.getRow(i))
  }

  class IntJblasMatrix(jblas: org.jblas.DoubleMatrix) extends JblasMatrix[Int](jblas) {

    def tToDouble(v: Int) = v

    def doubleToT(d: Double) = d.toInt

    def getColumn(j: Int) = IntMatrix.pure(jblas.getColumn(j))

    def getRow(i: Int) = IntMatrix.pure(jblas.getRow(i))
  }

  class BooleanJblasMatrix(jblas: org.jblas.DoubleMatrix) extends JblasMatrix[Boolean](jblas) {

    def doubleToT(d: Double) = d.toInt != 0

    def tToDouble(b: Boolean) = b match {
      case false => 0
      case true => 1
    }

    def getColumn(j: Int) = BooleanMatrix.pure(jblas.getColumn(j))

    def getRow(i: Int) = BooleanMatrix.pure(jblas.getRow(i))

  }

  class SetMatrixFactory[T] extends BackedMatrixFactory[T, Tuple3[Int, Int, Array[Set[T]]], SetMatrix[T]] {

    def pure[T](m: Int, n: Int) = {
      var array = new Array[Set[T]](m * n)
      for (i <- 0 until m; j <- 0 until n) {
        array(i * m + j) = Set[T]()
      }
      new SetMatrix[T](m, n, array)
    }

    def zeros[T](m: Int, n: Int): Matrix[Set[T]] = pure[T](m, n)

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
  
  class SetMatrix[U](m: Int, n: Int, var array: Array[Set[U]]) extends BackedMatrix[Set[U], Tuple3[Int, Int, Array[Set[U]]]] {

    def storage = (m, n, array)
    
    def rows() = m

    def columns() = n

    def length() = m * n

    def valueAt(i: Int, j: Int) = array(i * m + j)

    def setValueAt(i: Int, j: Int, v: Set[U]) = {
      array(i * m + j) = v
    }

    def getColumn(j: Int) = {
      var result = SetMatrix.pure[U](m, 1)
      // TODO: copy column values
      result
    }

    def getRow(i: Int) = {
      var result = SetMatrix.pure[U](1, n)
      // TODO: copy row values
      result
    }

  }

}

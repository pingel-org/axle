
package org.pingel.axle.matrix {

  abstract class MatrixFactory {

    type S

    type M[T] <: Matrix[T]

    trait Matrix[T] {
      def rows: Int
      def columns: Int
      def length: Int
      def valueAt(i: Int, j: Int): T
      def setValueAt(i: Int, j: Int, v: T): Unit
      def getColumn(j: Int): M[T]
      def getRow(i: Int): M[T]
      def add(other: M[T]): M[T]
      def +|+(right: M[T]): M[T]
      def +/+(under: M[T]): M[T]
    }

    protected def pure[T](s: S): M[T]
  }

  object JblasMatrixFactory extends JblasMatrixFactoryClass()

  class JblasMatrixFactoryClass extends MatrixFactory {

    type M[T] = JblasMatrixImpl[T]

    type S = org.jblas.DoubleMatrix

    class JblasMatrixImpl[T](jblas: org.jblas.DoubleMatrix) extends Matrix[T] {

      def rows() = jblas.rows

      def columns() = jblas.columns

      def length() = jblas.length

      def tToDouble[T](t: T): Double = t match {
        case d: Double => d
        case i: Int => i
        case b: Boolean => b match {
          case true => 1.0
          case false => 0.0
        }
      }

      def valueAt(i: Int, j: Int): T = null.asInstanceOf[T] // TODO doubleToT(jblas.get(i, j))

      def setValueAt(i: Int, j: Int, v: T) = jblas.put(i, j, tToDouble(v))

      def getColumn(j: Int) = pure(jblas.getColumn(j))

      def getRow(i: Int) = pure(jblas.getRow(i))

      def +|+(right: M[T]) = pure(org.jblas.DoubleMatrix.concatHorizontally(this.jblas, right.getJblas))

      def +/+(under: M[T]) = pure(org.jblas.DoubleMatrix.concatVertically(this.jblas, under.getJblas))

      def add(other: M[T]) = pure(jblas.add(other.getJblas()))

      def +(other: M[T]) = this.add(other)

      def sub(other: M[T]) = pure(jblas.sub(other.getJblas))

      def -(other: M[T]) = this.sub(other)

      def mul(other: M[T]) = pure(jblas.mul(other.getJblas))

      def mmul(other: M[T]) = pure(jblas.mmul(other.getJblas))

      // dot?

      def div(other: M[T]) = pure(jblas.div(other.getJblas))

      def neg() = pure(jblas.neg())

      override def toString() = getJblas.toString()

      def getJblas() = jblas
    }

    def zeros[T](m: Int, n: Int) = pure[T](org.jblas.DoubleMatrix.zeros(m, n))

    def ones[T](m: Int, n: Int) = pure[T](org.jblas.DoubleMatrix.ones(m, n))

    def eye[T](n: Int) = pure[T](org.jblas.DoubleMatrix.eye(n))

    // evenly distributed from 0.0 to 1.0
    def rand[T](m: Int, n: Int) = pure[T](org.jblas.DoubleMatrix.rand(m, n))

    // normal distribution
    def randn[T](m: Int, n: Int) = pure[T](org.jblas.DoubleMatrix.randn(m, n))

    def falses(m: Int, n: Int) = pure[Boolean](org.jblas.DoubleMatrix.zeros(m, n))

    def trues(m: Int, n: Int) = pure[Boolean](org.jblas.DoubleMatrix.ones(m, n))

    def pure[T](jblas: org.jblas.DoubleMatrix): M[T] = new JblasMatrixImpl[T](jblas)

  }

//  class ArrayMatrixFactory extends MatrixFactory {
//
//    type M[T] = ArrayMatrixImpl
//
//    type S = (Int, Int, Array[T])
//
//    class ArrayMatrixImpl(triple: (Int, Int, Array[T])) extends Matrix[T] {
//
//      def add(other: M[T]) = pure(triple) // TODO
//
//      def getTriple() = triple
//
//    }
//
//    def pure(double: (Int, Int)): M[T] = {
//      var array = new Array[Set[T]](m * n)
//      for (i <- 0 until m; j <- 0 until n) {
//        array(i * m + j) = Set[T]()
//      }
//      new ArrayMatrixImpl(triple)
//    }
//  }

}


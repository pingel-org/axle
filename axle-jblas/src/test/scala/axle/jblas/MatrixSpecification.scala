package axle.jblas

import org.specs2.mutable.Specification
import spire.implicits._
import axle.syntax.LinearAlgebraOps
import axle.syntax.linearalgebra.matrixOps
import axle.syntax.endofunctor.endofunctorOps

class MatrixSpecification extends Specification {

  implicit val endo = endoFunctorDoubleMatrix[Double]
  implicit val la = linearAlgebraDoubleMatrix[Double]
  import la._

  "Linear Algebra for org.jblas.DoubleMatrix" should {
    "create simple matrices" in {

      val z = zeros(3, 4)
      val o = ones(2, 3)
      val x = rand(1, 2)

      val y = rand(3, 3)
      val c = y.column(2)
      val r = y.row(2)

      z.rows must be equalTo 3
      z.columns must be equalTo 4
    }
  }

  //  "eye and diag" should {
  //    "sum of diag elements in eye(4) is 4" in {
  //      // TODO eye(4).diag.rowSums.get(1, 1) must be equalTo 4d
  //      // TODO eye(4).rows must be equalTo 4
  //    }
  //  }

  "x+x === x.map(_*2)" should {
    "hold for random 2x2 matrix x" in {

      val x = randn(2, 2)

      // implicitly[Ring[DoubleMatrix]].plus
      (x + x) must be equalTo x.map(_ * 2d)
    }
  }

  "(n atop m) === (n.t aside m.t).t" should {
    "hold for random 2x3 matrices m and n" in {

      val r = 2
      val c = 3

      val m = rand(r, c)
      val n = rand(r, c)

      (n atop m) must be equalTo ((n.t aside m.t).t)
    }
  }

  "boolean tests" should {
    "return false for random 2x3 matrix" in {

      // mask raw matrix to ensure Axle's methods are being tested
      val m = new LinearAlgebraOps(rand(2, 3))

      m.isEmpty must be equalTo false
      m.isColumnVector must be equalTo false
      m.isRowVector must be equalTo false
      m.isVector must be equalTo false
      m.isSquare must be equalTo false
      m.isScalar must be equalTo false
    }
  }

  "boolean comparisons" should {
    "work on 2x2 matrix" in {

      val lhs = new LinearAlgebraOps(matrix(1, 2, Array(0.5, 1.0, 1.5)))

      (lhs lt ones(1, 3)) must be equalTo matrix(1, 3, Array(1d, 0d, 0d))
      (lhs le ones(1, 3)) must be equalTo matrix(1, 3, Array(1d, 1d, 0d))
      (lhs gt ones(1, 3)) must be equalTo matrix(1, 3, Array(0d, 0d, 1d))
      (lhs ge ones(1, 3)) must be equalTo matrix(1, 3, Array(0d, 1d, 1d))
      (lhs eq ones(1, 3)) must be equalTo matrix(1, 3, Array(0d, 1d, 0d))
      (lhs ne ones(1, 3)) must be equalTo matrix(1, 3, Array(1d, 0d, 1d))
    }
  }

  "boolean operators" should {
    "work on 2x2 matrix" in {

      val tf = matrix(1, 2, Array(1d, 0d))
      val ft = matrix(1, 2, Array(0d, 1d))
      val tfops = new LinearAlgebraOps(tf)

      (tfops and ft) must be equalTo matrix(1, 2, Array(0d, 0d))
      (tfops or ft) must be equalTo matrix(1, 2, Array(1d, 1d))
      (tfops xor ft) must be equalTo matrix(1, 2, Array(1d, 1d))
      (tfops not) must be equalTo ft
    }
  }

  //  "mul row and column" should {
  //    "" in {
  //      // def mulRow(m: DoubleMatrix)(i: Int, x: N): DoubleMatrix = m.mulRow(i, x.toDouble)
  //      // def mulColumn
  //
  //      1 must be equalTo 1
  //    }
  //  }
  //
  //  "invert" should {
  //    "" in {
  //
  //      1 must be equalTo 1
  //    }
  //  }
  //
  //  "solve" should {
  //    "" in {
  //
  //      1 must be equalTo 1
  //    }
  //  }
  //
  //  "flatMap" should {
  //    "" in {
  //      // def flatMapColumns(m: DoubleMatrix)(f: DoubleMatrix => DoubleMatrix): DoubleMatrix
  //      1 must be equalTo 1
  //    }
  //  }
  //
  //  "folds" should {
  //    "" in {
  //
  //      //      foldLeft(m: DoubleMatrix)(zero: DoubleMatrix)(f: (DoubleMatrix, DoubleMatrix) => DoubleMatrix): DoubleMatrix
  //      //      foldTop(m: DoubleMatrix)(zero: DoubleMatrix)(f: (DoubleMatrix, DoubleMatrix) => DoubleMatrix): DoubleMatrix
  //      1 must be equalTo 1
  //    }
  //  }
  //
  //  "range, min, max, argmax" should {
  //    "" in {
  //      //      rowRange(m: DoubleMatrix): DoubleMatrix
  //      //      columnRange(m: DoubleMatrix): DoubleMatrix
  //      //      def rowMins(m: DoubleMatrix): DoubleMatrix
  //      //      def rowMaxs(m: DoubleMatrix): DoubleMatrix
  //      //      def columnMins(m: DoubleMatrix): DoubleMatrix
  //      //      def columnMaxs(m: DoubleMatrix): DoubleMatrix
  //      //      def max(m: DoubleMatrix): N
  //      //      def argmax(m: DoubleMatrix): (Int, Int)
  //      //      def min(m: DoubleMatrix): N 
  //      //      def argmin(m: DoubleMatrix): (Int, Int)
  //      1 must be equalTo 1
  //    }
  //  }

  "center, mean, sum" should {
    "sum, mean, and center by row and column" in {

      val m = new LinearAlgebraOps(matrix(2, 3,
        Array(1.4, 22d, 17.5, 2.3, 18d, 105d)))

      m.rowSums must be equalTo matrix(2, 1,
        Array(36.900000, 129.300000))

      m.columnSums must be equalTo matrix(1, 3,
        Array(23.400000, 19.800000, 123.000000))

      // 'floor' to workaround rounding error
      m.rowMeans.floor must be equalTo matrix(2, 1,
        Array(12d, 43d))

      m.columnMeans must be equalTo matrix(1, 3,
        Array(11.7, 9.9, 61.5))

      m.centerRows.floor must be equalTo matrix(2, 3,
        Array(-11d, -22d, 5d, -41, 5d, 61d))

      m.centerColumns.floor must be equalTo matrix(2, 3,
        Array(-11d, 10d, 7d, -8d, -44d, 43d))
    }
  }

  "sorts" should {
    "sort columns and rows" in {

      val m = new LinearAlgebraOps(matrix(2, 3,
        Array(1.4, 22d, 17.5, 2.3, 18d, 105d)))

      m.sortRows must be equalTo matrix(2, 3,
        Array(1.4, 2.3, 17.5, 22d, 18d, 105d))

      m.sortColumns must be equalTo matrix(2, 3,
        Array(1.4, 22d, 2.3, 17.5, 18d, 105d))
    }
  }

  "ceil, floor, log, log10, pow" should {
    "transform a 2x3 matrix" in {

      // mask raw matrix to ensure Axle's methods are being tested
      val m = new LinearAlgebraOps(matrix(2, 3,
        Array(1.4, 22d, 17.5, 2.3, 18d, 105d)))

      m.ceil must be equalTo matrix(2, 3,
        Array(2d, 22d, 18d, 3d, 18d, 105d))

      m.floor must be equalTo matrix(2, 3,
        Array(1d, 22d, 17d, 2d, 18d, 105d))

      m.log.floor must be equalTo matrix(2, 3,
        Array(0d, 3d, 2d, 0d, 2d, 4d))

      m.log10.floor must be equalTo matrix(2, 3,
        Array(0d, 1d, 1d, 0d, 1d, 2d))

      m.pow(2d) must be equalTo matrix(2, 3,
        Array(1.9599999999999997, 484d, 306.25, 5.289999999999999, 324d, 11025d))

    }
  }

  "addAssignment" should {
    "addAssignment (1,2) to 6d in a 2x3 matrix, leaving original unmodified" in {

      val m = matrix(2, 3,
        Array(1d, 2d, 3d, 4d, 5d, 0d))

      m.addAssignment(1, 2, 6d) must be equalTo matrix(2, 3,
        Array(1d, 2d, 3d, 4d, 5d, 6d))

      m must be equalTo matrix(2, 3,
        Array(1d, 2d, 3d, 4d, 5d, 0d))
    }
  }

}


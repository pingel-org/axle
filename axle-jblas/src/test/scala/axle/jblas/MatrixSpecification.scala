package axle.jblas

import org.scalatest._

import spire.algebra._
import axle.syntax.LinearAlgebraOps
import axle.syntax.linearalgebra.matrixOps
import axle.syntax.endofunctor.endofunctorOps

class MatrixSpecification extends FunSuite with Matchers {

  implicit val rngDouble: Rng[Double] = spire.implicits.DoubleAlgebra
  implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

  implicit val endo = endoFunctorDoubleMatrix[Double]
  implicit val la = linearAlgebraDoubleMatrix[Double]
  import la._

  test("Linear Algebra for org.jblas.DoubleMatrix create simple matrices") {

    val z = zeros(3, 4)
    ones(2, 3)
    rand(1, 2)

    val y = rand(3, 3)
    y.column(2)
    y.row(2)

    val zops = new LinearAlgebraOps(z)

    zops.rows should be(3)
    zops.columns should be(4)
    zops.length should be(12)
    new LinearAlgebraOps(y).diag.length should be(3)
    zops.dup should be(z)
  }

  //  "eye and diag" should {
  //    "sum of diag elements in eye(4) is 4" in {
  //      // TODO eye(4).diag.rowSums.get(1, 1) should be ( 4d
  //      // TODO eye(4).rows should be ( 4
  //    }
  //  }

  test("x+x === x.map(_*2)") {

    val x = randn(2, 2)

    // implicitly[Ring[DoubleMatrix]].plus
    (x + x) should be(x.map(_ * 2d))
  }

  test("(n atop m) === (n.t aside m.t).t") {

    val r = 2
    val c = 3

    val m = rand(r, c)
    val n = rand(r, c)

    (n atop m) should be((n.t aside m.t).t)
  }

  test("boolean tests") {

    // mask raw matrix to ensure Axle's methods are being tested
    val m = new LinearAlgebraOps(rand(2, 3))

    m.isEmpty should be(false)
    m.isColumnVector should be(false)
    m.isRowVector should be(false)
    m.isVector should be(false)
    m.isSquare should be(false)
    m.isScalar should be(false)
  }

  test("boolean comparisons on 2x2 matrices") {

    val lhs = new LinearAlgebraOps(fromColumnMajorArray(1, 3, Array(0.5, 1.0, 1.5)))

    (lhs lt ones(1, 3)) should be(fromColumnMajorArray(1, 3, Array(1d, 0d, 0d)))
    (lhs le ones(1, 3)) should be(fromColumnMajorArray(1, 3, Array(1d, 1d, 0d)))
    (lhs gt ones(1, 3)) should be(fromColumnMajorArray(1, 3, Array(0d, 0d, 1d)))
    (lhs ge ones(1, 3)) should be(fromColumnMajorArray(1, 3, Array(0d, 1d, 1d)))
    (lhs eq ones(1, 3)) should be(fromColumnMajorArray(1, 3, Array(0d, 1d, 0d)))
    (lhs ne ones(1, 3)) should be(fromColumnMajorArray(1, 3, Array(1d, 0d, 1d)))
  }

  test("boolean operators on 2x2 matrices") {

    val tf = fromColumnMajorArray(1, 2, Array(1d, 0d))
    val ft = fromColumnMajorArray(1, 2, Array(0d, 1d))
    val tfops = new LinearAlgebraOps(tf)

    (tfops and ft) should be(fromColumnMajorArray(1, 2, Array(0d, 0d)))
    (tfops or ft) should be(fromColumnMajorArray(1, 2, Array(1d, 1d)))
    (tfops xor ft) should be(fromColumnMajorArray(1, 2, Array(1d, 1d)))
    (tfops not) should be(ft)
  }

  test("mul row and column on ones(2, 2)") {

    val square = new LinearAlgebraOps(ones(2, 2))

    square.mulRow(0, 3.14) should be(fromColumnMajorArray(2, 2, Array(3.14, 1d, 3.14, 1d)))
    square.mulColumn(1, 2.717) should be(fromColumnMajorArray(2, 2, Array(1d, 1d, 2.717, 2.717)))
  }

  //  test("invert") {
  //  }
  //
  //  test("solve") {
  //  }

  test("flatMap apply Double => Matrix[1,2] to Matrix[r,c] to get a Matrix[r,2c]") {

    val m = new LinearAlgebraOps(fromColumnMajorArray(1, 2,
      Array(1.4, 22d)))

    m.flatMap { x => fromColumnMajorArray(1, 2, Array(x, 2 * x)) } should be(
      fromColumnMajorArray(1, 4, Array(1.4, 2.8, 22d, 44d)))
  }

  test("folds apply plus by row and column") {

    val m = new LinearAlgebraOps(fromColumnMajorArray(2, 3,
      Array(1.4, 22d, 17.5, 2.3, 18d, 105d)))

    m.foldLeft(zeros(2, 1))({ case (acc, c) => acc + c }) should be(m.rowSums)
    m.foldTop(zeros(1, 3))({ case (acc, r) => acc + r }) should be(m.columnSums)
  }

  test("range, min, max, argmax calculate {column,row,}x{arg,}x{min,max} (not including {row,col}arg{min,max})") {

    val m = new LinearAlgebraOps(fromColumnMajorArray(2, 3,
      Array(1.4, 22d, 17.5, 2.3, 18d, 105d)))

    m.rowMins should be(fromColumnMajorArray(2, 1, Array(1.4, 2.3)))
    m.rowMaxs should be(fromColumnMajorArray(2, 1, Array(18d, 105d)))
    m.rowRange should be(fromColumnMajorArray(2, 1, Array(16.6, 102.7)))
    m.columnMins should be(fromColumnMajorArray(1, 3, Array(1.4, 2.3, 18d)))
    m.columnMaxs should be(fromColumnMajorArray(1, 3, Array(22d, 17.5, 105d)))
    m.columnRange should be(fromColumnMajorArray(1, 3, Array(20.6, 15.2, 87d)))

    m.max should be(105d)
    m.argmax should be((1, 2))
    m.min should be(1.4)
    m.argmin should be((0, 0))
  }

  test("center, mean, sum by row and column") {

    val m = new LinearAlgebraOps(fromColumnMajorArray(2, 3,
      Array(1.4, 22d, 17.5, 2.3, 18d, 105d)))

    m.rowSums should be(fromColumnMajorArray(2, 1,
      Array(36.900000, 129.300000)))

    m.columnSums should be(fromColumnMajorArray(1, 3,
      Array(23.400000, 19.800000, 123.000000)))

    // 'floor' to workaround rounding error
    m.rowMeans.floor should be(fromColumnMajorArray(2, 1,
      Array(12d, 43d)))

    m.columnMeans should be(fromColumnMajorArray(1, 3,
      Array(11.7, 9.9, 61.5)))

    m.centerRows.floor should be(fromColumnMajorArray(2, 3,
      Array(-11d, -22d, 5d, -41, 5d, 61d)))

    m.centerColumns.floor should be(fromColumnMajorArray(2, 3,
      Array(-11d, 10d, 7d, -8d, -44d, 43d)))
  }

  test("sort columns and rows") {

    val m = new LinearAlgebraOps(fromColumnMajorArray(2, 3,
      Array(1.4, 22d, 17.5, 2.3, 18d, 105d)))

    m.sortRows should be(fromColumnMajorArray(2, 3,
      Array(1.4, 2.3, 17.5, 22d, 18d, 105d)))

    m.sortColumns should be(fromColumnMajorArray(2, 3,
      Array(1.4, 22d, 2.3, 17.5, 18d, 105d)))
  }

  test("ceil, floor, log, log10, pow on 2x3 matrix") {

    // mask raw matrix to ensure Axle's methods are being tested
    val m = new LinearAlgebraOps(fromColumnMajorArray(2, 3,
      Array(1.4, 22d, 17.5, 2.3, 18d, 105d)))

    m.ceil should be(fromColumnMajorArray(2, 3,
      Array(2d, 22d, 18d, 3d, 18d, 105d)))

    m.floor should be(fromColumnMajorArray(2, 3,
      Array(1d, 22d, 17d, 2d, 18d, 105d)))

    m.log.floor should be(fromColumnMajorArray(2, 3,
      Array(0d, 3d, 2d, 0d, 2d, 4d)))

    m.log10.floor should be(fromColumnMajorArray(2, 3,
      Array(0d, 1d, 1d, 0d, 1d, 2d)))

    m.pow(2d) should be(fromColumnMajorArray(2, 3,
      Array(1.9599999999999997, 484d, 306.25, 5.289999999999999, 324d, 11025d)))

  }

  test("addAssignment (1,2) to 6d in a 2x3 matrix, leaving original unmodified") {

    val m = fromColumnMajorArray(2, 3,
      Array(1d, 2d, 3d, 4d, 5d, 0d))

    m.addAssignment(1, 2, 6d) should be(fromColumnMajorArray(2, 3,
      Array(1d, 2d, 3d, 4d, 5d, 6d)))

    m should be(fromColumnMajorArray(2, 3,
      Array(1d, 2d, 3d, 4d, 5d, 0d)))
  }

  test("column and row vector operations on ones(2,2)") {

    val square = new LinearAlgebraOps(ones(2, 2))

    val row = fromColumnMajorArray(1, 2, Array(1d, 2d))
    val column = row.t

    square.addRowVector(row) should be(fromColumnMajorArray(2, 2, Array(2d, 2d, 3d, 3d)))
    square.subRowVector(row) should be(fromColumnMajorArray(2, 2, Array(0d, 0d, -1d, -1d)))
    square.mulRowVector(row) should be(fromColumnMajorArray(2, 2, Array(1d, 1d, 2d, 2d)))
    square.divRowVector(row) should be(fromColumnMajorArray(2, 2, Array(1d, 1d, 0.5, 0.5)))
    square.addColumnVector(column) should be(fromColumnMajorArray(2, 2, Array(2d, 3d, 2d, 3d)))
    square.subColumnVector(column) should be(fromColumnMajorArray(2, 2, Array(0d, -1d, 0d, -1d)))
    square.mulColumnVector(column) should be(fromColumnMajorArray(2, 2, Array(1d, 2d, 1d, 2d)))
    square.divColumnVector(column) should be(fromColumnMajorArray(2, 2, Array(1d, 0.5, 1d, 0.5)))
  }

  test("scalar operations on ones(2,2)") {

    val square = ones(2, 2)
    val sops = new LinearAlgebraOps(square)

    // implicit val module = moduleDoubleMatrix[Double]

    sops.addScalar(2d) should be(fromColumnMajorArray(2, 2, Array(3d, 3d, 3d, 3d)))
    sops.subtractScalar(2d) should be(fromColumnMajorArray(2, 2, Array(-1d, -1d, -1d, -1d)))
    square.multiplyScalar(2d) should be(fromColumnMajorArray(2, 2, Array(2d, 2d, 2d, 2d)))
    sops.divideScalar(2d) should be(fromColumnMajorArray(2, 2, Array(0.5, 0.5, 0.5, 0.5)))
  }

}


package axle.mtj

import scala.annotation.elidable
import scala.annotation.elidable.ASSERTION
import scala.math.sqrt
import scala.util.Random.nextDouble
import scala.util.Random.nextGaussian

import axle.algebra.FunctionPair
import axle.algebra.LinearAlgebra
import axle.Show
import axle.string
import no.uib.cipr.matrix.DenseMatrix
import no.uib.cipr.matrix.{ Matrix => MtjMatrix }
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

/**
 *
 * See https://github.com/fommil/matrix-toolkits-java
 *
 */

case class ConvertedMtjDenseMatrix[T](mtj: DenseMatrix)(implicit val fp: FunctionPair[Double, T])

object ConvertedMtjDenseMatrix {

//  implicit val jblasConvertedMatrix: Matrix[ConvertedMtjDenseMatrix] =
//    ???
  //new Matrix[ConvertedMtjDenseMatrix] {  }
}
    
//    def rows: Int = mtj.numRows
//    def columns: Int = mtj.numColumns
//    def length: Int = rows * columns
//    
//    def apply(i: Int, j: Int): T = converter(mtj.get(i, j))
//
//    def apply(rs: Seq[Int], cs: Seq[Int]): Matrix[T] = {
//      val mtj = new DenseMatrix(rs.length, cs.length)
//      rs.zipWithIndex foreach {
//        case (fromRow, toRow) =>
//          cs.zipWithIndex foreach {
//            case (fromCol, toCol) =>
//              mtj.set(toRow, toCol, converter.unapply(this(fromRow, fromCol)))
//          }
//      }
//      matrix[T](mtj)
//    }
//    // def update(i: Int, j: Int, v: T) = mtj.put(i, j, elementAdapter.converter.unapply(v))
//
//    def toList: List[T] = mtj match {
//      case dm: DenseMatrix => dm.getData.toList.map(converter.apply _)
//      case _               => ???
//    }


//    def isEmpty: Boolean = mtj.numRows == 0 && mtj.numColumns == 0
//    def isRowVector: Boolean = mtj.numRows == 1
//    def isColumnVector: Boolean = mtj.numColumns == 1
//    def isVector: Boolean = isRowVector || isColumnVector
//    def isSquare: Boolean = mtj.isSquare
//    def isScalar: Boolean = mtj.numRows == 1 && mtj.numColumns == 1
//
//    def dup: Matrix[T] = matrix(mtj.copy)
//
//    def negate: Matrix[T] = matrix(mtj.copy.scale(-1d))
//
//    def transpose: Matrix[T] = matrix(mtj.copy.transpose)


//    def addAssignment(r: Int, c: Int, v: T): Matrix[T] = {
//      val result = mtj.copy
//      result.set(r, c, converter.unapply(v))
//      matrix(result)(converter)
//    }


//    def addMatrix(other: Matrix[T]): Matrix[T] = {
//      val result = mtj.copy
//      result.add(other.mtj)
//      matrix(result)(converter)
//    }
//
//    def subtractMatrix(other: Matrix[T]): Matrix[T] = {
//      val result = mtj.copy
//      val otherCopy = other.mtj.copy
//      otherCopy.scale(-1d)
//      result.add(otherCopy)
//      matrix(result)(converter)
//    }


//    def max: T = {
//      var result = Double.MinValue
//      (0 to rows) foreach { r =>
//        (0 to columns) foreach { c =>
//          val v = mtj.get(r, c)
//          if (v > result) {
//            result = v
//          }
//        }
//      }
//      converter(result)
//    }

//    /**
//     * argmin: location (r, c) of the lowest value
//     *
//     */
//    def argmax: (Int, Int) = {
//      var (result, resultR, resultC) = (Double.MinValue, -1, -1)
//      (0 to rows) foreach { r =>
//        (0 to columns) foreach { c =>
//          val v = mtj.get(r, c)
//          if (v > result) {
//            result = v
//            resultR = r
//            resultC = c
//          }
//        }
//      }
//      (resultR, resultC)
//    }
//
//    def min: T = {
//      var result = Double.MaxValue
//      (0 to rows) foreach { r =>
//        (0 to columns) foreach { c =>
//          val v = mtj.get(r, c)
//          if (v < result) {
//            result = v
//          }
//        }
//      }
//      converter(result)
//    }
//
//    def argmin: (Int, Int) = {
//      var (result, resultR, resultC) = (Double.MaxValue, -1, -1)
//      (0 to rows) foreach { r =>
//        (0 to columns) foreach { c =>
//          val v = mtj.get(r, c)
//          if (v < result) {
//            result = v
//            resultR = r
//            resultC = c
//          }
//        }
//      }
//      (resultR, resultC)
//    }



//    // higher order methods
//
//    def map[B: C](f: T => B): Matrix[B] = {
//      val fpB = C[B]
//      val mtj = new DenseMatrix(rows, columns)
//      (0 until rows) foreach { r =>
//        (0 until columns) foreach { c =>
//          mtj.set(r, c, fpB.unapply(f(this(r, c))))
//        }
//      }
//      matrix[B](mtj)
//    }

//    def flatMapColumns[A: C](f: Matrix[T] => Matrix[A]): Matrix[A] = {
//      val fpA = C[A]
//      val mtj = new DenseMatrix(rows, columns)
//      (0 until columns) foreach { c =>
//        val fc = f(column(c))
//        (0 until rows) foreach { r =>
//          // assumes fc.rows === this.rows
//          mtj.set(r, c, fpA.unapply(fc(r, 0)))
//        }
//      }
//      matrix[A](mtj)
//    }
//
//  def foldLeft(m: M)(zero: M)(f: (M, M) => M): M =
//    (0 until columns(m)).foldLeft(zero)((x: M, c: ColT) => f(x, column(m)(c)))
//
//  def foldTop(m: M)(zero: M)(f: (M, M) => M): M =
//    (0 until rows(m)).foldLeft(zero)((x: M, r: RowT) => f(x, row(m)(r)))
    
//  }

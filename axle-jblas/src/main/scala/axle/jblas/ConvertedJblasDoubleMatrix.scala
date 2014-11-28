package axle.jblas

import org.jblas.DoubleMatrix
import axle.algebra.FunctionPair
import axle.algebra.Matrix
import spire.implicits.IntAlgebra
import spire.implicits.eqOps

case class ConvertedJblasDoubleMatrix[T](jdm: DoubleMatrix)(implicit val fp: FunctionPair[Double, T])

object ConvertedJblasDoubleMatrix {

  implicit val convertDouble: FunctionPair[Double, Double] = new FunctionPair[Double, Double] {
    def apply(d: Double) = d
    def unapply(t: Double) = t
  }

  implicit val convertInt: FunctionPair[Double, Int] = new FunctionPair[Double, Int] {
    def apply(d: Double) = d.toInt
    def unapply(t: Int) = t.toDouble
  }

  implicit val convertBoolean: FunctionPair[Double, Boolean] = new FunctionPair[Double, Boolean] {
    def apply(d: Double) = d != 0d
    def unapply(t: Boolean) = t match { case true => 0d case false => 1d }
  }

  implicit val jblasConvertedMatrix: Matrix[ConvertedJblasDoubleMatrix] =
    new Matrix[ConvertedJblasDoubleMatrix] {

      def rows[T](m: ConvertedJblasDoubleMatrix[T]): Int = m.jdm.getRows

      def columns[T](m: ConvertedJblasDoubleMatrix[T]): Int = m.jdm.getColumns

      def length[T](m: ConvertedJblasDoubleMatrix[T]): Int = m.jdm.getLength

      def get[T](m: ConvertedJblasDoubleMatrix[T])(i: Int, j: Int): T = m.fp(m.jdm.get(i, j))

      def apply[T](m: ConvertedJblasDoubleMatrix[T])(rs: Seq[Int], cs: Seq[Int]): ConvertedJblasDoubleMatrix[T] = {
        import m.fp
        val jblas = DoubleMatrix.zeros(rs.length, cs.length)
        rs.zipWithIndex foreach {
          case (fromRow, toRow) =>
            cs.zipWithIndex foreach {
              case (fromCol, toCol) =>
                jblas.put(toRow, toCol, m.fp.unapply(this.get(m)(fromRow, fromCol)))
            }
        }
        this.matrix[T](jblas)
      }

      def plus[T](x: ConvertedJblasDoubleMatrix[T])(y: ConvertedJblasDoubleMatrix[T]): ConvertedJblasDoubleMatrix[T] =
        ConvertedJblasDoubleMatrix[T](x.jdm.add(y.jdm))(x.fp)

      def matrix[T](jblas: DoubleMatrix)(implicit fp: FunctionPair[Double, T]): ConvertedJblasDoubleMatrix[T] =
        ConvertedJblasDoubleMatrix[T](jblas)

      def matrix[T](
        m: Int,
        n: Int,
        topleft: => T,
        left: Int => T,
        top: Int => T,
        fill: (Int, Int, T, T, T) => T)(implicit fp: FunctionPair[Double, T]): ConvertedJblasDoubleMatrix[T] = {
        
        val jblas = DoubleMatrix.zeros(m, n)
        jblas.put(0, 0, fp.unapply(topleft))
        (0 until m) foreach { r => jblas.put(r, 0, fp.unapply(left(r))) }
        (0 until n) foreach { c => jblas.put(0, c, fp.unapply(top(c))) }
        (1 until m) foreach { r =>
          (1 until n) foreach { c =>
            val diag = fp(jblas.get(r - 1, c - 1))
            val left = fp(jblas.get(r, c - 1))
            val right = fp(jblas.get(r - 1, c))
            jblas.put(r, c, fp.unapply(fill(r, c, diag, left, right)))
          }
        }
        matrix(jblas)
      }

      def matrix[T](m: Int, n: Int, f: (Int, Int) => T): ConvertedJblasDoubleMatrix[T] = ???

    }

}
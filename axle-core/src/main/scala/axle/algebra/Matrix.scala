package axle.algebra

// TODO: T should be convertable to/from Double
trait Matrix[M, T] {

  def rows(m: M): Int

  def columns(m: M): Int

  def length(m: M): Int

  def get(m: M, i: Int, j: Int): T

  def apply(m: M, rs: Seq[Int], cs: Seq[Int]): M

  def plus(x: M, y: M): M

}

//object Matrix {
//
//  import org.jblas.DoubleMatrix
//  import axle.algebra.FunctionPair
//  import axle.Show
//  import axle.string
//  import spire.implicits.IntAlgebra
//  import spire.implicits.eqOps
//
//  def jblasDoubleMatrix[T](implicit fp: FunctionPair[Double, T]): Matrix[DoubleMatrix, T] =
//    new Matrix[DoubleMatrix, T] {
//
//      def rows(m: DoubleMatrix): Int = m.getRows
//
//      def columns(m: DoubleMatrix): Int = m.getColumns
//
//      def length(m: DoubleMatrix): Int = m.getLength
//
//      def get(m: DoubleMatrix, i: Int, j: Int): T = fp(m.get(i, j))
//
//      def apply(m: DoubleMatrix, rs: Seq[Int], cs: Seq[Int]): DoubleMatrix = ???
//
//      // def toList(m: DoubleMatrix): List[T] = m.toArray
//
//      def plus(x: DoubleMatrix, y: DoubleMatrix): DoubleMatrix = x.add(y)
//
//    }
//
//}

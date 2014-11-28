package axle.algebra

trait Matrix[M[_]] {

  // TODO: T should be convertable to/from Double
  
  def rows[T](m: M[T]): Int

  def columns[T](m: M[T]): Int

  def length[T](m: M[T]): Int

  def get[T](m: M[T])(i: Int, j: Int): T

  def apply[T](m: M[T])(rs: Seq[Int], cs: Seq[Int]): M[T]

  def plus[T](x: M[T])(y: M[T]): M[T]

  def matrix[T](m: Int, n: Int, topleft: => T, left: Int => T, top: Int => T, fill: (Int, Int, T, T, T) => T)(implicit fp: FunctionPair[Double, T]): M[T]

  def matrix[T](m: Int, n: Int, f: (Int, Int) => T)(implicit fp: FunctionPair[Double, T]): M[T]

}

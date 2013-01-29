package axle.algebra

trait Semigroup[A] {
  def mappend(a: A, b: A): A
}

object Semigroup {

  implicit def IntSemigroup: Semigroup[Int] = new Semigroup[Int] {
    def mappend(a: Int, b: Int) = a + b
  }

  def IntMultSemigroup: Semigroup[Int] = new Semigroup[Int] {
    def mappend(a: Int, b: Int) = a * b
  }

  implicit def DoubleSemigroup: Semigroup[Double] = new Semigroup[Double] {
    def mappend(a: Double, b: Double) = a + b
  }

  implicit def StringSemigroup: Semigroup[String] = new Semigroup[String] {
    def mappend(a: String, b: String) = a + b
  }

  implicit def ListSemigroup[A]: Semigroup[List[A]] = new Semigroup[List[A]] {
    def mappend(a: List[A], b: List[A]) = a ++ b
  }

  implicit def Tuple2Semigroup[R: Semigroup, S: Semigroup]: Semigroup[(R, S)] = new Semigroup[(R, S)] {
    def mappend(a: (R, S), b: (R, S)) = (a._1 |+| b._1, a._2 |+| b._2)
  }

  implicit def Tuple3Semigroup[R: Semigroup, S: Semigroup, T: Semigroup]: Semigroup[(R, S, T)] = new Semigroup[(R, S, T)] {
    def mappend(a: (R, S, T), b: (R, S, T)) = (a._1 |+| b._1, a._2 |+| b._2, a._3 |+| b._3)
  }

  implicit def Tuple4Semigroup[R: Semigroup, S: Semigroup, T: Semigroup, U: Semigroup]: Semigroup[(R, S, T, U)] = new Semigroup[(R, S, T, U)] {
    def mappend(a: (R, S, T, U), b: (R, S, T, U)) = (a._1 |+| b._1, a._2 |+| b._2, a._3 |+| b._3, a._4 |+| b._4)
  }

}
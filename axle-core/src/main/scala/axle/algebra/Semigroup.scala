package axle.algebra

import spire.algebra.Semigroup

object Semigroups {

  implicit def IntSemigroup: Semigroup[Int] = new Semigroup[Int] {
    def op(x: Int, y: Int) = x + y
  }

  def IntMultSemigroup: Semigroup[Int] = new Semigroup[Int] {
    def op(x: Int, y: Int) = x * y
  }

  implicit def DoubleSemigroup: Semigroup[Double] = new Semigroup[Double] {
    def op(x: Double, y: Double) = x + y
  }

  implicit def StringSemigroup: Semigroup[String] = new Semigroup[String] {
    def op(x: String, y: String) = x + y
  }

  implicit def ListSemigroup[A]: Semigroup[List[A]] = new Semigroup[List[A]] {
    def op(x: List[A], y: List[A]) = x ++ y
  }

  //  implicit def MapSemigroup[K, V: Semigroup]: Semigroup[Map[K, V]] = new Semigroup[Map[K, V]] {
  //    def op(x: Map[K, V], y: Map[K, V]) = {
  //      val onlyX = x.keySet -- y.keySet
  //      val onlyY = y.keySet -- x.keySet
  //      val both = x.keySet.intersect(y.keySet)
  //      ???
  //    }
  //  }

  implicit def Tuple2Semigroup[R: Semigroup, S: Semigroup]: Semigroup[(R, S)] = new Semigroup[(R, S)] {
    def op(x: (R, S), y: (R, S)) = (x._1 |+| y._1, x._2 |+| y._2)
  }

  implicit def Tuple3Semigroup[R: Semigroup, S: Semigroup, T: Semigroup]: Semigroup[(R, S, T)] = new Semigroup[(R, S, T)] {
    def op(x: (R, S, T), y: (R, S, T)) = (x._1 |+| y._1, x._2 |+| y._2, x._3 |+| y._3)
  }

  implicit def Tuple4Semigroup[R: Semigroup, S: Semigroup, T: Semigroup, U: Semigroup]: Semigroup[(R, S, T, U)] = new Semigroup[(R, S, T, U)] {
    def op(x: (R, S, T, U), y: (R, S, T, U)) = (x._1 |+| y._1, x._2 |+| y._2, x._3 |+| y._3, x._4 |+| y._4)
  }

}
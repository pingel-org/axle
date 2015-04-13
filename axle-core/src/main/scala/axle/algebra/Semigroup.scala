package axle.algebra

import spire.algebra.Semigroup

object Semigroups {

  implicit def StringSemigroup: Semigroup[String] = new Semigroup[String] {
    def op(x: String, y: String): String = x + y
  }

  implicit def ListSemigroup[A]: Semigroup[List[A]] = new Semigroup[List[A]] {
    def op(x: List[A], y: List[A]): List[A] = x ++ y
  }

  implicit def MapSemigroup[K, V: Semigroup]: Semigroup[Map[K, V]] = new Semigroup[Map[K, V]] {

    val vSemi = Semigroup[V]

    def op(x: Map[K, V], y: Map[K, V]): Map[K, V] =
      (x.keySet ++ y.keySet).map(k => {
        if (x.contains(k)) {
          if (y.contains(k)) { (k, vSemi.op(x(k), y(k))) }
          else { (k, x(k)) }
        } else {
          (k, y(k))
        }
      }).toMap

  }


}

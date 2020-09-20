package axle.algebra

import scala.collection.immutable.TreeMap

import cats.implicits._

import spire.algebra._

package object eqs {

  implicit def eqIterable[T](implicit eqT: Eq[T]): Eq[Iterable[T]] =
    (x, y) =>
      x.size === y.size && x.zip(y).forall({ case (p, q) => eqT.eqv(p, q) })

  implicit def eqTreeMap[K, V](implicit eqK: Eq[K], eqV: Eq[V]): Eq[TreeMap[K, V]] = {
    val eqOptV = Eq[Option[V]]
    (x, y) =>
      Eq[Iterable[K]].eqv(x.keys, y.keys) && x.keySet.forall(k => eqOptV.eqv(x.get(k), y.get(k)))
  }

  implicit def eqIndexedSeq[T](implicit eqT: Eq[T]): Eq[IndexedSeq[T]] =
    (l: IndexedSeq[T], r: IndexedSeq[T]) =>
      l.size === r.size && (0 until l.size).forall( i => eqT.eqv(l(i), r(i)))

}

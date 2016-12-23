package axle

import scala.reflect.ClassTag

import axle.algebra.Aggregatable
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.MapFrom
import axle.algebra.MapReducible
import axle.algebra.SetFrom

import org.apache.spark.rdd.RDD

import com.twitter.chill.MeatLocker

package object spark {

  def freezeAtoB[A, B](f: A => B): A => B = {
    val locker = MeatLocker(f)
    x => locker.get.apply(x)
  }

  def freezeAtoBC[A, B, C](f: A => (B, C)): A => (B, C) = {
    val locker = MeatLocker(f)
    x => locker.get.apply(x)
  }

  def freezeBAtoB[A, B](f: (B, A) => B): (B, A) => B = {
    val locker = MeatLocker(f)
    (b, a) => locker.get.apply(b, a)
  }

  def freezeBBtoB[B](f: (B, B) => B): (B, B) => B = {
    val locker = MeatLocker(f)
    (b1, b2) => locker.get.apply(b1, b2)
  }

  implicit def finiteRDD[A]: Finite[RDD[A], Long] =
    new Finite[RDD[A], Long] {

      def size(rdd: RDD[A]): Long = rdd.count
    }

  implicit def aggregatableRDD[A, B: ClassTag]: Aggregatable[RDD[A], A, B] =
    new Aggregatable[RDD[A], A, B] {
      def aggregate(rdd: RDD[A])(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
        rdd.aggregate(zeroValue)(freezeBAtoB(seqOp), freezeBBtoB(combOp))
    }

  implicit def functorRDD[A, B: ClassTag]: Functor[RDD[A], A, B, RDD[B]] =
    new Functor[RDD[A], A, B, RDD[B]] {
      def map(rdd: RDD[A])(f: A => B): RDD[B] =
        rdd.map(freezeAtoB(f))
    }

  implicit def toMapRDD[K: ClassTag, V: ClassTag]: MapFrom[RDD[(K, V)], K, V] =
    new MapFrom[RDD[(K, V)], K, V] {

      def toMap(rdd: RDD[(K, V)]): Map[K, V] =
        rdd.collect().toMap
    }

  implicit def mapReduceRDD[A: ClassTag, B: ClassTag, K: ClassTag] =
    new MapReducible[RDD[A], A, B, K, RDD[(K, B)]] {

      def mapReduce(
        input: RDD[A],
        mapper: A => (K, B),
        zero: B,
        reduce: (B, B) => B): RDD[(K, B)] =
        input
          .map(freezeAtoBC(mapper))
          .groupBy(_._1) // TODO: groupByKey
          .map({
            case (k, kbs) => {
              (k, kbs.map(_._2).aggregate(zero)(freezeBBtoB(reduce), freezeBBtoB(reduce)))
            }
          })
    }

  implicit def toSetRDD[A]: SetFrom[RDD[A], A] =
    new SetFrom[RDD[A], A] {

      def toSet(rdd: RDD[A]): Set[A] =
        rdd.collect.toSet
    }

}

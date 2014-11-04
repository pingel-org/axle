package axle

import scala.collection.GenTraversableOnce
import scala.reflect.ClassTag

import axle.algebra.Aggregatable
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.MapFrom
import axle.algebra.MapReducible
import axle.algebra.SetFrom

import org.apache.spark.Partition
import org.apache.spark.TaskContext
import org.apache.spark.rdd.RDD

package object spark {

  implicit def finiteRDD: Finite[RDD] = new Finite[RDD] {

    def size[A: ClassTag](rdd: RDD[A]): Long =
      rdd.count
  }

  implicit def aggregatableRDD = new Aggregatable[RDD] {
    def aggregate[A, B: ClassTag](rdd: RDD[A])(zeroValue: B)(seqOp: (B, A) => B, combOp: (B, B) => B): B =
      rdd.aggregate(zeroValue)(seqOp, combOp)
  }

  implicit def functorRDD = new Functor[RDD] {
    def map[A, B: ClassTag](rdd: RDD[A])(f: A => B): RDD[B] =
      rdd.map(f)
  }

  implicit def mapFromRDD: MapFrom[RDD] = new MapFrom[RDD] {

    def toMap[K: ClassTag, V: ClassTag](rdd: RDD[(K, V)]): Map[K, V] =
      rdd.collect().toMap
  }

  implicit def mapReduceRDD: MapReducible[RDD] = new MapReducible[RDD] {

    def mapReduce[A: ClassTag, B: ClassTag, K: ClassTag](
      input: RDD[A],
      mapper: A => (K, B),
      zero: B,
      reduce: (B, B) => B): RDD[(K, B)] =
      input
        .map(mapper)
        .groupBy(_._1)
        .map({
          case (k, kbs) => {
            (k, kbs.map(_._2).aggregate(zero)(reduce, reduce))
          }
        })
  }

  implicit def settableRDD: SetFrom[RDD] = new SetFrom[RDD] {

    def toSet[A: ClassTag](rdd: RDD[A]): Set[A] =
      rdd.collect.toSet
  }

}
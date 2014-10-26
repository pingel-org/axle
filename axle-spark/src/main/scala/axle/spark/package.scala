package axle

import org.apache.spark.rdd.RDD
import org.apache.spark.Partition
import org.apache.spark.TaskContext
import scala.collection.GenTraversableOnce
import scala.reflect.ClassTag

package object spark {

  implicit class RDDAsSeq[A](rdd: RDD[A])(implicit split: Partition, context: TaskContext)
    extends Seq[A] {

    def iterator: Iterator[A] =
      rdd.iterator(split, context)

    def apply(idx: Int): A =
      rdd.apply(idx)

    def length: Int =
      rdd.length

    def map[B: ClassTag](f: A ⇒ B): RDD[B] =
      rdd.map(f)

    def flatMap[B: ClassTag](f: (A) ⇒ TraversableOnce[B]): RDD[B] =
      rdd.flatMap { f }

    override def filter(p: (A) ⇒ Boolean): Seq[A] =
      rdd.filter { p }

    def collect[B: ClassTag](pf: PartialFunction[A, B]): Seq[B] =
      rdd.collect { pf }

    //def reduce[A1 >: A](op: (A1, A1) => A1): A1 =
    //  rdd.reduce { op }

    //    override def foldLeft[B: ClassTag](z: B)(op: (B, A) ⇒ B): B =
    //      rdd.foldLeft(z)(op)

    def aggregate[B: ClassTag](z: ⇒ B)(seqOp: (B, A) ⇒ B, combOp: (B, B) ⇒ B): B =
      rdd.aggregate(z)(seqOp, combOp)

    def groupBy[B: ClassTag](f: A => B) =
      rdd.groupBy { f }

  }

}
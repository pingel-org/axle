package axle

import org.apache.spark.rdd.RDD

package object spark {

  implicit class RDDAsSeq[T](rdd: RDD[T]) extends Seq[T] {

    def iterator: Iterator[T] = ???

    def apply(idx: Int): T = ???
    
    def length: Int = ???

  }

}
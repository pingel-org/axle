import scala.collection.mutable.Buffer

import scala.reflect.ClassTag
import scala.collection.immutable.TreeMap
import scala.language.implicitConversions

/**
 * Axle enrichments of scala collections
 *
 */

package object axle {

  implicit def enrichIterable[T](ita: Iterable[T]): EnrichedIterable[T] = EnrichedIterable(ita)

  implicit def enrichIndexedSeq[T](is: IndexedSeq[T]): EnrichedIndexedSeq[T] = EnrichedIndexedSeq(is)

  implicit def enrichIterator[T](it: Iterator[T]) = new EnrichedIterator(it)

  implicit def enrichByteArray(barr: Array[Byte]): EnrichedByteArray = EnrichedByteArray(barr)

  implicit def enrichMutableBuffer[T](buffer: Buffer[T]): EnrichedMutableBuffer[T] = EnrichedMutableBuffer(buffer)

  implicit def enrichArray[T: ClassTag](arr: Array[T]): EnrichedArray[T] = EnrichedArray(arr)

  implicit def enrichInt(n: Int): EnrichedInt = EnrichedInt(n)

}

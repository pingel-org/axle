import scala.collection.mutable.Buffer

import scala.reflect.ClassTag
import scala.language.implicitConversions

/**
 * Axle enrichments of scala collections
 *
 */

package object axle {

  implicit def enrichIndexedSeq[T](is: IndexedSeq[T]): EnrichedIndexedSeq[T] = EnrichedIndexedSeq(is)

  implicit def enrichMutableBuffer[T](buffer: Buffer[T]): EnrichedMutableBuffer[T] = EnrichedMutableBuffer(buffer)

  implicit def enrichArray[T: ClassTag](arr: Array[T]): EnrichedArray[T] = EnrichedArray(arr)

}

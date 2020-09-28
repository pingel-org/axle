import scala.collection.mutable.Buffer

import scala.language.implicitConversions

package object axle {

  implicit def enrichMutableBuffer[T](buffer: Buffer[T]): EnrichedMutableBuffer[T] = EnrichedMutableBuffer(buffer)

}

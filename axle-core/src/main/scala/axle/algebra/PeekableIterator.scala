package axle.algebra

/**
 * A PeekableIterator is an iterator with a buffer of length one
 * that can be "peeked"
 * 
 */

class PeekableIterator[T](it: Iterator[T]) extends Iterator[T] {

  private var buffer: Option[T] = None
  
  def advance(): Unit = {
    buffer = if( it.hasNext ) Option(it.next()) else None
  }

  advance()

  def peek: Option[T] = buffer

  def hasNext: Boolean = buffer.isDefined

  def next(): T = {
    val result = buffer.get
    advance()
    result
  }

}
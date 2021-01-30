package axle.algebra

/**
 * A PeekableIterator is an iterator with a buffer of length one
 * that can be "peeked"
 * 
 */

class PeekableIterator[T](it: Iterator[T]) extends Iterator[T] {

  private var _buffer: Option[T] = None

  def advance(): Unit = {
    _buffer =
      if( it.hasNext ) {
        Option(it.next())
      } else {
        None
      }
  }

  advance()

  def peek: Option[T] = _buffer

  def hasNext: Boolean = _buffer.isDefined

  def next(): T = {
    val result = _buffer.get
    advance()
    result
  }

}

package axle

object AxleSimple {

  def getLine: List[Char] = scala.io.Source.stdin.getLine(1).toList // List[Char]

//  case class EnrichedAny[T](a: T) { }
//  implicit def enrichAny[T](a: T) = EnrichedAny[T](a)

  def replicate[T](n: Int)(v: T) = (0 until n).map( i => v ).toList

  def reverse[T](l: List[T]) = l.reverse

  def intersperse[T](d: T)(implicit l: List[T]) =
    (0 until ( 2*l.size - 1 ) ).map( i => i % 2 match { case 0 => l(i/2) case 1 => d } ).toList

  // if we define intersperse with a single argument list we would have to use
  //
  //    (intersperse[Char] _ curried) apply('-')
  //
  // instead of simply:
  //
  //    intersperse('-')

  def id[A](x: A) = x

  // Functors define fmap
  def fmaplist[A,B](f: (A) => B)(l: List[A]) = l.map( f )
  def fmapopt[A,B](f: (A) => B)(o: Option[A]) = o.map( f )
  def fmapleft[A,B](f: (A) => B)(l: Left[A, _]) = l
  def fmapright[A,B](f: (A) => B)(r: Right[_, A]) = r match { case Right(v: A) => Right(f(v)) }
  def fmapfn[A,B,C](f1: (B) => C)(f2: (A) => B) = f1 compose f2

}

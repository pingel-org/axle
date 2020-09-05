package axle

package object nlp {

  def bigrams[T](xs: Iterable[T]): Iterable[(T, T)] =
    xs.zip(xs.tail)

}

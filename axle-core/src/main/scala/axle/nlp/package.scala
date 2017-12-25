package axle

import scala.collection.GenSeq

package object nlp {

  def bigrams[T](xs: GenSeq[T]): GenSeq[(T, T)] =
    xs.zip(xs.tail)

}

package axle.algebra

trait FromStream[S[_]] {

  def fromStream[A](stream: Stream[A]): S[A]
}

object FromStream {

  implicit def indexedSeqFromStream = new FromStream[IndexedSeq] {

    def fromStream[A](stream: Stream[A]): IndexedSeq[A] = stream.toIndexedSeq
  }
}

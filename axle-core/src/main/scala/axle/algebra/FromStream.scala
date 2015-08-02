package axle.algebra

trait FromStream[S, A] {

  def fromStream(stream: Stream[A]): S
}

object FromStream {

  implicit def indexedSeqFromStream[A] =
    new FromStream[IndexedSeq[A], A] {

      def fromStream(stream: Stream[A]): IndexedSeq[A] = stream.toIndexedSeq
    }
}

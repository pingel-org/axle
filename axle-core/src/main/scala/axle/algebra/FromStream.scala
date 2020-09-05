package axle.algebra

trait FromStream[S, A] {

  def fromStream(stream: LazyList[A]): S
}

object FromStream {

  implicit def indexedSeqFromLazyList[A] =
    new FromStream[IndexedSeq[A], A] {

      def fromStream(stream: LazyList[A]): IndexedSeq[A] = stream.toIndexedSeq
    }
}

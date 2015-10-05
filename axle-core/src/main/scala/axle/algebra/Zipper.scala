package axle.algebra

trait Zipper[X, A, Y, B, Z] {

  def zip(left: X, right: Y): Z

  def unzip(zipped: Z): (X, Y)
}

object Zipper {

  def apply[X, A, Y, B, Z](implicit zipper: Zipper[X, A, Y, B, Z]): Zipper[X, A, Y, B, Z] =
    zipper

  implicit def zipSeq[A, B]: ZipperK1[Seq, A, B] =
    new Zipper[Seq[A], A, Seq[B], B, Seq[(A, B)]] {

      def zip(left: Seq[A], right: Seq[B]): Seq[(A, B)] =
        left.zip(right)

      def unzip(zipped: Seq[(A, B)]): (Seq[A], Seq[B]) =
        zipped.unzip
    }

  implicit def zipList[A, B]: ZipperK1[List, A, B] =
    new Zipper[List[A], A, List[B], B, List[(A, B)]] {

      def zip(left: List[A], right: List[B]): List[(A, B)] =
        left.zip(right)

      def unzip(zipped: List[(A, B)]): (List[A], List[B]) =
        zipped.unzip
    }

  implicit def zipIndexedSeq[A, B]: ZipperK1[IndexedSeq, A, B] =
    new Zipper[IndexedSeq[A], A, IndexedSeq[B], B, IndexedSeq[(A, B)]] {

      def zip(left: IndexedSeq[A], right: IndexedSeq[B]): IndexedSeq[(A, B)] =
        left.zip(right)

      def unzip(zipped: IndexedSeq[(A, B)]): (IndexedSeq[A], IndexedSeq[B]) =
        zipped.unzip
    }

}
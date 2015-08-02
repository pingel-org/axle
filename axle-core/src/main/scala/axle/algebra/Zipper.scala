package axle.algebra

trait Zipper[Z, A, ZZ] {

  def zip(left: Z, right: Z): ZZ

  def unzip(zipped: ZZ): (Z, Z)

}

object Zipper {

  implicit def zipperK1[M[_], A]: Zipper[M[A], A, M[(A, A)]] =
    implicitly[Zipper[M[A], A, M[(A, A)]]]

  type ZipperK1[M[_], A] = Zipper[M[A], A, M[(A, A)]]

  def apply[Z, A, ZZ](implicit zipper: Zipper[Z, A, ZZ]): Zipper[Z, A, ZZ] =
    zipper

  implicit def zipSeq[A]: ZipperK1[Seq, A] =
    new Zipper[Seq[A], A, Seq[(A, A)]] {

      def zip(left: Seq[A], right: Seq[A]): Seq[(A, A)] =
        left.zip(right)

      def unzip(zipped: Seq[(A, A)]): (Seq[A], Seq[A]) =
        zipped.unzip
    }

  implicit def zipIndexedSeq[A] =
    new Zipper[IndexedSeq[A], A, IndexedSeq[(A, A)]] {

      def zip(left: IndexedSeq[A], right: IndexedSeq[A]): IndexedSeq[(A, A)] =
        left.zip(right)

      def unzip(zipped: IndexedSeq[(A, A)]): (IndexedSeq[A], IndexedSeq[A]) =
        zipped.unzip
    }

}
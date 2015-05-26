package axle.algebra

trait Zipper[Z[_]] {

  def zip[A](left: Z[A], right: Z[A]): Z[(A, A)]

  def unzip[A](zipped: Z[(A, A)]): (Z[A], Z[A])

}

object Zipper {

  def apply[Z[_]](implicit zipper: Zipper[Z]): Zipper[Z] = zipper

  implicit def zipSeq = new Zipper[Seq] {

    def zip[A](left: Seq[A], right: Seq[A]): Seq[(A, A)] =
      left.zip(right)

    def unzip[A](zipped: Seq[(A, A)]): (Seq[A], Seq[A]) =
      zipped.unzip
  }

  implicit def zipIndexedSeq = new Zipper[IndexedSeq] {

    def zip[A](left: IndexedSeq[A], right: IndexedSeq[A]): IndexedSeq[(A, A)] =
      left.zip(right)

    def unzip[A](zipped: IndexedSeq[(A, A)]): (IndexedSeq[A], IndexedSeq[A]) =
      zipped.unzip
  }

}
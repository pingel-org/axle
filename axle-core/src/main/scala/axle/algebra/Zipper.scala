package axle.algebra

trait Zipper[Z[_], A] {

  def zip(left: Z[A], right: Z[A]): Z[(A, A)]

}

object Zipper {

  def apply[Z[_], A](implicit zipper: Zipper[Z, A]): Zipper[Z, A] = zipper

  implicit def zipSeq[A] = new Zipper[Seq, A] {
    def zip(left: Seq[A], right: Seq[A]): Seq[(A, A)] =
      left.zip(right)
  }

}
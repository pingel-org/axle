package axle.algebra

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Indexed[${C}, ${I}]")
trait Indexed[C[_], I] {

  def at[A](xs: C[A])(i: I): A

  def slyce[A](xs: C[A])(range: Range): C[A]

  def take[A](xs: C[A])(i: I): C[A]

  def drop[A](xs: C[A])(i: I): C[A]
}

object Indexed {

  final def apply[C[_], I](implicit ev: Indexed[C, I]): Indexed[C, I] = ev

  implicit val indexedSeq: Indexed[Seq, Int] =
    new Indexed[Seq, Int] {

      def at[A](seq: Seq[A])(i: Int): A = seq(i)

      def slyce[A](xs: Seq[A])(range: Range): Seq[A] = {
        import cats.implicits._
        assert(range.step === 1)
        if (range.isEmpty) {
          Seq.empty[A]
        } else {
          xs.slice(range.start, range.last + 1)
        }
      }

      def take[A](xs: Seq[A])(i: Int): Seq[A] = xs.take(i)

      def drop[A](xs: Seq[A])(i: Int): Seq[A] = xs.drop(i)
    }

  implicit val indexedIndexedSeq: Indexed[IndexedSeq, Int] =
    new Indexed[IndexedSeq, Int] {

      def at[A](is: IndexedSeq[A])(i: Int): A = is(i)

      def slyce[A](xs: IndexedSeq[A])(range: Range): IndexedSeq[A] = {
        import cats.implicits._
        assert(range.step === 1)
        if (range.isEmpty) {
          IndexedSeq.empty[A]
        } else {
          xs.slice(range.start, range.last + 1)
        }
      }

      def take[A](xs: IndexedSeq[A])(i: Int): IndexedSeq[A] = xs.take(i)

      def drop[A](xs: IndexedSeq[A])(i: Int): IndexedSeq[A] = xs.drop(i)
    }

  implicit val indexedArray: Indexed[Array, Int] =
    new Indexed[Array, Int] {

      def at[A](seq: Array[A])(i: Int): A = seq(i)

      def slyce[A](xs: Array[A])(range: Range): Array[A] = {
        import cats.implicits._
        assert(range.step === 1)
        if (range.isEmpty) {
          ??? // Array.empty[A]
        } else {
          xs.slice(range.start, range.last + 1)
        }
      }

      def take[A](xs: Array[A])(i: Int): Array[A] = xs.take(i)

      def drop[A](xs: Array[A])(i: Int): Array[A] = xs.drop(i)
    }

  implicit val indexedList: Indexed[List, Int] =
    new Indexed[List, Int] {

      def at[A](list: List[A])(i: Int): A = list(i)

      def slyce[A](xs: List[A])(range: Range): List[A] = {
        import cats.implicits._
        assert(range.step === 1)
        if (range.isEmpty) {
          List.empty[A]
        } else {
          xs.slice(range.start, range.last + 1)
        }
      }

      def take[A](xs: List[A])(i: Int): List[A] = xs.take(i)

      def drop[A](xs: List[A])(i: Int): List[A] = xs.drop(i)
    }

  implicit val vectorIndexed: Indexed[Vector, Int] =
    new Indexed[Vector, Int] {

      def at[A](vector: Vector[A])(i: Int): A = vector(i)

      def slyce[A](xs: Vector[A])(range: Range): Vector[A] = {
        import cats.implicits._
        assert(range.step === 1)
        if (range.isEmpty) {
          Vector.empty[A]
        } else {
          xs.slice(range.start, range.last + 1)
        }
      }

      def take[A](xs: Vector[A])(i: Int): Vector[A] = xs.take(i)

      def drop[A](xs: Vector[A])(i: Int): Vector[A] = xs.drop(i)
    }

  import scala.collection.mutable.Buffer
  import scala.collection.mutable.ListBuffer
  implicit val bufferIndexed: Indexed[Buffer, Int] =
    new Indexed[Buffer, Int] {

      def at[A](buffer: Buffer[A])(i: Int): A = buffer(i)

      def slyce[A](xs: Buffer[A])(range: Range): Buffer[A] = {
        import cats.implicits._
        assert(range.step === 1)
        if (range.isEmpty) {
          ListBuffer.empty[A]
        } else {
          xs.slice(range.start, range.last + 1)
        }
      }

      def take[A](xs: Buffer[A])(i: Int): Buffer[A] = xs.take(i)

      def drop[A](xs: Buffer[A])(i: Int): Buffer[A] = xs.drop(i)
    }

}

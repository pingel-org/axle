object NickPartridgeScalaz9 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // Pull out a FoldLeft typeclass, using the same technique that we used for Monoid
  // So that our sum method has to reference to List or Int

  trait Monoid[A] {
    def mappend(a1: A, a2: A): A
    def mzero: A
  }

  object Monoid {

    implicit object IntMonoid extends Monoid[Int] {
      def mappend(a: Int, b: Int): Int = a + b
      def mzero: Int = 0
    }

    implicit object StringMonoid extends Monoid[String] {
      def mappend(a: String, b: String): String = a + b
      def mzero: String = ""
    }
  }

  def multMonoid = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a * b
    def mzero = 1
  }                                               //> multMonoid: => NickPartridgeScalaz9.Monoid[Int]

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (B, A) => B): B
  }

  object FoldLeft { // companion object to the FoldLeft trait
    implicit object FoldLeftList extends FoldLeft[List] {
      def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B): B = xs.foldLeft(b)(f)
    }
  }

  def sum[M[_], T](xs: M[T])(implicit m: Monoid[T], fl: FoldLeft[M]): T = fl.foldLeft(xs, m.mzero, m.mappend)
                                                  //> sum: [M[_], T](xs: M[T])(implicit m: NickPartridgeScalaz9.Monoid[T], implic
                                                  //| it fl: NickPartridgeScalaz9.FoldLeft[M])T

  sum(List(1, 2, 3, 4))                           //> res0: <error> = 10
  sum(List("a", "b", "c"))                        //> res1: <error> = abc

  sum(List(1, 2, 3, 4))(multMonoid, FoldLeft.FoldLeftList)
                                                  //> res2: Int = 24

}
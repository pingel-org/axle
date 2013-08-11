object NickPartridgeScalaz10 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // In order to make the explicit passing of multMonoid work
  // we also now need to supply a FoldLeft, which we can have Scala
  // find by using "implicitly", which we can think of as being implemented as:
  //
  //    def implicitly[T](implicit t: T): T = t

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
  }                                               //> multMonoid: => NickPartridgeScalaz10.Monoid[Int]

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (B, A) => B ): B
  }

  object FoldLeft {
    implicit object FoldLeftList extends FoldLeft[List] {
      def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B ): B = xs.foldLeft(b)(f)
    }
  }

  def sum[M[_], T](xs: M[T])(implicit m: Monoid[T], fl: FoldLeft[M]): T = fl.foldLeft(xs, m.mzero, m.mappend)
                                                  //> sum: [M[_], T](xs: M[T])(implicit m: NickPartridgeScalaz10.Monoid[T], impli
                                                  //| cit fl: NickPartridgeScalaz10.FoldLeft[M])T

  sum(List(1, 2, 3, 4))                           //> res0: <error> = 10
  
  sum(List("a", "b", "c"))                        //> res1: <error> = abc
  
  sum(List(1, 2, 3, 4))(multMonoid, implicitly[FoldLeft[List]])
                                                  //> res2: Int = 24


}
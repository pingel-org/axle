object NickPartridgeScalaz8 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // recall the endgame:
  //   def sum[M[_], A](xs: M[A])(...): A
  //
  // generalize to sum higher-kinded anything

  // Start by defining a foldLeftList and redefining sum in terms of it

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
  }                                               //> multMonoid: => NickPartridgeScalaz8.Monoid[Int]

  object FoldLeftList {
    def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B ) = xs.foldLeft(b)(f)
  }

  def sum[T](xs: List[T])(implicit m: Monoid[T]): T = FoldLeftList.foldLeft(xs, m.mzero, m.mappend)
                                                  //> sum: [T](xs: List[T])(implicit m: NickPartridgeScalaz8.Monoid[T])T

  sum(List(1, 2, 3, 4))                           //> res0: Int = 10
  
  sum(List("a", "b", "c"))                        //> res1: String = abc
  
  sum(List(1, 2, 3, 4))(multMonoid)               //> res2: Int = 24

}
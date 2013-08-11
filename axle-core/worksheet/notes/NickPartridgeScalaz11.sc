object NickPartridgeScalaz11 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // Create a trait called Identity[A] that wraps *any* value and provides a "plus" method
  //
  // In order to be able to call "plus" on anything as long as there's a Monoid available for the 'A'
  // instead of doing this:
  //
  // def plus[T](a: T, b: T)(implicit m: Monoid[T]): T = m.mappend(a, b)

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
  }                                               //> multMonoid: => NickPartridgeScalaz11.Monoid[Int]

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (B, A) => B ): B
  }

  object FoldLeft {
    implicit object FoldLeftList extends FoldLeft[List] {
      def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B ): B = xs.foldLeft(b)(f)
    }
  }

  trait Identity[A] {
    val value: A
    def plus(a2: A)(implicit m: Monoid[A]): A = m.mappend(value, a2)
  }

  // Note that the name of this implicit def could be anything.  It's found by its type signature, not its name.
  implicit def toIdent[A](a: A): Identity[A] = new Identity[A] {
    val value = a
  }                                               //> toIdent: [A](a: A)NickPartridgeScalaz11.Identity[A]

  def sum[M[_], T](xs: M[T])(implicit m: Monoid[T], fl: FoldLeft[M]): T = fl.foldLeft(xs, m.mzero, m.mappend)
                                                  //> sum: [M[_], T](xs: M[T])(implicit m: NickPartridgeScalaz11.Monoid[T], impli
                                                  //| cit fl: NickPartridgeScalaz11.FoldLeft[M])T

  sum(List(1, 2, 3, 4))                           //> res0: <error> = 10
  
  sum(List("a", "b", "c"))                        //> res1: <error> = abc
  
  sum(List(1, 2, 3, 4))(multMonoid, implicitly[FoldLeft[List]])
                                                  //> res2: Int = 24

  3.plus(4)                                       //> res3: <error> = 7

  3 plus 4                                        //> res4: <error> = 7

}
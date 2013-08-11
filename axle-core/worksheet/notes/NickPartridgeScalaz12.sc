object NickPartridgeScalaz12 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // Scalaz calls that |+|
  //
  // Also replace "implicit object" with "implicit val" in order to avoid ???
  //

  trait Monoid[A] {
    def mappend(a1: A, a2: A): A
    def mzero: A
  }

  object Monoid {

    implicit val IntMonoid: Monoid[Int] = new Monoid[Int] {
      def mappend(a: Int, b: Int): Int = a + b
      def mzero: Int = 0
    }

    implicit val StringMonoid: Monoid[String]  = new Monoid[String] {
      def mappend(a: String, b: String): String = a + b
      def mzero: String = ""
    }
  }

  def multMonoid = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a * b
    def mzero = 1
  }                                               //> multMonoid: => NickPartridgeScalaz12.Monoid[Int]

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (B, A) => B ): B
  }

  object FoldLeft {
    implicit val FoldLeftList: FoldLeft[List] = new FoldLeft[List] {
      def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B ): B = xs.foldLeft(b)(f)
    }
  }

  trait Identity[A] {
    val value: A
    def |+|(a2: A)(implicit m: Monoid[A]): A = m.mappend(value, a2)
  }

  // Note that the name of this implicit def could be anything.  It's found by its type signature, not its name.
  implicit def toIdent[A](a: A): Identity[A] = new Identity[A] {
    val value = a
  }                                               //> toIdent: [A](a: A)NickPartridgeScalaz12.Identity[A]

  def sum[M[_], T](xs: M[T])(implicit m: Monoid[T], fl: FoldLeft[M]): T = fl.foldLeft(xs, m.mzero, m.mappend)
                                                  //> sum: [M[_], T](xs: M[T])(implicit m: NickPartridgeScalaz12.Monoid[T], impli
                                                  //| cit fl: NickPartridgeScalaz12.FoldLeft[M])T

  sum(List(1, 2, 3, 4))                           //> res0: <error> = 10

  sum(List("a", "b", "c"))                        //> res1: <error> = abc

  sum(List(1, 2, 3, 4))(multMonoid, implicitly[FoldLeft[List]])
                                                  //> res2: Int = 24

  3 . |+| (4) // need spaces to help the parser if you want to use the dot notation
                                                  //> res3: <error> = 7
  
  3 |+| 4                                         //> res4: <error> = 7


}
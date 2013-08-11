object NickPartridgeScalaz13 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // We still need work on "sum".  We can't simply add this to Identity:
  //
  //    def sum(implicit fl: FoldLeft[...])
  //
  // because we need to know what the type constructor used on that 'A' is.
  //
  // The solution is:
  // 1) create another type class called "MA"
  // 2) delete previous "sum"
  // 3) provide another implicit for MA

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
  }                                               //> multMonoid: => NickPartridgeScalaz13.Monoid[Int]

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
  }                                               //> toIdent: [A](a: A)NickPartridgeScalaz13.Identity[A]

  trait MA[M[_], A] {
    val value: M[A]

    // Note that we can't use "sum" in order to avoid conflict with existing List.sum
    def summ(implicit m: Monoid[A], fl: FoldLeft[M]): A = fl.foldLeft(value, m.mzero, m.mappend)
  }

  implicit def toMA[M[_], A](ma: M[A]): MA[M, A] = new MA[M, A] {
    val value = ma
  }                                               //> toMA: [M[_], A](ma: M[A])NickPartridgeScalaz13.MA[M,A]

  List(1, 2, 3, 4).summ                           //> res0: <error> = 10
  
  List(1, 2, 3, 4).summ(multMonoid, implicitly[FoldLeft[List]])
                                                  //> res1: Int = 24

  3 |+| 4                                         //> res2: <error> = 7

}
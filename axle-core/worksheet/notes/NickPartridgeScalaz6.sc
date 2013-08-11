object NickPartridgeScalaz6 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // Demonstrate the generality of Monoid by creating one for String

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

  def sum[T](xs: List[T])(implicit m: Monoid[T]): T = xs.foldLeft(m.mzero)(m.mappend)
                                                  //> sum: [T](xs: List[T])(implicit m: NickPartridgeScalaz6.Monoid[T])T

  sum(List(1, 2, 3, 4))                           //> res0: Int = 10
  
  sum(List("a", "b", "c"))                        //> res1: String = abc

}
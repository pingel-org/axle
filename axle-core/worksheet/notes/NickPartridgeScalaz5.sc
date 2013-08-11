object NickPartridgeScalaz5 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // Scala finds implicits in the scope, including a companion object for the type of the implicit

  trait Monoid[A] {
    def mappend(a1: A, a2: A): A
    def mzero: A
  }

  object Monoid {
    implicit object IntMonoid extends Monoid[Int] {
      def mappend(a: Int, b: Int): Int = a + b
      def mzero: Int = 0
    }
  }

  def sum[T](xs: List[T])(implicit m: Monoid[T]): T = xs.foldLeft(m.mzero)(m.mappend)
                                                  //> sum: [T](xs: List[T])(implicit m: NickPartridgeScalaz5.Monoid[T])T

  sum(List(1, 2, 3, 4))                           //> res0: Int = 10


}
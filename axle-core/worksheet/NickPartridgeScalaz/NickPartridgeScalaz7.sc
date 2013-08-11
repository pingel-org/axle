object NickPartridgeScalaz7 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // Define multMonoid to demo another example of a monoid
  // as well as how to use a non-implicit monoid

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
  }                                               //> multMonoid: => NickPartridgeScalaz7.Monoid[Int]

  def sum[T](xs: List[T])(implicit m: Monoid[T]): T = xs.foldLeft(m.mzero)(m.mappend)
                                                  //> sum: [T](xs: List[T])(implicit m: NickPartridgeScalaz7.Monoid[T])T

  sum(List(1, 2, 3, 4))                           //> res0: Int = 10
  
  sum(List("a", "b", "c"))                        //> res1: String = abc
  
  sum(List(1, 2, 3, 4))(multMonoid)               //> res2: Int = 24

}
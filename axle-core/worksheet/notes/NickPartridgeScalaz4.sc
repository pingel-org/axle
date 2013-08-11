
object NickPartridgeScalaz4 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // Make the monoid argument implicit

  trait Monoid[A] {
    def mappend(a1: A, a2: A): A
    def mzero: A
  }

  object IntMonoid extends Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  implicit val intMonoid = IntMonoid              //> intMonoid  : NickPartridgeScalaz4.IntMonoid.type = NickPartridgeScalaz4$$ano
                                                  //| nfun$main$1$IntMonoid$2$@58ecb281

  def sum[T](xs: List[T])(implicit m: Monoid[T]): T = xs.foldLeft(m.mzero)(m.mappend)
                                                  //> sum: [T](xs: List[T])(implicit m: NickPartridgeScalaz4.Monoid[T])T

  sum(List(1, 2, 3, 4))                           //> res0: Int = 10

}
object NickPartridgeScalaz3 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // Abstract on the type of the Monoid

  trait Monoid[A] {
    def mappend(a1: A, a2: A): A
    def mzero: A
  }

  object IntMonoid extends Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  def sum(xs: List[Int], m: Monoid[Int]): Int = xs.foldLeft(m.mzero)(m.mappend)
                                                  //> sum: (xs: List[Int], m: NickPartridgeScalaz3.Monoid[Int])Int


  sum(List(1, 2, 3, 4), IntMonoid)                //> res0: Int = 10


}
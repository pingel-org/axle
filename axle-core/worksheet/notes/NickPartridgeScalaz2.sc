object NickPartridgeScalaz2 {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // pull out a thing called a Monoid
  // And generalize our "sum" definition in terms of it

  object IntMonoid {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  def sum(xs: List[Int]): Int = xs.foldLeft(IntMonoid.mzero)(IntMonoid.mappend)
                                                  //> sum: (xs: List[Int])Int

  sum(List(1, 2, 3, 4))                           //> res0: Int = 10

}
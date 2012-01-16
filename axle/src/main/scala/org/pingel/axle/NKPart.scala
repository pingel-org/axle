
package org.pingel.axle

object NKPart {

  trait Monoid[A] {
    def mappend(a1: A, a2: A): A
    def mzero: A
  }

  object IntMonoid extends Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero = 0
  }

  object IntMultiplicationMonoid extends Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a * b
    def mzero = 1
  }

  object StringMonoid extends Monoid[String] {
    def mappend(a: String, b: String): String = a + b
    def mzero = ""
  }

  object BooleanMonoid extends Monoid[Boolean] {
    def mappend(a: Boolean, b: Boolean): Boolean = a && b
    def mzero = true
  }

//  object IntFunction1Monoid extends Monoid[Function1] {
//    def mappend(a: Function1[Int, Int], b: Function1[Int, Int]): Function1[Int, Int] = a.compose(b)
//    def mzero = (x: Int) => x
//  }

  def sumOld(xs: List[Int]): Int = xs.foldLeft(IntMonoid.mzero)( IntMonoid.mappend )

  def sum[T](xs: List[T])(implicit m: Monoid[T]): T = xs.foldLeft(m.mzero)(m.mappend)

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    // val foo: Int = sum(List(1, 2, 3, 4))(IntMonoid)
    p( sum(List(1, 2, 3, 4))(IntMonoid) )
    p( sum(List(1, 2, 3, 4))(IntMultiplicationMonoid) )
    p( sum(List("a", "b", "c"))(StringMonoid) )
    p( sum(List(false, true, false))(BooleanMonoid) )
    println
  }


}

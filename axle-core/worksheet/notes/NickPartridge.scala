
package org.pingel.category

/*
 * These are my notes from Nick Partidge's presentation to the Melbourne Scala Users Group
 * on "Deriving Scalaz"
 *
 * The original video is available at http://vimeo.com/10482466
 *
 * I've separated each of the steps in the derivation into separate StepX objects
 * 
 * The steps I've captured are all covered in the first 40 minutes of the video.
 * They cover the derivation of Monoid, FoldLeft, Identity, and MA "type classes".
 * This is the tehnique that Scalaz uses everywhere for ad-hoc polymorphism.
 *
 * Scalaz also provides Functor, Monad (Pure + Bind), and many other type classes.
 * Monoid is actually Semigroup + Zero
 * 
 */

 /*
  * We start with a "sum" function defined like so:
  * 
  *   def sum(xs: List[Int]): Int
  *
  * With the goal of generalizing to sum higher kinded *anything*:
  * 
  *   def sum[M[_], A](xs: M[A])(...): A
  *
  */

object Step1 {

  def sum(xs: List[Int]): Int = xs.foldLeft(0) { _ + _ }

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    p(sum(List(1, 2, 3, 4)))
    println
  }

}

object Step2 {

  // pull out a thing called a Monoid
  // And generalize our "sum" definition in terms of it

  object IntMonoid {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  def sum(xs: List[Int]): Int = xs.foldLeft(IntMonoid.mzero)(IntMonoid.mappend)

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    p(sum(List(1, 2, 3, 4)))
    println
  }

}

object Step3 {

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

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    p(sum(List(1, 2, 3, 4), IntMonoid))
    println
  }

}

object Step4 {

  // Make the monoid argument implicit

  trait Monoid[A] {
    def mappend(a1: A, a2: A): A
    def mzero: A
  }

  object IntMonoid extends Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  implicit val intMonoid = IntMonoid

  def sum[T](xs: List[T])(implicit m: Monoid[T]): T = xs.foldLeft(m.mzero)(m.mappend)

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    p(sum(List(1, 2, 3, 4)))
    println
  }

}

object Step5 {

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

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    p(sum(List(1, 2, 3, 4)))
    println
  }

}

object Step6 {

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

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    p(sum(List(1, 2, 3, 4)))
    p(sum(List("a", "b", "c")))
    println
  }

}

object Step7 {

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
  }

  def sum[T](xs: List[T])(implicit m: Monoid[T]): T = xs.foldLeft(m.mzero)(m.mappend)

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    p(sum(List(1, 2, 3, 4)))
    p(sum(List("a", "b", "c")))
    p(sum(List(1, 2, 3, 4))(multMonoid))
    println
  }

}

object Step8 {

  // recall the endgame:
  //   def sum[M[_], A](xs: M[A])(...): A
  //
  // generalize to sum higher-kinded anything

  // Start by defining a foldLeftList and redefining sum in terms of it

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
  }

  object FoldLeftList {
    def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B ) = xs.foldLeft(b)(f)
  }

  def sum[T](xs: List[T])(implicit m: Monoid[T]): T = FoldLeftList.foldLeft(xs, m.mzero, m.mappend)

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    p(sum(List(1, 2, 3, 4)))
    p(sum(List("a", "b", "c")))
    p(sum(List(1, 2, 3, 4))(multMonoid))
    println
  }

}

/*
 
object Step9 {

  // Pull out a FoldLeft typeclass, using the same technique that we used for Monoid
  // So that our sum method has to reference to List or Int

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
  }

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (A, B) => B ): B
  }

  object FoldLeft { // companion object to the FoldLeft trait
    implicit object FoldLeftList extends FoldLeft[List] {
      def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B ): B = xs.foldLeft(b)(f)
    }
  }

  def sum[M[_], T](xs: M[T])(implicit m: Monoid[T], fl: FoldLeft[M]): T = fl.foldLeft(xs, m.mzero, m.mappend)

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    p(sum(List(1, 2, 3, 4)))
    p(sum(List("a", "b", "c")))
    // p(sum(List(1, 2, 3, 4))(multMonoid)) // broken
    println
  }

}

object Step10 {

  // In order to make the explicit passing of multMonoid work
  // we also now need to supply a FoldLeft, which we can have Scala
  // find by using "implicitly", which we can think of as being implemented as:
  //
  //    def implicitly[T](implicit t: T): T = t

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
  }

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (A, B) => B ): B
  }

  object FoldLeft { 
    implicit object FoldLeftList extends FoldLeft[List] {
      def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B ): B = xs.foldLeft(b)(f)
    }
  }

  def sum[M[_], T](xs: M[T])(implicit m: Monoid[T], fl: FoldLeft[M]): T = fl.foldLeft(xs, m.mzero, m.mappend)

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println
    p(sum(List(1, 2, 3, 4)))
    p(sum(List("a", "b", "c")))
    p(sum(List(1, 2, 3, 4))(multMonoid, implicitly[FoldLeft[List]]))
    println
  }

}


object Step11 {

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
  }

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (A, B) => B ): B
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
  }

  def sum[M[_], T](xs: M[T])(implicit m: Monoid[T], fl: FoldLeft[M]): T = fl.foldLeft(xs, m.mzero, m.mappend)

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println

    p(sum(List(1, 2, 3, 4)))
    p(sum(List("a", "b", "c")))
    p(sum(List(1, 2, 3, 4))(multMonoid, implicitly[FoldLeft[List]]))

    p(3.plus(4))

    p(3 plus 4)

    println
  }

}


object Step12 {

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
  }

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (A, B) => B ): B
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
  }

  def sum[M[_], T](xs: M[T])(implicit m: Monoid[T], fl: FoldLeft[M]): T = fl.foldLeft(xs, m.mzero, m.mappend)

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println

    p(sum(List(1, 2, 3, 4)))
    p(sum(List("a", "b", "c")))
    p(sum(List(1, 2, 3, 4))(multMonoid, implicitly[FoldLeft[List]]))

    p(3 . |+| (4) ) // need spaces to help the parser if you want to use the dot notation
    p(3 |+| 4)

    println
  }

}


object Step13 {

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
  }

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (A, B) => B ): B
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
  }

  trait MA[M[_], A] {
    val value: M[A]
    
    def sum(implicit m: Monoid[A], fl: FoldLeft[M]): A = fl.foldLeft(value, m.mzero, m.mappend)
  }

  implicit def toMA[M[_], A](ma: M[A]): MA[M, A] = new MA[M, A] {
    val value = ma
  }

  def p(m: Any) { println("###> " + m) }

  def main(args: Array[String]) {
    println

    p(List(1, 2, 3, 4).sum)
    // p(List(1, 2, 3, 4).sum(multMonoid, implicitly[FoldLeft[List]])) // not finding implicit toMA

    p(3 |+| 4)

    println
  }

}


object Step14 {

  // Use Scalaz

  import scalaz._
  import Scalaz._

  //def multMonoid = new Monoid[Int] {
  //  def mappend(a: Int, b: Int): Int = a * b
  //  def mzero = 1
  //}

  def main(args: Array[String]) {
    val x = 3 |+| 4
    val y = List(1, 2) |+| List(3, 4)
  }

}

*/

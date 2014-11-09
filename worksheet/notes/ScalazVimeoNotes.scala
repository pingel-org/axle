package org.pingel.category

// http://vimeo.com/10482466
//
// Typeclasses in Scalaz
//
// Monoid - append, zero
// FoldLeft
// Functor - map
// Monad - bind, return
// many more...

class ScalazVimeoNotes {

trait FoldLeft[F[_]] {
	def foldLeft[A, B](c: F[A], b: B, f: (B, A) => B): B
}

trait Monoid[A] {
	def mappend(a1: A, a2: A): A
	def mzero: A
}

trait Identity[A] {
	val value: A
	def |+|(a2: A)(implicit m: Monoid[A]): A = m.mappend(value, a2)
}

trait MA[M[_], A] {
  
	val value: M[A]

	def masum(implicit m: Monoid[A], f1: FoldLeft[M]): A = f1.foldLeft(value, m.mzero, m.mappend)
}

object FoldLeft {
  implicit val FoldLeftList: FoldLeft[List] = new FoldLeft[List] {
	  def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B) = xs.foldLeft(b)(f)
  }  
}

object Monoid {

  implicit val IntMonoid: Monoid[Int] = new Monoid[Int] {
	def mappend(a: Int, b: Int): Int = a + b
	def mzero: Int = 0
  }

  implicit val StringMonoid: Monoid[String] = new Monoid[String] {
    def mappend(a: String, b: String): String = a + b
    def mzero: String = ""
  }
}

object Main {

  val multMonoid = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a * b
    def mzero: Int = 1
  }
  
  // def sum[M[_], T](xs: M[T])(implicit m: Monoid[T], f1: FoldLeft[M]): T = 
  //	  f1.foldLeft(xs, m.mzero, m.mappend) // xs.foldLeft(m.mzero)(m.mappend)
	  
  // def plus[T](a: T, b: T)(implicit m: Monoid[T]): T = m.mappend(a, b)
  
  def p(a: Any) { println("###> " + a) }
   
  def main(args: Array[String]) {

    implicit def toIdentity[A](a: A): Identity[A] = new Identity[A] {
    	val value = a
    }

    implicit def toMA[M[_], A](ma: M[A]): MA[M, A] = new MA[M, A] {
    	val value = ma
    }

    println
    p(List(1,2,3,4).masum)
    p(List("a", "b", "c").masum)
    //p(List(1,2,3,4) (multMonoid, implicitly[FoldLeft[List]]))
    p(3 |+| 4 )
    p(List(4,5,6).masum)
    println
    
  }
  
}

}

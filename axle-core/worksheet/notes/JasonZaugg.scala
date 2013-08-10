

// Notes from Jason Zaugg's introduction to Scalaz
//
// http://ontwik.com/scala/jason-zauggs-intro-to-scalaz/
//

object Step1 {

  // at 11:16

  trait Pure[P[_]] {
    def pure[A](a: => A): P[A]
  }

  implicit def Tuple1Pure = new Pure[Tuple1] {
    def pure[A](a: => A) = Tuple1(a)
  }

  implicitly[Pure[Tuple1]].pure(1)

  // 1.pure[Tuple1]
  
}

object Step2 {

  // around 23:32
  
  object Predef {

//    def implicitly[A](implicit a: A): A = a
//    implicit val s = ""
//    implicitly[String]

    def xList[A](as: List[A]): List[(A, A)] = {
      for {
	a1 <- as
	a2 <- as
      } yield (a1, a2)
    }

    def xOption[A](as: Option[A]): Option[(A, A)] = {
      for {
	a1 <- as
	a2 <- as
      } yield (a1, a2)
    }

    // List and Option share this behavior, but it isn't made explicit anywhere

    // Using Scalaz's Functor to capture this:

    import scalaz._
    import Scalaz._
    
    def x1[F[_], A](as: F[A])(implicit fa: Monad[F]): F[(A, A)] = {
      for {
        a1 <- as
        a2 <- as
      } yield (a1, a2)
    }

    def x2[F[_] : Monad, A](as: F[A]): F[(A, A)] = {
      for {
        a1 <- as
        a2 <- as
      } yield (a1, a2)
    }

  }

}

object WritingTypeClasses {

  // about 34:30 in

  case class Complex(real: Double, imaginary: Double)
  
  object Complex {

    import scalaz._
    import Scalaz._
    
    implicit val Show: Show[Complex] = showA

    implicit val Equal: Equal[Complex] = equalA
    
    implicit val Zero: Zero[Complex] = zero(Complex(0, 0))

    implicit val Semig: Semigroup[Complex] = semigroup {
      case (Complex(r1, i1), Complex(r2, i2)) => Complex(r1 + r2, i1 + i2)
    }

    implicit val OrderComplex: Order[Complex] = orderBy {
      case Complex(r, i) => (r, i)
    }
    
  }

  object ComplexTest {
 
    import scalaz._
    import Scalaz._

    val (c1, c2) = (Complex(0, 1), Complex(0, 2))
    val cs = Seq(c1, c2)
    val csString = cs.shows
    c1 === c2
    // c1 <= c2 // less than or equal to
    c1 |+| c2
    
    // val sum = cs.sum // Sigma

    // TraversableOnce#{min, max} are in the way
    //val (min, max) = (cs: MA[Seq, Complex]).pair.???.mapElements(_.min, _.max)
    
  }
  
}

object DataStructures {

  // about 51:53 in

  import scalaz._
  import Scalaz._
  
  val tree: Tree[Int] = 
    1.node(
        2.leaf,
        3.node(
            4.leaf)
        )

        
        
}

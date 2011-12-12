

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

    def implicitly[A](implicit a: A): A = a

    implicit val s = ""
    implicitly[String]

    def x[A](as: List[A]): List[(A, A)] = {
      for {
	a1 <- as
	a2 <- as
      } yield (a1, a2)
    }

    def x[A](as: Option[A]): Option[(A, A)] = {
      for {
	a1 <- as
	a2 <- as
      } yield (a1, a2)
    }

    // List and Option share this behavior, but it isn't made explicit anywhere

    // Using Scalaz's Functor to capture this:

    import scalaz._
    import Scalaz._

    def y[F[_], A](as: F[A])(implicit fa: scalaz.Functor[A]): F[(A, A)] = {
      for {
	a1 <- as
	a2 <- as
      } yield (a1, a2)
    }
    
  }

}



object Foo {

  trait Monad[M[_]] { // Note: was +M
    def unit[A](a: A): M[A]
    def bind[A, B](m: M[A])(f: A => M[B]): M[B]
  }

  implicit def monadicSyntax[M[_], A](m: M[A])(implicit tc: Monad[M]) = new {

    private val bindval = tc.bind(m) _

    def map[B](f: A => B) = bindval(f compose tc.unit)
    
    def flatMap[B](f: A => M[B]) = bindval(f)
  }

  implicit object MonadicOption extends Monad[Option] {
    def unit[A](a: A) = Some(a)

    def bind[A, B](opt: Option[A])(f: A => Option[B]) = opt flatMap f
  }
  
}

package axle.algebra

trait Monad[M[_]] {

  def bind[A, B](xs: M[A], f: A => M[B]): M[B]

  // unit = "apply"?
  def unit[A](a: A): M[A]

}

object Monad {

  import axle.algebra.toMA

  def checkLeftIdentity[M[_]: Monad, A, B](x: A, f: A => M[B]): Boolean = {
    val monad = implicitly[Monad[M]]
    monad.unit(x).bind(f) === f(x)
  }

  def checkRightIdentity[M[_]: Monad, A](ma: M[A]): Boolean = {
    val monad = implicitly[Monad[M]]
    ma.bind(monad.unit) === ma
  }

  def checkAssociativity[M[_]: Monad, A, B, C](ma: M[A], f: A => M[B], g: B => M[C]): Boolean = {
    val monad = implicitly[Monad[M]]
    ma.bind(f).bind(g) === ma.bind(x => f(x).bind(g))
  }

  implicit val listMonad = new Monad[List] {

    def bind[A, B](xs: List[A], f: A => List[B]): List[B] = xs.flatMap(f)

    def unit[A](a: A): List[A] = List(a)
  }

  implicit val optionMonad = new Monad[Option] {

    def bind[A, B](xs: Option[A], f: A => Option[B]): Option[B] = xs.flatMap(f)

    def unit[A](a: A): Option[A] = Some(a)
  }

  //  class Reader[A, B] extends Function1[A, B]
  //
  //  implicit val readerMonad = new Monad[Reader] {
  //    
  //  }

  // Writer
  // State
  // IO
  // ParserCombinator
  // Future
  // Behavior
  // Continuation
  // Giry

}
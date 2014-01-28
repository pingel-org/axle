package axle.algebra

import spire.algebra._
import spire.implicits._
import org.specs2.ScalaCheck
import org.specs2.mutable._
import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class MonadLawsSpec[M[_]: Monad, A: Eq: Arbitrary, B: Eq: Arbitrary, C: Eq: Arbitrary](name: String)(implicit eqma: Eq[M[A]], arbma: Arbitrary[M[A]], arbamb: Arbitrary[A => M[B]], eqmb: Eq[M[B]], arbbmc: Arbitrary[B => M[C]], eqmc: Eq[M[C]])
  extends Specification
  with ScalaCheck {

  s"$name obey left identity" ! prop { (m: M[A], x: A, f: A => M[B]) =>
    val monad = implicitly[Monad[M]]
    val lhs: M[B] = monad.bind(monad.unit(x), f)
    val rhs: M[B] = f(x)
    lhs === rhs
  }

  s"$name obey right identity" ! prop { (m: M[A]) =>
    val monad = implicitly[Monad[M]]
    val lhs: M[A] = monad.bind(m, monad.unit)
    val rhs: M[A] = m
    lhs === rhs
  }

  s"$name obey associativity" ! prop { (m: M[A], f: A => M[B], g: B => M[C]) =>
    val monad = implicitly[Monad[M]]
    val lhs: M[C] = monad.bind(monad.bind(m, f), g)
    val rhs: M[C] = monad.bind(m, (x: A) => monad.bind(f(x), g))
    lhs === rhs
  }

}

object Implicits {

  implicit def eqEitherIntInt[L: Eq, R: Eq]: Eq[Either[L, R]] = new Eq[Either[L, R]] {
    def eqv(x: Either[L, R], y: Either[L, R]): Boolean = (x, y) match {
      case (Left(xl), Left(yl)) if xl === yl => true
      case (Right(xr), Right(yr)) if xr === yr => true
      case _ => false
    }
  }

}

import Implicits._

class ListIntMonadLawsSpec extends MonadLawsSpec[List, Int, Int, Int]("List[Int] Monad")

class ListStringMonadLawsSpec extends MonadLawsSpec[List, String, String, String]("List[String] Monad")

class OptionIntMonadLawsSpec extends MonadLawsSpec[Option, Int, Int, Int]("Option[Int] Monad")

class EitherIntMonadLawsSpec extends MonadLawsSpec[({ type λ[α] = Either[Int, α] })#λ, Int, Int, Int]("Either[Int, Int] Monad")

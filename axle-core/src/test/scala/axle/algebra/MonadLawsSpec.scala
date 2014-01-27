package axle.algebra

import spire.algebra._
import spire.implicits._
import org.specs2.ScalaCheck
import org.specs2.mutable._
import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class MonadLawsSpec[M[_]: Monad, A: Eq: Arbitrary, B: Eq: Arbitrary, C: Eq: Arbitrary](name: String, monads: Seq[M[A]], fs: Seq[A => M[B]], gs: Seq[B => M[C]])(implicit eqm: Eq[M[B]])
  extends Specification with ScalaCheck {

  implicit def genMonad: Gen[M[_]] = Gen.oneOf(monads)

  implicit def arbMonad: Arbitrary[M[_]] = Arbitrary(genMonad)
  
/*
  s"$name obey left identity" ! prop { (m: M[_], x: A, f: A => M[B]) =>
    val monad = implicitly[Monad[M]]
    val lhs: M[B] = monad.bind(monad.unit(x), f)
    val rhs: M[B] = f(x)
    lhs === rhs
  }

  s"$name obey right identity" ! prop { (m: M[_]) =>
    val monad = implicitly[Monad[M]]
    val lhs: M[A] = monad.bind(m, (x: A) => monad.unit(x))
    val rhs: M[A] = monad
    lhs === rhs
  }

  s"$name obey associativity" ! prop { (m: M[A], f: A => M[B], g: B => M[C]) =>
    val monad = implicitly[Monad[M]]
    val lhs: M[C] = monad.bind(monad.bind(m, f), g)
    val rhs: M[C] = monad.bind(m, (x: A) => f(x).bind(g))
    lhs === rhs
  }
*/
  //  def checkLeftIdentity[M[_]: Monad, A, B](x: A, f: A => M[B]): Boolean =
  //    implicitly[Monad[M]].unit(x).bind(f) === f(x)
  //  def checkRightIdentity[M[_]: Monad, A](ma: M[A]): Boolean =
  //    ma.bind(implicitly[Monad[M]].unit) === ma
  //  def checkAssociativity[M[_]: Monad, A, B, C](ma: M[A], f: A => M[B], g: B => M[C]): Boolean =
  //    ma.bind(f).bind(g) === ma.bind(x => f(x).bind(g))

}

//class IntListMonadLawsSpec extends MonadLawsSpec(
//  "List[Int] Monad", List(
//    implicitly[Monad[List[Int]]]))

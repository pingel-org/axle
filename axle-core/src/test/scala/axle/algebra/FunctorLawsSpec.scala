package axle.algebra

import spire.algebra._
import spire.implicits._
import org.specs2.ScalaCheck
import org.specs2.mutable._
import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class FunctorLawsSpec[F[_]: Functor, A: Eq: Arbitrary, B: Eq: Arbitrary, C: Eq: Arbitrary](name: String)(implicit eqFa: Eq[F[A]], arbFa: Arbitrary[F[A]], eqFc: Eq[F[C]])
  extends Specification
  with ScalaCheck {

  s"$name obey identity" ! prop { (xs: F[A]) =>
    val functor = implicitly[Functor[F]]
    val lhs: F[A] = functor.fmap[A, A](xs, identity)
    val rhs: F[A] = identity(xs)
    lhs === rhs
  }

  s"$name obey composition" ! prop { (xs: F[A], f: A => B, g: B => C) =>
    val functor = implicitly[Functor[F]]
    val lhs: F[C] = functor.fmap[B, C](functor.fmap[A, B](xs, f), g)
    val rhs: F[C] = functor.fmap[A, C](xs, g compose f)
    lhs === rhs
  }

}

object ArbitraryStuff {

  // TODO
  //implicit val arbListInt: Arbitrary[List[Int]] = Arbitrary(Gen.oneOf(List(List(1, 2, 3), List(4, 5, 6))))

}

import ArbitraryStuff._

class ListIntFunctorLawsSpec extends FunctorLawsSpec[List, Int, Int, Int]("List[Int] Functor")


//      val getLine = () => "test data".toList // override the real getLine
//      val doit = ((xs: () => List[Char]) => intersperse('-')(xs().map(_.toUpper).reverse))
//      val didit = doit(getLine).mkString("")
//      val f: Function1[List[Char], List[Char]] = (intersperse('-') _) compose (reverse[Char] _) compose ((chars: List[Char]) => chars.map(_.toUpper))
//
//      f(getLine()).mkString("")
//
//      // getLine.map(intersperse "-" compose reverse compose map toUpper)
//      // getLine.map((cs: List[Char]) => f(cs))
//
//      List(1, 2, 3).map(replicate[Int](3) _)
//      Some(4).map(replicate[Int](3) _)
//      //Right("blah").asInstanceOf[Either[String, String]].map(replicate[String](3) _)
//      //Left("foo").asInstanceOf[Either[String, String]].map(replicate[String](3) _)
//
//      // Functions as Functors
//      //{ (_: Int) + 100 } map { _ * 3 } apply (1)
//
//      { (_: Int) * 3 } compose { (_: Int) + 100 } apply (1)
//
//      List(1, 2, 3, 4).map(replicate[Int](3) _)
//
//      Some(4).map(replicate[Int](3) _)
//
//      // Right("blah").asInstanceOf[Either[Nothing, String]].map(replicate[String](3) _)
//
//      None.asInstanceOf[Option[Int]].map(replicate[Int](3) _)
//
//      // Left("foo").asInstanceOf[Either[String, String]].map(replicate[String](3) _)
//
//      List(1, 2, 3, 4).map({ (x: Int) => x + 1 })
//
//      "hello".toList.map({ (c: Char) => c.toUpper })
//
//      List(1, 2, 3, 4).map(replicate[Int](3) _)
//
//      Some(4).map(replicate[Int](3) _)
//
//      // { () => 1 }.map(replicate[Int](3) _)
//
//      // Left(5).asInstanceOf[Either[Int, Int]].map(replicate(3) _)
//
//      // Right(6).asInstanceOf[Either[Int, Int]].map(replicate[Any](3) _)
//
//      // { (_: Int) + 100 }.map({ (_: Int) * 3 }).apply(1)
//
//      // Law 1: fmap id = id
//
////      functorLaw1a(None.asInstanceOf[Option[Int]])
////      functorLaw1a(Some(4).asInstanceOf[Option[Int]])
////      functorLaw1a(List(1, 2, 3, 4))
//      // functorLaw1b(Right("blah").asInstanceOf[Either[String, String]])
//      // functorLaw1b(Left("foo").asInstanceOf[Either[String, String]])
//      // functorLaw1a({ (_: Int) + 100 })
//
//      // Law 2: fmap (f . g) = fmap f . fmap g
//      // Law 2 restated: forall x: fmap (f . g) x = fmap f (fmap g x)
//
//      val f = { (x: Int) => x + 1 }
//      val g = { (x: Int) => x * 10 }
//
//      //      functorLaw2a(f, g, Some(4).asInstanceOf[Option[Int]])
//      //      functorLaw2a(f, g, List(1, 2, 3, 4))
//      //      functorLaw2b(f, g, Right(5).asInstanceOf[Either[Nothing, Int]])
//      //      functorLaw2b(f, g, Left(6).asInstanceOf[Either[Int, Nothing]])
//      //      functorLaw2b(f, g, { (_: Int) + 100 })


package axle

import org.specs2.mutable._
import scalaz._
import Scalaz._

class AxleSpec extends Specification {

  // This file isn't so much an "Axle" spec as it is a list of expressions found
  // in "Learn You a Haskell For Great Good" translated to Scala w/Scalaz
  
  def functorLaw1a[M[_]: Functor, A](functor: M[A]): Boolean = functor.map(id[A]) == id[M[A]](functor)

  //  def functorLaw1b[M[_, _]: Functor, A, B](functor: M[A, B]): Boolean = {
  //    val lhs = functor.map(id[B])
  //    val rhs = id[M[A, B]](functor)
  //    println("lhs = " + lhs)
  //    println("rhs = " + rhs)
  //    val ok = lhs == rhs
  //    println("equal? " + ok)
  //    ok
  //  }

  "Functor Redux" should {
    "work" in {

      val getLine = () => "test data".toList // override the real getLine
      val doit = ((xs: () => List[Char]) => intersperse('-')(xs().map(_.toUpper).reverse))
      val didit = doit(getLine).mkString("")
      val f: Function1[List[Char], List[Char]] = (intersperse('-') _) compose (reverse[Char] _) compose ((chars: List[Char]) => chars.map(_.toUpper))

      f(getLine()).mkString("")

      // getLine.map(intersperse "-" compose reverse compose map toUpper) 
      getLine.map((cs: List[Char]) => f(cs))

      List(1, 2, 3).map(replicate[Int](3) _)
      Some(4).map(replicate[Int](3) _)
      Right("blah").asInstanceOf[Either[String, String]].map(replicate[String](3) _)
      Left("foo").asInstanceOf[Either[String, String]].map(replicate[String](3) _)

      // Functions as Functors
      { (_: Int) + 100 } map { _ * 3 } apply (1)

      { (_: Int) * 3 } compose { (_: Int) + 100 } apply (1)

      List(1, 2, 3, 4).map(replicate[Int](3) _)

      Some(4).map(replicate[Int](3) _)

      Right("blah").asInstanceOf[Either[Nothing, String]].map(replicate[String](3) _)

      None.asInstanceOf[Option[Int]].map(replicate[Int](3) _)

      Left("foo").asInstanceOf[Either[String, String]].map(replicate[String](3) _)

      List(1, 2, 3, 4).map({ (x: Int) => x + 1 })

      "hello".toList.map({ (c: Char) => c.toUpper })

      List(1, 2, 3, 4).map(replicate[Int](3) _)

      Some(4).map(replicate[Int](3) _)

      { () => 1 }.map(replicate[Int](3) _)

      Left(5).asInstanceOf[Either[Int, Int]].map(replicate(3) _)

      Right(6).asInstanceOf[Either[Int, Int]].map(replicate[Any](3) _)

      { (_: Int) + 100 }.map({ (_: Int) * 3 }).apply(1)

      1 must be equalTo (1)
    }

  }

  "Functor Laws" should {
    "work" in {
      // Law 1: fmap id = id

      functorLaw1a(None.asInstanceOf[Option[Int]])
      functorLaw1a(Some(4).asInstanceOf[Option[Int]])
      functorLaw1a(List(1, 2, 3, 4))
      // functorLaw1b(Right("blah").asInstanceOf[Either[String, String]])
      // functorLaw1b(Left("foo").asInstanceOf[Either[String, String]])
      // functorLaw1a({ (_: Int) + 100 })

      // Law 2: fmap (f . g) = fmap f . fmap g
      // Law 2 restated: forall x: fmap (f . g) x = fmap f (fmap g x)

      val f = { (x: Int) => x + 1 }
      val g = { (x: Int) => x * 10 }

      //      functorLaw2a(f, g, Some(4).asInstanceOf[Option[Int]])
      //      functorLaw2a(f, g, List(1, 2, 3, 4))
      //      functorLaw2b(f, g, Right(5).asInstanceOf[Either[Nothing, Int]])
      //      functorLaw2b(f, g, Left(6).asInstanceOf[Either[Int, Nothing]])
      //      functorLaw2b(f, g, { (_: Int) + 100 })

      1 must be equalTo (1)
    }
  }

  "Applicatives" should {
    "work" in {
      /*
    val p2 = pure2( { (x: Int) => x + 3 } )

    val somePlus3 = Some( { (x: Int) => x + 3 } )

    val a1 = somePlus3 <*> Some(9)
    
    val a2 = pure2( { (x: Int) => x + 3 } ) <*> Some(10)

    val a3 = pure2( { (x: Int) => x + 3 } ) <*> Some(9)
    
    val a4 = Some( { (s: String) => s ++ "hahah" } ) <*> None

    val a5 = None <*> Some("woot")

    val intAdd = { (x: Int, y: Int) => x + y }
    val stringAppend = { (s1: String, s2: String) => s1 ++ s2 }

    val p4 = pure3( intAdd )

    val a6 = ( pure( intAdd ) <*> Some(3) ) <*> Some(5)
    val a7 = ( pure( intAdd ) <*> Some(3) ) <*> None
    val a8 = ( pure( intAdd ) <*> None    ) <*> Some(5)
*/
      1 must be equalTo (1)
    }
  }

}

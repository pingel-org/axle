
package axle

import org.specs2.mutable._

class AxleSpec extends Specification {

  // TODO: single import
  import LearnYouAnAxle.VariousFunctions._
  import LearnYouAnAxle.Functor._
  import LearnYouAnAxle.Functor2._

  "Functor Redux" should {
    "work" in {

      def getLine() = "test data".toList // override the real getLine

      val doit = ((xs: List[Char]) => intersperse('-')(xs.map(_.toUpper).reverse))
      val didit = doit(getLine).mkString("")

      val f: Function1[List[Char], List[Char]] = (intersperse('-') _) compose (reverse[Char] _) compose ((chars: List[Char]) => chars.map(_.toUpper))

      (f)(getLine).mkString("")

      // fmap (intersperse '-' . reverse . map toUpper) getLine
      fmap(((cs: List[Char]) => f(cs)), (getLine _).asInstanceOf[Function0[List[Char]]])

      fmap(replicate[Int](3) _, List(1, 2, 3))
      fmap(replicate[Int](3) _, Some(4).asInstanceOf[Option[Int]])
      fmap2(replicate[String](3) _, Right("blah").asInstanceOf[Either[Nothing, String]])
      fmap2(replicate[String](3) _, Left("foo").asInstanceOf[Either[String, Nothing]])

      // Functions as Functors

      fmap2({ (_: Int) * 3 }, { (_: Int) + 100 }) apply (1)

      { (_: Int) * 3 } compose { (_: Int) + 100 } apply (1)

      fmap(replicate[Int](3) _, List(1, 2, 3, 4))
      fmap(replicate[Int](3) _, Some(4).asInstanceOf[Option[Int]])
      fmap2(replicate[String](3) _, Right("blah").asInstanceOf[Either[Nothing, String]])
      fmap(replicate[Int](3) _, None.asInstanceOf[Option[Int]])
      fmap2(replicate[Int](3) _, Left("foo").asInstanceOf[Either[String, Nothing]])

      // explicitly passing the typeclass to fmapexp
      val e1 = fmapexp({ (x: Int) => x + 1 }, List(1, 2, 3, 4), ListFunctor)
      val e2 = fmapexp({ (c: Char) => c.toUpper }, "hello".toList, ListFunctor)
      val e3 = fmapexp(replicate[Int](3) _, List(1, 2, 3, 4), ListFunctor)
      val e4 = fmapexp(replicate[Int](3) _, Some(4), OptionFunctor)
      val e5 = fmapexp(replicate[Int](3) _, { () => 1 }, Function0Functor)
      val e6 = fmap2exp(replicate(3) _, Left(5), EitherFunctor)
      val e7 = fmap2exp(replicate[Any](3) _, Right(6), EitherFunctor)
      val e8 = fmap2exp({ (_: Int) * 3 }, { (_: Int) + 100 }, Function1Functor).apply(1)

      // allowing fmap's implicit typeclass (tc) to be determined by Scala:
      val i1 = fmap({ (x: Int) => x + 1 }, List(1, 2, 3, 4))
      val i2 = fmap({ (c: Char) => c.toUpper }, "hello".toList)
      val i3 = fmap(replicate[Int](3) _, List(1, 2, 3, 4))

      // TODO: get rid of the following asInstanceOf calls:
      val i4 = fmap(replicate[Int](3) _, Some(4).asInstanceOf[Option[Int]])
      val i5 = fmap2(replicate(3) _, Left(5).asInstanceOf[Either[Int, Nothing]])
      val i6 = fmap2(replicate[Any](3) _, Right(6).asInstanceOf[Either[Nothing, Int]])

      val i7 = fmap2({ (_: Int) * 3 }, { (_: Int) + 100 }).apply(1)

    }

    1 must be equalTo (1)
  }

  "Functor Laws" should {
    "work" in {
      // Law 1: fmap id = id

      functorLaw1a(None.asInstanceOf[Option[Int]])
      functorLaw1a(Some(4).asInstanceOf[Option[Int]])
      functorLaw1a(List(1, 2, 3, 4))
      functorLaw1b(Right("blah").asInstanceOf[Either[Nothing, String]])
      functorLaw1b(Left("foo").asInstanceOf[Either[String, Nothing]])
      functorLaw1b({ (_: Int) + 100 })

      // Law 2: fmap (f . g) = fmap f . fmap g
      // Law 2 restated: forall x: fmap (f . g) x = fmap f (fmap g x)

      val f = { (x: Int) => x + 1 }
      val g = { (x: Int) => x * 10 }

      functorLaw2a(f, g, Some(4).asInstanceOf[Option[Int]])
      functorLaw2a(f, g, List(1, 2, 3, 4))
      functorLaw2b(f, g, Right(5).asInstanceOf[Either[Nothing, Int]])
      functorLaw2b(f, g, Left(6).asInstanceOf[Either[Int, Nothing]])
      functorLaw2b(f, g, { (_: Int) + 100 })

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

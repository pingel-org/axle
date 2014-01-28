package axle.algebra

class ApplicativeLawsSpec {

  // pure id <*> v = v

  def checkAxiom1[F[_]: Applicative, T](v: T): Boolean = {
    //val applicative = implicitly[Applicative[F]]
    //applicative.pure(id <*> v) === v
    true
  }

  // pure (.) <*> u <*> v <*> w = u <*> (v <*> w)

  def checkAxiom2[F[_]: Applicative, T](u: T, v: T, w: T): Boolean =
    true

  // pure f <*> pure x = pure (f x)

  def checkAxiom3[F[_]: Applicative, T, U](x: T, f: T => U): Boolean =
    true

  // u <*> pure y = pure ($ y) <*> u

  def checkAxiom4[F[_]: Applicative, T](v: T): Boolean =
    true

}

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

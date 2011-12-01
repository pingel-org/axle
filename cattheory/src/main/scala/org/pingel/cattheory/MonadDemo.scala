
package org.pingel.cattheory

object CategoryTheory {

/*

  trait Semigroup[A] {
    // def append ? TODO
  }

  trait Monoid[A] extends Semigroup[A] {
    // def zero TODO
  }

  trait Monad[A] extends Monoid[A] { // TODO: this A masks method signatures A's:

    // def unit TODO

    def myFlatten(m: Monad[Monad[A]]): Monad[A]

    def myBind[A, B](m: Monad[A])(f: A => Monad[B]): Monad[B]
  }
*/

}

object Monads {

  import CategoryTheory._

/*
  case class MonadicOption[A](opt: Option[A]) extends Monad[A] {

    // TODO: inherit Monad's signatures

    def myFlatten[A](m: Option[Option[A]]): Option[A] = m match {
      case Some(inner) => inner
      case None => None
    }

    def myBind[A, B](m: Option[A])(f: A => Option[B]): Option[B] = m.flatMap(f)
  }

  implicit def enMonadOption[A](opt: Option[A]) = MonadicOption(opt)

  case class MonadicList[A](list: List[A]) extends Monad[A] {

    // TODO: inherit Monad's signature

    def myFlatten[A](m: List[List[A]]): List[A] = m.foldLeft(List[A]())({ (x: List[A], y: List[A]) => x ++ y })

    def myBind[T, B](m: List[T])(f: T => List[B]): List[B] = m.flatMap(f)
  }

  implicit def enMonadList[A](list: List[A]) = MonadicList(list)
*/

  def axioms(): Unit = {

    // TODO: make this stuff work for both List and Option Monads

/*
    def identity1[A, B](a: A, f: A => M[B]) {
      val ma: M[A] = tc.unit(a)
      assert(tc.bind(ma)(f) == f(a))
    }

    forAll { (x, f) => identity1(a, f) }        // sort-of ScalaCheck

    def identity2[A](ma: M[A]) {
      assert(tc.bind(ma)(tc.unit) == ma)
    }

    forAll { m => identity2(m) }
    
    def associativity[A, B, C](m: M[A], f: A => M[B], g: B => M[C]) {
      val mf: M[B] = tc.bind(m)(f)
      val mg: M[C] = tc.bind(mf)(g)
 
      val mg2: M[C] = tc.bind(m) { 
	a => tc.bind(f(a))(g)
      }
      assert(mg == mg2)
    }

    forAll { (m, f, g) => associativity(m, f, g) }
*/

  }

}

object MonadDemo {

/*
  import CategoryTheory._
  import Monads._

  {
    // simple demo:
    val m  : Monad[Int] = Some(4)
    val ob : Monad[Int] = m.myBind(m)({ x: Int => Some(x) })
    val ob2: Monad[Int] = m.myBind(m)({ x: Int => List(x) }) // TODO: disallow this
  }

  {
    // define a notion of "flatten" for both Option and List
    val o0 = None
    val x0 = o0.myFlatten(o0)

    val o1 = Some(Some("two"))
    val x1 = o1.myFlatten(o1)

    val o2 = Some(None)
    val x2 = o2.myFlatten(o2)

    val o3 = None
    val x3 = o3.myFlatten(o3)
  }

  {
    def si(s: String) : Option[Int] = s match {
      case "one" => Some(1)
      case "two" => Some(2)
      case _     => None
    }

    {
      val m = Some("two")

      // the hard way:
      val hardWay: Option[Int] = m.map( s => si(s) ).flatMap( x => x )

      // the hard way using myBind:

      val hwi: Option[Option[Int]] = m.map( s => si(s) )
      val hardWayMine = hwi.myBind(hwi)( x => x )

      // the right way

      val fmsi: Option[Int] = m.flatMap( s => si(s) )

      val fmsiMine: Option[Int] = m.myBind(m)( s => si(s) )
    }

    {
      val m: Option[Option[String]] = Some(Some("two"))

      val flat: Option[String] = m.flatMap( x => x ) // same as flatten
      val flatMine: Option[String] = m.myBind(m)( x => x ) 

      // Applying the "si" function shouldn't happen direction on such a structure,
      // but for pedagogical purposes, it is useful to show how to deal with it
     
      // the hard way using scala built-in:
      val naive: Option[Int] = m.map({ outer => outer.map({ inner => si(inner) }) }).flatMap( x => x ).flatMap( y => y )

      // the hard way using myBind:
      val mi = m.map({ outer => { outer.map({ inner => si(inner) }) } })
      val mif = mi.myBind(mi)( x => x )
      val naiveMine: Option[Int] = mif.myBind(mif)( x => x )
    }

    {
      val m = Some(None)
      val flat: Option[Nothing] = m.flatMap( x => x )
      val flatMine: Option[Nothing] = m.myBind(m)({ x => x })
    }

    {
      val m = None

      val fm: Option[Int] = m.flatMap({ x => si(x) })
      
      val fmon2: Option[Int] = m.myBind(m)({ x => si(x) })
    }

    {
      object config {
	def fetchParam(name: String): Option[String] = Some("two")
      }

      // Using Scala's built-in flatMap:
      val result2: Option[Int] = config.fetchParam("MaxThreads").flatMap({ s => si(s) })

      val m: Option[String] = config.fetchParam("MaxThreads")
      val result: Option[Int] = m.myBind(m)({ x => si(x) })
    }
    
  }
  

  {
    def si(s: String) : List[Int] = s match {
      case "one" => List(1)
      case "two" => List(2)
      case _     => List[Int]()
    }

    {
      val m  : Monad[Int] = List(1, 2, 3)
      val lb : Monad[Int] = m.myBind(m)({ x: Int => List(x) })
      val lb2: Monad[Int] = m.myBind(m)({ x: Int => Some(x) }) // TODO: disallow this
    }

    {
      val m = List("two")
      
      val foo: List[Int] = m.flatMap( x => si(x) )

      val bar: List[Int] = m.myBind(m)( x => si(x) )
    }

    {
      val m = List(List("two"))

      val flat: List[String] = m.flatMap( x => x ) // same as flatten
      val flatMine: List[String] = m.myBind(m)( x => x ) 

      // Applying the "si" function shouldn't happen direction on such a structure,
      // but for pedagogical purposes, it is useful to show how to deal with it
     
      // the hard way using scala built-in:
      val naive: List[Int] = m.map({ outer => outer.map({ inner => si(inner) }) }).flatMap( x => x ).flatMap( y => y )

      // the hard way using myBind:
      val mi = m.map({ outer => { outer.map({ inner => si(inner) }) } })
      val mif = mi.myBind(mi)( x => x )
      val naiveMine: List[Int] = mif.myBind(mif)( x => x )
    }

    {
      val m = List(Nil)

      val flat: List[Nothing] = m.flatMap( x => x )

      val flatMine: List[Nothing] = m.myBind(m)({ x => x })
    }

    {
      val m = Nil // List()

      val fm: List[Int] = m.flatMap({ x => si(x) })
      
      val fmon2: List[Int] = m.myBind(m)({ x => si(x) })
    }

  }

*/

}



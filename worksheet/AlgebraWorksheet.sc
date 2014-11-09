object AlgebraWorksheet {

  println("axle.algebra worksheet")               //> axle.algebra worksheet

  import axle.algebra._

  import spire.math._
  import spire.implicits._
  import spire.algebra._
  import Additive._

  // 2 |+| 2
 
  // 1.0 |+| 2.0

  List(1, 2, 3) |+| List(3, 4, 5)                 //> res0: List[Int] = List(1, 2, 3, 3, 4, 5)

  // (1, 2) |+| (1, 2)
  
  // (3, "a") |+| (1, "b")

  implicit val addIntMonoid = new Monoid[Int] {
    def id() = 0
    def op(x: Int, y: Int) = x + y
  }                                               //> addIntMonoid  : spire.algebra.Monoid[Int]{def id(): Int} = AlgebraWorksheet$
                                                  //| $anonfun$main$1$$anon$1@5caf993e

  axle.algebra.Monoid.checkLeftZero(4)            //> res1: Boolean = true
  axle.algebra.Monoid.checkRightZero(4)           //> res2: Boolean = true
  axle.algebra.Monoid.checkAssociativity(1, 2, 3) //> res3: Boolean = true

/*

  List(1, 2, 3).fmap(_ |+| 3)

  List(1, 2, 3).fmap(_ |+| 1)
  Option(1).fmap(_ |+| 1)

  val f1 = (x: Int) => x |+| 1
  val f2 = (x: Int) => x * 10
  // f1.fmap(f2)

  Functor.checkIdentity(List(1, 2, 3))

  Functor.checkComposition(List(1, 2, 3), (x: Int) => x |+| 1, (y: Int) => y * 10)

  Option(4).bind(x => Some(x |+| 1))

  List(1, 2, 3).bind(x => List(x))

  // http://stackoverflow.com/questions/10935858/scala-dynamic-field-named-x
  // Right(3).asInstanceOf[Either[Int, Int]].bind(x => Right(x |+| 1).asInstanceOf[Either[Int, Int]])

  Monad.checkLeftIdentity(4, (x: Int) => Option(x |+| 1))

  Monad.checkRightIdentity(Option(4))

  Monad.checkAssociativity(Option(4), (x: Int) => Option(x |+| 1), (y: Int) => Option(y * 10))

  */

}
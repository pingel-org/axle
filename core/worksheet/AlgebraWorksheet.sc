object AlgebraWorksheet {

  println("axle.algebra worksheet")               //> axle.algebra worksheet

  import axle.algebra._

  //import Monoid._
  // implicit val im = intMonoid
  
  4 |+| 1                                         //> res0: Int = 5

  Monoid.checkLeftZero(4)                         //> res1: Boolean = true
  Monoid.checkRightZero(4)                        //> res2: Boolean = true
  Monoid.checkAssociativity(1, 2, 3)              //> res3: Boolean = true
  
  List(1, 2, 3).fmap(_ |+| 3)                     //> res4: List[Int] = List(4, 5, 6)
  Functor.checkIdentity(List(1, 2, 3))            //> res5: Boolean = true

  Functor.checkComposition(List(1, 2, 3), (x: Int) => x |+| 1, (y: Int) => y * 10)
                                                  //> res6: Boolean = true

  Option(4).bind(x => Some(x |+| 1))              //> res7: Option[Int] = Some(5)

  Monad.checkLeftIdentity(4, (x: Int) => Option(x |+| 1))
                                                  //> res8: Boolean = true

  Monad.checkRightIdentity(Option(4))             //> res9: Boolean = true

  Monad.checkAssociativity(Option(4), (x: Int) => Option(x |+| 1), (y: Int) => Option(y * 10))
                                                  //> res10: Boolean = true

}
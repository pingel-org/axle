object AlgebraWorksheet {

  println("axle.algebra worksheet")               //> axle.algebra worksheet

  import axle.algebra._
  2 |+| 2                                         //> res0: Int = 4
  
  1.0 |+| 2.0                                     //> res1: Double = 3.0

  List(1, 2, 3) |+| List(3, 4, 5)                 //> res2: List[Int] = List(1, 2, 3, 3, 4, 5)
 
  (1, 2) |+| (1, 2)                               //> res3: (Int, Int) = (2,4)
  
  (3, "a") |+| (1, "b")                           //> res4: (Int, java.lang.String) = (4,ab)

  Monoid.checkLeftZero(4)                         //> res5: Boolean = true
  Monoid.checkRightZero(4)                        //> res6: Boolean = true
  Monoid.checkAssociativity(1, 2, 3)              //> res7: Boolean = true

  List(1, 2, 3).fmap(_ |+| 3)                     //> res8: List[Int] = List(4, 5, 6)

  List(1, 2, 3).fmap(_ |+| 1)                     //> res9: List[Int] = List(2, 3, 4)
  Option(4).fmap(_ |+| 1)                         //> res10: Option[Int] = Some(5)

  val f1 = (x: Int) => x |+| 1                    //> f1  : Int => Int = <function1>
  val f2 = (x: Int) => x * 10                     //> f2  : Int => Int = <function1>
  // f1.fmap(f2)

  Functor.checkIdentity(List(1, 2, 3))            //> res11: Boolean = true

  Functor.checkComposition(List(1, 2, 3), (x: Int) => x |+| 1, (y: Int) => y * 10)
                                                  //> res12: Boolean = true

  Option(4).bind(x => Some(x |+| 1))              //> res13: Option[Int] = Some(5)

  List(1, 2, 3).bind(x => List(x))                //> res14: List[Int] = List(1, 2, 3)

  // http://stackoverflow.com/questions/10935858/scala-dynamic-field-named-x
  // Right(3).asInstanceOf[Either[Int, Int]].bind(x => Right(x |+| 1).asInstanceOf[Either[Int, Int]])

  Monad.checkLeftIdentity(4, (x: Int) => Option(x |+| 1))
                                                  //> res15: Boolean = true

  Monad.checkRightIdentity(Option(4))             //> res16: Boolean = true

  Monad.checkAssociativity(Option(4), (x: Int) => Option(x |+| 1), (y: Int) => Option(y * 10))
                                                  //> res17: Boolean = true

}
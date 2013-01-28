object AlgebraWorksheet {

  println("axle.algebra worksheet")               //> axle.algebra worksheet

  import axle.algebra._
  
  toIdent("foo") |+| "bar"                        //> res0: java.lang.String = foobar
  
  List(1, 2, 3).mapp(_ |+| 3)                     //> res1: List[Int] = List(4, 5, 6)

  Option(4).bind(x => Some(x |+| 1))              //> res2: Option[Int] = Some(5)

}
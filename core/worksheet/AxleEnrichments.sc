
object AxleEnrichments {

  println("Axle Enrichments")                     //> Axle Enrichments

  import axle._

  true and false                                  //> res0: Boolean = false

  // true ∧ false

  true and true                                   //> res1: Boolean = true

  // true ∧ true

  true or false                                   //> res2: Boolean = true

  // true ∨ false

  true implies false                              //> res3: Boolean = false

  true implies true                               //> res4: Boolean = true

  // Set(1, 2, 3) ∃ (_ % 2 == 0)

  // List(1, 2, 3) ∀ (_ % 2 == 0)

  // (1 to 10) Π((i: Int) => { () => i * 3 }) // TODO: clean this up

  // (1 to 10) Σ (_ * 2)

  List(1, 2, 3).doubles                           //> res5: Seq[(Int, Int)] = List((1,2), (1,3), (2,1), (2,3), (3,1), (3,2))

  Set(1, 2, 3).triples                            //> res6: Seq[(Int, Int, Int)] = List((1,2,3), (1,3,2), (2,1,3), (2,3,1), (3,1,2
                                                  //| ), (3,2,1))

  // (List(1, 2, 3) ⨯ List(4, 5, 6)).toList

  Vector("a", "x", "l", "e").permutations(2)      //> res7: axle.Permutations[java.lang.String] = Permutations(List(a, x), List(a,
                                                  //|  l), List(a, e), List(x, a), List(x, l), List(x, e), List(l, a), List(l, x),
                                                  //|  List(l, e), List(e, a), List(e, x), List(e, l))

  Vector("a", "x", "l", "e").combinations(2).toList
                                                  //> res8: List[scala.collection.immutable.Vector[java.lang.String]] = List(Vecto
                                                  //| r(a, x), Vector(a, l), Vector(a, e), Vector(x, l), Vector(x, e), Vector(l, e
                                                  //| ))

}
object CardHandRanking {

  println("Hand Ranking Demo")                    //> Hand Ranking Demo

  import axle.game.poker._
  import Implicits._
  
  val available = Deck().cards.take(7)            //> available  : IndexedSeq[axle.game.poker.Card] = Vector(2?, Q?, J?, 10?, A?, 
                                                  //| J?, 4?)
  val gs = available.combinations(5).map(Hand(_)).toList.sorted
                                                  //> gs  : List[axle.game.poker.Hand] = List(Hand(Vector(2?, Q?, 10?, J?, 4?)), H
                                                  //| and(Vector(2?, Q?, J?, 10?, 4?)), Hand(Vector(2?, 10?, A?, J?, 4?)), Hand(Ve
                                                  //| ctor(2?, J?, 10?, A?, 4?)), Hand(Vector(2?, Q?, 10?, A?, 4?)), Hand(Vector(2
                                                  //| ?, Q?, A?, J?, 4?)), Hand(Vector(2?, Q?, J?, A?, 4?)), Hand(Vector(2?, Q?, 1
                                                  //| 0?, A?, J?)), Hand(Vector(2?, Q?, J?, 10?, A?)), Hand(Vector(Q?, 10?, A?, J?
                                                  //| , 4?)), Hand(Vector(Q?, J?, 10?, A?, 4?)), Hand(Vector(2?, J?, 10?, J?, 4?))
                                                  //| , Hand(Vector(2?, Q?, J?, J?, 4?)), Hand(Vector(2?, Q?, J?, 10?, J?)), Hand(
                                                  //| Vector(Q?, J?, 10?, J?, 4?)), Hand(Vector(2?, J?, A?, J?, 4?)), Hand(Vector(
                                                  //| 2?, J?, 10?, A?, J?)), Hand(Vector(J?, 10?, A?, J?, 4?)), Hand(Vector(2?, Q?
                                                  //| , J?, A?, J?)), Hand(Vector(Q?, J?, A?, J?, 4?)), Hand(Vector(Q?, J?, 10?, A
                                                  //| ?, J?)))
  for (g <- gs) { println(g) }                    //> Hand(Vector(2?, Q?, 10?, J?, 4?))
                                                  //| Hand(Vector(2?, Q?, J?, 10?, 4?))
                                                  //| Hand(Vector(2?, 10?, A?, J?, 4?))
                                                  //| Hand(Vector(2?, J?, 10?, A?, 4?))
                                                  //| Hand(Vector(2?, Q?, 10?, A?, 4?))
                                                  //| Hand(Vector(2?, Q?, A?, J?, 4?))
                                                  //| Hand(Vector(2?, Q?, J?, A?, 4?))
                                                  //| Hand(Vector(2?, Q?, 10?, A?, J?))
                                                  //| Hand(Vector(2?, Q?, J?, 10?, A?))
                                                  //| Hand(Vector(Q?, 10?, A?, J?, 4?))
                                                  //| Hand(Vector(Q?, J?, 10?, A?, 4?))
                                                  //| Hand(Vector(2?, J?, 10?, J?, 4?))
                                                  //| Hand(Vector(2?, Q?, J?, J?, 4?))
                                                  //| Hand(Vector(2?, Q?, J?, 10?, J?))
                                                  //| Hand(Vector(Q?, J?, 10?, J?, 4?))
                                                  //| Hand(Vector(2?, J?, A?, J?, 4?))
                                                  //| Hand(Vector(2?, J?, 10?, A?, J?))
                                                  //| Hand(Vector(J?, 10?, A?, J?, 4?))
                                                  //| Hand(Vector(2?, Q?, J?, A?, J?))
                                                  //| Hand(Vector(Q?, J?, A?, J?, 4?))
                                                  //| Hand(Vector(Q?, J?, 10?, A?, J?))

}
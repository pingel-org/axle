object CardHandRanking {

  println("Hand Ranking Demo")                    //> Hand Ranking Demo

  import axle._
  import axle.game.cards._
  import Implicits._

  
  val available = Deck().cards.take(7)            //> available  : IndexedSeq[axle.game.cards.Card] = Vector(Q?, J?, A?, 3?, 10?, 
                                                  //| 7?, 10?)
  val gs = available.combinations(5).map(PokerHand(_)).toList.sorted
                                                  //> gs  : List[axle.game.cards.PokerHand] = List(Q? J? 10? 7? 3?, Q? J? 10? 7? 3
                                                  //| ?, A? J? 10? 7? 3?, A? J? 10? 7? 3?, A? Q? 10? 7? 3?, A? Q? 10? 7? 3?, A? Q?
                                                  //|  J? 7? 3?, A? Q? J? 10? 3?, A? Q? J? 10? 3?, A? Q? J? 10? 7?, A? Q? J? 10? 7
                                                  //| ?, J? 10? 10? 7? 3?, Q? 10? 10? 7? 3?, Q? J? 10? 10? 3?, Q? J? 10? 10? 7?, A
                                                  //| ? 10? 10? 7? 3?, A? J? 10? 10? 3?, A? J? 10? 10? 7?, A? Q? 10? 10? 3?, A? Q?
                                                  //|  10? 10? 7?, A? Q? J? 10? 10?)
  for (g <- gs) { println(g) }                    //> Q? J? 10? 7? 3?
                                                  //| Q? J? 10? 7? 3?
                                                  //| A? J? 10? 7? 3?
                                                  //| A? J? 10? 7? 3?
                                                  //| A? Q? 10? 7? 3?
                                                  //| A? Q? 10? 7? 3?
                                                  //| A? Q? J? 7? 3?
                                                  //| A? Q? J? 10? 3?
                                                  //| A? Q? J? 10? 3?
                                                  //| A? Q? J? 10? 7?
                                                  //| A? Q? J? 10? 7?
                                                  //| J? 10? 10? 7? 3?
                                                  //| Q? 10? 10? 7? 3?
                                                  //| Q? J? 10? 10? 3?
                                                  //| Q? J? 10? 10? 7?
                                                  //| A? 10? 10? 7? 3?
                                                  //| A? J? 10? 10? 3?
                                                  //| A? J? 10? 10? 7?
                                                  //| A? Q? 10? 10? 3?
                                                  //| A? Q? 10? 10? 7?
                                                  //| A? Q? J? 10? 10?

}
object CardHandRanking {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // royal flush
  // straight flush
  // four of a kind
  // full house
  // flush
  // straight
  // three of a kind
  // two pair
  // one air

  import math.Ordering
  import math.Ordering.Implicits._
  import axle.game.poker._

  class RankOrdering extends Ordering[Rank] {
    def compare(a: Rank, b: Rank) = a.asInt.compare(b.asInt)
  }

  implicit val rankOrdering = new RankOrdering()  //> rankOrdering  : CardHandRanking.RankOrdering = CardHandRanking$$anonfun$main
                                                  //| $1$RankOrdering$1@4c5e176f

  class CardOrdering extends Ordering[Card] {
    def compare(a: Card, b: Card) = {
      rankOrdering.compare(a.rank, b.rank)
    }
  }

  implicit val cardOrdering = new CardOrdering()  //> cardOrdering  : CardHandRanking.CardOrdering = CardHandRanking$$anonfun$main
                                                  //| $1$CardOrdering$1@46b8c8e6

  val deck = Deck()                               //> deck  : axle.game.poker.Deck = 5? 7? 9? 3? 2? J? Q? 10? 5? 6? 4? 2? 7? 10? 1
                                                  //| 0? 8? Q? 6? A? 8? Q? J? 8? A? 7? 10? 9? 2? 6? 3? 4? 4? J? 7? 5? 3? 2? Q? J? 
                                                  //| A? 5? 6? K? 8? K? 9? 9? 3? A? 4? K? K?
  val available = deck.cards.take(7)              //> available  : IndexedSeq[axle.game.poker.Card] = Vector(5?, 7?, 9?, 3?, 2?, J
                                                  //| ?, Q?)

  val gs = available.combinations(5).toList
    .map({
      hand =>
        {
          val sortedHand = hand.sorted.reverse
          val gs = hand.groupBy(_.rank).map({ case (k, v) => (k, v.size) })
          val isStraight = sortedHand.zipWithIndex.tail.forall({ case (c, i) => (sortedHand.head.rank.asInt - i) == c.rank.asInt })
          val isFlush = sortedHand.tail.forall(_.suit == sortedHand.head.suit)
          val groups = gs.values.toList.sorted.reverse
          (isFlush, isStraight, groups, sortedHand)
        }
    }).sorted                                     //> gs  : List[(Boolean, Boolean, List[Int], IndexedSeq[axle.game.poker.Card])]
                                                  //|  = List((false,false,List(1, 1, 1, 1, 1),Vector(9?, 7?, 5?, 3?, 2?)), (fals
                                                  //| e,false,List(1, 1, 1, 1, 1),Vector(J?, 7?, 5?, 3?, 2?)), (false,false,List(
                                                  //| 1, 1, 1, 1, 1),Vector(J?, 9?, 5?, 3?, 2?)), (false,false,List(1, 1, 1, 1, 1
                                                  //| ),Vector(J?, 9?, 7?, 3?, 2?)), (false,false,List(1, 1, 1, 1, 1),Vector(J?, 
                                                  //| 9?, 7?, 5?, 2?)), (false,false,List(1, 1, 1, 1, 1),Vector(J?, 9?, 7?, 5?, 3
                                                  //| ?)), (false,false,List(1, 1, 1, 1, 1),Vector(Q?, 7?, 5?, 3?, 2?)), (false,f
                                                  //| alse,List(1, 1, 1, 1, 1),Vector(Q?, 9?, 5?, 3?, 2?)), (false,false,List(1, 
                                                  //| 1, 1, 1, 1),Vector(Q?, 9?, 7?, 5?, 2?)), (false,false,List(1, 1, 1, 1, 1),V
                                                  //| ector(Q?, 9?, 7?, 5?, 3?)), (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?,
                                                  //|  5?, 3?, 2?)), (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 7?, 3?, 2?))
                                                  //| , (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 7?, 5?, 2?)), (false,fals
                                                  //| e,List(1, 1, 1, 1, 1),Vector(Q?, J?, 7?, 5?, 3?)), (false,false,List(1, 1, 
                                                  //| 1, 1, 1),Vector(Q?, J?, 9?, 3?, 2?)), (false,false,List(1, 1, 1, 1, 1),Vect
                                                  //| or(Q?, J?, 9?, 5?, 2?)), (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 9?
                                                  //| , 5?, 3?)), (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 9?, 7?, 2?)), (
                                                  //| false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 9?, 7?, 3?)), (false,false,L
                                                  //| ist(1, 1, 1, 1, 1),Vector(Q?, J?, 9?, 7?, 5?)), (true,false,List(1, 1, 1, 1
                                                  //| , 1),Vector(Q?, 9?, 7?, 3?, 2?)))

  for (g <- gs) { println(g) }                    //> (false,false,List(1, 1, 1, 1, 1),Vector(9?, 7?, 5?, 3?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(J?, 7?, 5?, 3?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(J?, 9?, 5?, 3?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(J?, 9?, 7?, 3?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(J?, 9?, 7?, 5?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(J?, 9?, 7?, 5?, 3?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, 7?, 5?, 3?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, 9?, 5?, 3?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, 9?, 7?, 5?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, 9?, 7?, 5?, 3?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 5?, 3?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 7?, 3?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 7?, 5?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 7?, 5?, 3?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 9?, 3?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 9?, 5?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 9?, 5?, 3?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 9?, 7?, 2?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 9?, 7?, 3?))
                                                  //| (false,false,List(1, 1, 1, 1, 1),Vector(Q?, J?, 9?, 7?, 5?))
                                                  //| (true,false,List(1, 1, 1, 1, 1),Vector(Q?, 9?, 7?, 3?, 2?))
}
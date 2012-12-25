package axle.game.cards

object Implicits {

  implicit val rankOrdering = new RankOrdering()
  implicit val cardOrdering = new CardOrdering()
  implicit val handOrdering = new PokerHandOrdering()

}

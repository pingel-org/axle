package axle.game.poker

object Implicits {

  implicit val pokerHandOrdering = new PokerHandOrdering()
  implicit val pokerHandCategoryOrdering = new PokerHandCategoryOrdering()

}

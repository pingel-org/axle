package axle.game.poker

trait PokerMove {

  def description: String

}

case class Call() extends PokerMove {
  def description: String = "calls"
}
case class Raise(amount: Int) extends PokerMove {
  def description: String = "raises the bet by " + amount
}
case class Fold() extends PokerMove {
  def description: String = "folds"
}
case class Deal() extends PokerMove {
  def description: String = "initial deal"
}
case class Flop() extends PokerMove {
  def description: String = "reveals the flop"
}
case class Turn() extends PokerMove {
  def description: String = "reveals the turn"
}
case class River() extends PokerMove {
  def description: String = "reveals the river"
}
case class Payout() extends PokerMove {
  def description: String = "pays out"
}

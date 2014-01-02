package axle.game.poker

import axle.game._

abstract class PokerMove(_pokerPlayer: PokerPlayer)(implicit game: Poker)
extends Move[Poker](_pokerPlayer) {
  def player: PokerPlayer = _pokerPlayer
  def description: String
  def displayTo(p: PokerPlayer): String =
    (if (_pokerPlayer != p) _pokerPlayer.description else "You") + " " + description + "."
}

case class Call(pokerPlayer: PokerPlayer)(implicit game: Poker) extends PokerMove(pokerPlayer) {
  def description: String = "calls"
}
case class Raise(pokerPlayer: PokerPlayer, amount: Int)(implicit game: Poker) extends PokerMove(pokerPlayer) {
  def description: String = "raises the bet by " + amount
}
case class Fold(pokerPlayer: PokerPlayer)(implicit game: Poker) extends PokerMove(pokerPlayer) {
  def description: String = "folds"
}
case class Deal()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description: String = "initial deal"
}
case class Flop()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description: String = "reveals the flop"
}
case class Turn()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description: String = "reveals the turn"
}
case class River()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description: String = "reveals the river"
}
case class Payout()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description: String = "pays out"
}

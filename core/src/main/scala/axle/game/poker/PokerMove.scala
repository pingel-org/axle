package axle.game.poker

import axle.game._

abstract class PokerMove(_pokerPlayer: PokerPlayer)(implicit game: Poker)
extends Move[Poker](_pokerPlayer) {
  def player() = _pokerPlayer
  def description(): String
  def displayTo(p: PokerPlayer): String =
    (if (_pokerPlayer != p) _pokerPlayer.description else "You") + " " + description() + "."
}

case class Call(pokerPlayer: PokerPlayer)(implicit game: Poker) extends PokerMove(pokerPlayer) {
  def description() = "calls"
}
case class Raise(pokerPlayer: PokerPlayer, amount: Int)(implicit game: Poker) extends PokerMove(pokerPlayer) {
  def description() = "raises the bet by " + amount
}
case class Fold(pokerPlayer: PokerPlayer)(implicit game: Poker) extends PokerMove(pokerPlayer) {
  def description() = "folds"
}
case class Deal()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description() = "initial deal"
}
case class Flop()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description() = "reveals the flop"
}
case class Turn()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description() = "reveals the turn"
}
case class River()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description() = "reveals the river"
}
case class Payout()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description() = "pays out"
}

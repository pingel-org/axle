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
  def description() = "call"
}
case class Raise(pokerPlayer: PokerPlayer, amount: Int)(implicit game: Poker) extends PokerMove(pokerPlayer) {
  def description() = "raise the bet by " + amount
}
case class Fold(pokerPlayer: PokerPlayer)(implicit game: Poker) extends PokerMove(pokerPlayer) {
  def description() = "fold"
}
case class Deal()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description() = "initial deal"
}
case class Flop()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description() = "reveal the flop"
}
case class Turn()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description() = "reveal the turn"
}
case class River()(implicit game: Poker) extends PokerMove(game.dealer) {
  def description() = "reveal the river"
}

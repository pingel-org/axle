package axle.game.poker

import axle.game._
import spire.algebra.Eq
import axle.Show

trait PokerMove
  extends Move[Poker] {

  def player: PokerPlayer

  def description: String

  def displayTo(p: PokerPlayer)(implicit eqp: Eq[PokerPlayer], sp: Show[PokerPlayer]): String =
    (if (player != p) player.description else "You") + " " + description + "."
}

case class Call(player: PokerPlayer)(implicit game: Poker) extends PokerMove {
  def description: String = "calls"
}
case class Raise(player: PokerPlayer, amount: Int)(implicit game: Poker) extends PokerMove {
  def description: String = "raises the bet by " + amount
}
case class Fold(player: PokerPlayer)(implicit game: Poker) extends PokerMove {
  def description: String = "folds"
}
case class Deal()(implicit game: Poker) extends PokerMove {
  def player = game.dealer
  def description: String = "initial deal"
}
case class Flop()(implicit game: Poker) extends PokerMove {
  def player = game.dealer
  def description: String = "reveals the flop"
}
case class Turn()(implicit game: Poker) extends PokerMove {
  def player = game.dealer
  def description: String = "reveals the turn"
}
case class River()(implicit game: Poker) extends PokerMove {
  def player = game.dealer
  def description: String = "reveals the river"
}
case class Payout()(implicit game: Poker) extends PokerMove {
  def player = game.dealer
  def description: String = "pays out"
}

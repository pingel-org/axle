package axle.game.poker

import axle.game._
import spire.algebra.Eq
import axle.Show

trait PokerMove
    extends Move[Poker] {

  def player: PokerPlayer

  def description: String

  def displayTo(p: PokerPlayer, game: Poker)(implicit eqp: Eq[PokerPlayer], sp: Show[PokerPlayer]): String =
    (if (player != p) player.description else "You") + " " + description + "."
}

case class Call(player: PokerPlayer) extends PokerMove {
  def description: String = "calls"
}
case class Raise(player: PokerPlayer, amount: Int) extends PokerMove {
  def description: String = "raises the bet by " + amount
}
case class Fold(player: PokerPlayer) extends PokerMove {
  def description: String = "folds"
}
case class Deal(player: PokerPlayer) extends PokerMove {
  def description: String = "initial deal"
}
case class Flop(player: PokerPlayer) extends PokerMove {
  def description: String = "reveals the flop"
}
case class Turn(player: PokerPlayer) extends PokerMove {
  def description: String = "reveals the turn"
}
case class River(player: PokerPlayer) extends PokerMove {
  def description: String = "reveals the river"
}
case class Payout(player: PokerPlayer) extends PokerMove {
  def description: String = "pays out"
}

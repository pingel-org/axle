package axle.game.poker

import axle.game._
import spire.algebra.Eq
import axle.Show

trait PokerMove {

  def description: String

//  def displayTo(p: Player)(implicit eqp: Eq[Player], sp: Show[Player]): String =
//    (if (player != p) player.description else "You") + " " + description + "."
}

case class Call(player: Player) extends PokerMove {
  def description: String = "calls"
}
case class Raise(player: Player, amount: Int) extends PokerMove {
  def description: String = "raises the bet by " + amount
}
case class Fold(player: Player) extends PokerMove {
  def description: String = "folds"
}
case class Deal(player: Player) extends PokerMove {
  def description: String = "initial deal"
}
case class Flop(player: Player) extends PokerMove {
  def description: String = "reveals the flop"
}
case class Turn(player: Player) extends PokerMove {
  def description: String = "reveals the turn"
}
case class River(player: Player) extends PokerMove {
  def description: String = "reveals the river"
}
case class Payout(player: Player) extends PokerMove {
  def description: String = "pays out"
}

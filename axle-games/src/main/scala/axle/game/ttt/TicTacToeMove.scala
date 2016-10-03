package axle.game.ttt

import axle.Show
import axle.game.Move
import axle.string
import spire.algebra.Eq

case class TicTacToeMove(player: TicTacToePlayer, position: Int, boardSize: Int)
  extends Move[TicTacToe] {

  def description: String = boardSize match {
    case 3 => position match {
      case 1 => "upper left"
      case 2 => "upper middle"
      case 3 => "upper right"
      case 4 => "center left"
      case 5 => "center"
      case 6 => "center right"
      case 7 => "lower left"
      case 8 => "lower middle"
      case 9 => "lower right"
    }
    case _ => string(position)
  }

  def displayTo(p: TicTacToePlayer)(implicit eqp: Eq[TicTacToePlayer], sp: Show[TicTacToePlayer]): String =
    (if (player != p) "I will" else "You have") +
      " put an " + player.id +
      " in the " + description + "."

}

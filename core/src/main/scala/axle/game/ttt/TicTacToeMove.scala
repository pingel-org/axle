
package axle.game.ttt

import axle.game._

case class TicTacToeMove(player: Player[TicTacToe], position: Int, game: TicTacToe)
  extends Move[TicTacToe](player, game) {

  def description(): String = game.boardSize match {
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
    case _ => "%s".format(position)
  }

  def displayTo(p: Player[TicTacToe]): String = {

    val beginSentence = if (player != p) {
      "I will"
    } else {
      "You have"
    }

    "%s put an %s in the %s.".format(
      beginSentence,
      player.id,
      description()
    )
  }

}

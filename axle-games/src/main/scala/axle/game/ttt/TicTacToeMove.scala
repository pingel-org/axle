package axle.game.ttt

import axle.string
import axle.showInt

case class TicTacToeMove(position: Int, boardSize: Int) {

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

}

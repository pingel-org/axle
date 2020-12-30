package axle.game.ttt

import axle.game._

case class TicTacToeState(
  moverOpt:  Option[Player],
  board:     Array[Option[Player]],
  boardSize: Int)

package axle.game.ttt

import axle.game._

abstract class TicTacToePlayer(id: String, description: String)(implicit ttt: TicTacToe)
  extends Player[TicTacToe](id, description) {
  
  override def toString() = id
}

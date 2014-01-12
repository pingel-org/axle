package axle.game.ttt

import axle.game._
import spire.algebra.Eq

object TicTacToePlayer {

  implicit def tttpEq: Eq[TicTacToePlayer] = new Eq[TicTacToePlayer] {
    def eqv(x: TicTacToePlayer, y: TicTacToePlayer): Boolean = x.equals(y)
  }

}

abstract class TicTacToePlayer(id: String, description: String)(implicit ttt: TicTacToe)
  extends Player[TicTacToe](id, description) {

  override def toString: String = id
}

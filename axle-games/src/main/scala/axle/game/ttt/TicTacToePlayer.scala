package axle.game.ttt

import axle.game._
import spire.algebra.Eq
import axle.Show

object TicTacToePlayer {

  implicit def tttpEq: Eq[TicTacToePlayer] =
    new Eq[TicTacToePlayer] {
      def eqv(x: TicTacToePlayer, y: TicTacToePlayer): Boolean = x.equals(y)
    }

  implicit def showTTTPlayer: Show[TicTacToePlayer] =
    new Show[TicTacToePlayer] {
      def text(p: TicTacToePlayer): String = p.id
    }

}

trait TicTacToePlayer extends Player[TicTacToe]

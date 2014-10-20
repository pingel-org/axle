package axle.game.ttt

import axle.game._
import spire.algebra.Eq
import axle.Show
import axle.string

object TicTacToePlayer {

  implicit def tttpEq: Eq[TicTacToePlayer] = new Eq[TicTacToePlayer] {
    def eqv(x: TicTacToePlayer, y: TicTacToePlayer): Boolean = x.equals(y)
  }

  implicit def showTTTPlayer: Show[TicTacToePlayer] = new Show[TicTacToePlayer] {
    
    def text(p: TicTacToePlayer): String = p.id
  }
  
}

abstract class TicTacToePlayer(id: String, description: String)(implicit ttt: TicTacToe)
  extends Player[TicTacToe](id, description)

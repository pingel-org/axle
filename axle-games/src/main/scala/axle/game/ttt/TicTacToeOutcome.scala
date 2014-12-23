package axle.game.ttt

import axle.game.Outcome

case class TicTacToeOutcome(winner: Option[TicTacToe#PLAYER])(implicit ev: TicTacToe)
  extends Outcome[TicTacToe] {

  implicit def game = ev
}

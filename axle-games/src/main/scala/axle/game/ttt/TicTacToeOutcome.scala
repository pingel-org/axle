package axle.game.ttt

import axle.game.Outcome

case class TicTacToeOutcome(winner: Option[TicTacToe#PLAYER])
  extends Outcome[TicTacToe]

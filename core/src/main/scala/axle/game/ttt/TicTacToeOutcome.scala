package axle.game.ttt

import axle.game._

case class TicTacToeOutcome(winner: Option[TicTacToe#PLAYER])(implicit ttt: TicTacToe)
extends Outcome[TicTacToe](winner)

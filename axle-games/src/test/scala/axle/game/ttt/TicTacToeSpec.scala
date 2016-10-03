
package axle.game.ttt

import axle.game.scriptToLastMoveState
import org.specs2.mutable._

class TicTacToeSpec extends Specification {

  val game = TicTacToe(3, "human", "human")

  import game.{ x, o }

  def movesFrom(pps: List[(TicTacToePlayer, Int)]): List[TicTacToeMove] =
    pps.map({ case pp => TicTacToeMove(pp._1, pp._2, game.boardSize) })

  "game1" should {
    "work" in {
      val moves = movesFrom(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 6), (x, 7)))
      val (_, lastState) = scriptToLastMoveState[TicTacToe](game, moves)
      val outcome = lastState.outcome(game).get
      outcome.displayTo(x, game) must contain("win")
      outcome.displayTo(o, game) must contain("lose")
      outcome.winner.get should be equalTo x
    }
  }

  "game2" should {
    "work" in {
      val moves = movesFrom(List((x, 2), (o, 3), (x, 4), (o, 5), (x, 6), (o, 7), (x, 8)))
      val (_, lastState) = scriptToLastMoveState[TicTacToe](game, moves)
      val winnerOpt = lastState.outcome(game).flatMap(_.winner)
      winnerOpt should be equalTo (Some(o))
    }
  }

  "game3" should {
    "work" in {
      val moves = movesFrom(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 7), (x, 8), (o, 9), (x, 6)))
      val (_, lastState) = scriptToLastMoveState[TicTacToe](game, moves)
      val winnerOpt = lastState.outcome(game).flatMap(_.winner)
      winnerOpt should be equalTo (None)
    }
  }

}

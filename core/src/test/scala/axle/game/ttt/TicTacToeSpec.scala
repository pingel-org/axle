
package axle.game

import org.specs2.mutable._

class TicTacToeSpec extends Specification {

  import ttt._

  val x = InteractiveTicTacToePlayer("X", "Player X")
  val o = InteractiveTicTacToePlayer("O", "Player O")
  val game = TicTacToe(3, x, o)

  def script(moves: List[(Player[TicTacToe], Int)]) = {
    val start = new TicTacToeState(x, game.startBoard, game).asInstanceOf[State[TicTacToe#G]]
    val lastMoveState = game.scriptedMoveStateStream(start, moves.map(pp => TicTacToeMove(pp._1, pp._2)).iterator).last
    lastMoveState._2.getOutcome.get.winner
  }

  "game1" should {
    "work" in {
      val win1 = script(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 6), (x, 7)))
      win1 should be equalTo (Some(x))
    }
  }

  "game2" should {
    "work" in {
      val win2 = script(List((x, 2), (o, 3), (x, 4), (o, 5), (x, 6), (o, 7), (x, 8)))
      win2 should be equalTo (Some(o))
    }
  }

  "game3" should {
    "work" in {
      val win3 = script(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 7), (x, 8), (o, 9), (x, 6)))
      win3 should be equalTo (None)
    }
  }

}


package axle.game.ttt

import org.specs2.mutable._

class TicTacToeSpec extends Specification {

  val game = TicTacToe(3, "human", "human")

  import game.{x, o}
  
  def script(moveScript: List[(game.type#TicTacToePlayer, Int)]) =
    game.scriptedMoveStateStream(game.startState,
      moveScript.map(pp => game.move(pp._1, pp._2)).iterator).last._2.outcome.flatMap(_.winner)

  "game1" should {
    "work" in {
      script(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 6), (x, 7))) should be equalTo (Some(x))
    }
  }

  "game2" should {
    "work" in {
      script(List((x, 2), (o, 3), (x, 4), (o, 5), (x, 6), (o, 7), (x, 8))) should be equalTo (Some(o))
    }
  }

  "game3" should {
    "work" in {
      script(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 7), (x, 8), (o, 9), (x, 6))) should be equalTo (None)
    }
  }

}


package axle.game.ttt

import axle.game.scriptToLastMoveState
import org.specs2.mutable._

class TicTacToeSpec extends Specification {

  val x = InteractiveTicTacToePlayer("X", "Player X")
  val o = InteractiveTicTacToePlayer("O", "Player O")

  val game = TicTacToe(3, x, o)

  def movesFrom(pps: List[(TicTacToePlayer, Int)]): List[TicTacToeMove] =
    pps.map({ case pp => TicTacToeMove(pp._1, pp._2, game.boardSize) })

  "game" should {
    "define intro message, have 9 positions" in {

      game.introMessage must contain("Intro")
      game.numPositions must be equalTo 9
    }
  }

  "random game" should {

    val rx = RandomTicTacToePlayer("x", "R X")
    val ro = RandomTicTacToePlayer("o", "R O")
    val rGame = TicTacToe(3, rx, ro)

    "produce moveStateStream" in {
      rGame.moveStateStream(rGame.startState).take(3).length must be equalTo 3
    }

    "play" in {
      val endState: TicTacToeState = rGame.play(rGame.startState, false).get
      // TODO number of moves should really be 0
      endState.moves(rGame).length must be lessThan 5
    }

    "product game stream" in {
      val games = rGame.gameStream(rGame.startState, false).take(2)
      games.length must be equalTo 2
    }

  }

  "start state" should {
    "display movement key to player x, and have 9 moves available to x" in {
      game.startState.displayTo(x, game) must contain("Movement Key")
    }
  }

  "startFrom" should {
    "simply return the start state" in {
      val state = game.startState
      val move = state.moves(game).head
      val nextState = state(move, game).get // TODO .get
      game.startFrom(nextState).get.moves(game).length must be equalTo 9
    }
  }

  "starting moves" should {
    "be nine-fold, display to O with 'put an', and have string descriptions that contain 'upper'" in {

      val startingMoves = game.startState.moves(game)

      startingMoves.head.displayTo(o, game) must contain("put an")
      startingMoves.length must be equalTo 9
      startingMoves.map(_.description).mkString(",") must contain("upper")
    }
    "work for large (4x4) game" in {
      val bigGame = TicTacToe(4, x, o)
      val startingMoves = bigGame.startState.moves(game)
      startingMoves.map(_.description).mkString(",") must contain("16")
    }
  }

  "event queues" should {
    "be two" in {
      val move = game.startState.moves(game).head
      val newState = game.startState.broadcast(game.players, move)
      newState.eventQueues.size must be equalTo 2
    }
  }

  "interactive player" should {
    "print various messages" in {
      val i = InteractiveTicTacToePlayer("i", "IP")

      val firstMove = TicTacToeMove(x, 2, game.boardSize)
      val secondState = game.startState.apply(firstMove, game).get

      // TODO grab resulting output via an IO Monad or some such
      i.introduceGame(game)
      i.displayEvents(List(firstMove), game)
      i.endGame(game.startState, game)
      i.id must be equalTo "i"
      i.validateMoveInput("1", game.startState, game).right.toOption.get.position must be equalTo 1
      i.validateMoveInput("14", game.startState, game) must be equalTo Left("Please enter a number between 1 and 9")
      i.validateMoveInput("foo", game.startState, game) must be equalTo Left("foo is not a valid move.  Please select again")
      i.validateMoveInput("2", secondState, game) must be equalTo Left("That space is occupied.")
    }
  }

  "random player" should {
    "make a move" in {
      val rando = RandomTicTacToePlayer("r", "RP")

      val (move, result) = rando.move(game.startState, game)

      move.position must be greaterThan 0
    }
  }

  "A.I. player with" should {
    "make a move" in {

      val ai = AITicTacToePlayer("o", "aio")

      val firstMove = TicTacToeMove(x, 2, game.boardSize)

      val secondState = game.startState.apply(firstMove, game).get

      val (move, result) = ai.move(secondState, game)

      move.position must be greaterThan 0
    }
  }

  "7-move x diagonal" should {
    "be a victory for x" in {
      val moves = movesFrom(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 6), (x, 7)))
      val (_, lastState) = scriptToLastMoveState[TicTacToe](game, moves)
      val outcome = lastState.outcome(game).get
      outcome.displayTo(x, game) must contain("You have beaten")
      outcome.displayTo(o, game) must contain("beat you")
      outcome.winner.get should be equalTo x
    }
  }

  "7-move o diagonal" should {
    "be a victory for o" in {
      val moves = movesFrom(List((x, 2), (o, 3), (x, 4), (o, 5), (x, 6), (o, 7), (x, 8)))
      val (_, lastState) = scriptToLastMoveState[TicTacToe](game, moves)
      val winnerOpt = lastState.outcome(game).flatMap(_.winner)
      winnerOpt should be equalTo (Some(o))
    }
  }

  "9 move tie" should {
    "result in no-winner outcome" in {
      val moves = movesFrom(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 7), (x, 8), (o, 9), (x, 6)))
      val (_, lastState) = scriptToLastMoveState[TicTacToe](game, moves)
      val winnerOpt = lastState.outcome(game).flatMap(_.winner)
      winnerOpt should be equalTo (None)
    }
  }

}

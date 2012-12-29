package axle.game.ttt

import axle.game._
import collection._
import util.Random.nextInt

abstract class TicTacToePlayer(id: String, description: String)(implicit ttt: TicTacToe)
  extends Player[TicTacToe](id, description)

class AITicTacToePlayer(aitttPlayerId: String, aitttDescription: String = "minimax")(implicit ttt: TicTacToe)
  extends TicTacToePlayer(aitttPlayerId, aitttDescription) {

  val heuristic = (state: TicTacToeState) => ttt.players.map(p => {
    (p, state.outcome.map(out => if (out.winner == Some(p)) 1.0 else -1.0).getOrElse(0.0))
  }).toMap

  def move(state: TicTacToeState): (TicTacToeMove, TicTacToeState) = {
    val (move, newState, values) = ttt.minimax(state, 3, heuristic)
    (move, newState)
  }
}

class RandomTicTacToePlayer(aitttPlayerId: String, aitttDescription: String = "random")(implicit ttt: TicTacToe)
  extends TicTacToePlayer(aitttPlayerId, aitttDescription) {

  def move(state: TicTacToeState): (TicTacToeMove, TicTacToeState) = {
    val opens = state.moves
    val move = opens(nextInt(opens.length))
    (move, state(move).get) // TODO: .get
  }
}

class InteractiveTicTacToePlayer(itttPlayerId: String, itttDescription: String = "human")(implicit ttt: TicTacToe)
  extends TicTacToePlayer(itttPlayerId, itttDescription) {

  val eventQueue = mutable.ListBuffer[Event[TicTacToe]]()

  override def introduceGame(): Unit = {
    val intro = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)
    println(intro)
  }

  override def notify(event: Event[TicTacToe]): Unit = {
    eventQueue += event
  }

  override def displayEvents(): Unit = {
    val info = eventQueue.map(_.displayTo(this)).mkString("  ")
    println(info)
    eventQueue.clear()
  }

  def userInputStream(): Stream[String] = {
    print("Enter move: ")
    val num = readLine()
    println
    Stream.cons(num, userInputStream)
  }

  def isValidMove(num: String, state: TicTacToeState): Boolean = {
    try {
      val i = num.toInt
      if (i >= 1 && i <= ttt.numPositions) {
        if (state(i).isEmpty) {
          true
        } else {
          println("That space is occupied.")
          false
        }
      } else {
        println("Please enter a number between 1 and " + ttt.numPositions)
        false
      }
    } catch {
      case e: Exception => {
        println(num + " is not a valid move.  Please select again")
        false
      }
    }
  }

  def move(state: TicTacToeState): (TicTacToeMove, TicTacToeState) = {
    displayEvents()
    println(state.displayTo(state.player))
    val move = TicTacToeMove(this, userInputStream().find(input => isValidMove(input, state)).map(_.toInt).get)
    (move, state(move).get) // TODO .get
  }

}

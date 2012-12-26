
package axle.game.ttt

import axle.game._
import axle.matrix.ArrayMatrixFactory._
import util.Random.{nextInt}
import collection._
import scalaz._
import Scalaz._

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(boardSize: Int = 3, xClass: String = "human", oClass: String = "ai")
  extends Game {

  ttt =>

  type PLAYER = TicTacToePlayer
  type MOVE = TicTacToeMove
  type STATE = TicTacToeState
  type OUTCOME = TicTacToeOutcome

  val x = player("X", "Player X", xClass)
  val o = player("O", "Player O", oClass)

  def state(player: TicTacToePlayer, board: Matrix[Option[String]]) =
    new TicTacToeState(player, board)

  def move(player: TicTacToePlayer, position: Int) = TicTacToeMove(player, position)

  def player(id: String, description: String, which: String) = which match {
    case "random" => new RandomTicTacToePlayer(id, description)
    case "ai" => new AITicTacToePlayer(id, description)
    case _ => new InteractiveTicTacToePlayer(id, description)
  }

  def startState() = state(x, startBoard())
  
  def numPositions() = boardSize * boardSize

  def introMessage() = "Intro message to Tic Tac Toe"

  def startBoard() = matrix[Option[String]](boardSize, boardSize, None)

  def players(): immutable.Set[TicTacToePlayer] = immutable.Set(x, o)

  def playerAfter(player: TicTacToePlayer): TicTacToePlayer =
    if (player == x) o else x

  case class TicTacToeMove(tttPlayer: TicTacToePlayer, position: Int)
    extends Move(tttPlayer) {

    def description(): String = ttt.boardSize match {
      case 3 => position match {
        case 1 => "upper left"
        case 2 => "upper middle"
        case 3 => "upper right"
        case 4 => "center left"
        case 5 => "center"
        case 6 => "center right"
        case 7 => "lower left"
        case 8 => "lower middle"
        case 9 => "lower right"
      }
      case _ => position.toString
    }

    def displayTo(p: TicTacToePlayer): String =
      (if (tttPlayer != p) { "I will" } else { "You have" }) +
        " put an " + tttPlayer.id +
        " in the " + description() + "."

  }

  case class TicTacToeState(player: TicTacToePlayer, board: Matrix[Option[String]])
    extends State() {

    val boardSize = board.columns
    val numPositions = board.length

    def displayTo(viewer: TicTacToePlayer): String = {

      val keyWidth = numPositions.toString().length

      "Board:         Movement Key:\n" +
        0.until(boardSize).map(r => {
          val rowlist = board.row(r).toList
          rowlist.map(_.getOrElse(" ")).mkString("|") +
            "          " +
            (1 + r * boardSize).until(1 + (r + 1) * boardSize).mkString("|") // TODO rjust(keyWidth)
        }).mkString("\n")

    }

    def positionToRow(position: Int) = (position - 1) / boardSize

    def positionToColumn(position: Int) = (position - 1) % boardSize

    def apply(position: Int) = board(positionToRow(position), positionToColumn(position))

    // The validation in InteractiveTicTacToePlayer.chooseMove might be better placed here
    //    def updat(position: Int, player: TicTacToePlayer) =
    //      board(positionToRow(position), positionToColumn(position)) = Some(player.id)

    def hasWonRow(player: TicTacToePlayer) =
      (0 until boardSize).exists(board.row(_).toList.forall(_ equals Some(player.id)))

    def hasWonColumn(player: TicTacToePlayer) =
      (0 until boardSize).exists(board.column(_).toList.forall(_ equals Some(player.id)))

    def hasWonDiagonal(player: TicTacToePlayer) =
      (0 until boardSize).forall(i => board(i, i) equals Some(player.id)) ||
        (0 until boardSize).forall(i => board(i, (boardSize - 1) - i) equals Some(player.id))

    def hasWon(player: TicTacToePlayer) = hasWonRow(player) || hasWonColumn(player) || hasWonDiagonal(player)

    def openPositions() = 1.to(numPositions).filter(this(_).isEmpty)

    def moves(): Seq[TicTacToeMove] = openPositions().map(TicTacToeMove(player, _))

    def outcome(): Option[TicTacToeOutcome] = {
      val winner = ttt.players.find(hasWon(_))
      if (winner.isDefined) {
        Some(TicTacToeOutcome(winner))
      } else if (openPositions().length == 0) {
        Some(TicTacToeOutcome(None))
      } else {
        None
      }
    }

    def apply(move: TicTacToeMove): TicTacToeState = {
      val rc2v = (positionToRow(move.position), positionToColumn(move.position)) -> Some(player.id)
      ttt.state(ttt.playerAfter(move.tttPlayer), board.addAssignment(positionToRow(move.position), positionToColumn(move.position), Some(player.id)))
    }

  }

  case class TicTacToeOutcome(winner: Option[TicTacToePlayer]) extends Outcome(winner)

  abstract class TicTacToePlayer(id: String, description: String) extends Player(id, description)

  class AITicTacToePlayer(aitttPlayerId: String, aitttDescription: String = "minimax")
    extends TicTacToePlayer(aitttPlayerId, aitttDescription) {

    val heuristic = (state: TicTacToeState) => players.map(p => {
      (p, state.outcome.map(out => if (out.winner == Some(p)) 1.0 else -1.0).getOrElse(0.0))
    }).toMap

    def chooseMove(state: TicTacToeState): TicTacToeMove = ttt.minimax(state, 3, heuristic)._1
  }

  class RandomTicTacToePlayer(aitttPlayerId: String, aitttDescription: String = "random")
    extends TicTacToePlayer(aitttPlayerId, aitttDescription) {

    def chooseMove(state: TicTacToeState): TicTacToeMove = {
      val opens = state.moves
      opens(nextInt(opens.length))
    }
  }

  class InteractiveTicTacToePlayer(itttPlayerId: String, itttDescription: String = "human")
    extends TicTacToePlayer(itttPlayerId, itttDescription) {

    val eventQueue = mutable.ListBuffer[Event]()

    override def introduceGame(): Unit = {
      val intro = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)
      println(intro)
    }

    override def endGame(state: TicTacToeState): Unit = {
      displayEvents()
      println(state.displayTo(state.player))
    }

    override def notify(event: Event): Unit = {
      eventQueue += event
    }

    def displayEvents(): Unit = {
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

    def chooseMove(state: TicTacToeState): TicTacToeMove = {
      displayEvents()
      println(state.displayTo(state.player))
      TicTacToeMove(this, userInputStream().find(input => isValidMove(input, state)).map(_.toInt).get)
    }

  }

}

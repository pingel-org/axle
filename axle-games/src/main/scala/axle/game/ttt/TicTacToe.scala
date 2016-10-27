
package axle.game.ttt

import axle.game._
import spire.implicits._

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(
    boardSize: Int = 3,
    x: Player,
    xStrategy: (TicTacToeState, TicTacToe) => TicTacToeMove,
    xDisplayer: String => Unit,
    o: Player,
    oStrategy: (TicTacToeState, TicTacToe) => TicTacToeMove,
    oDisplayer: String => Unit) {

  val players = Vector(x, o)

  val playerToDisplayer = Map(x -> xDisplayer, o -> oDisplayer)

  val playerToStrategy = Map(x -> xStrategy, o -> oStrategy)

  def numPositions: Int = boardSize * boardSize

  def startBoard: Array[Option[Player]] =
    (0 until (boardSize * boardSize)).map(i => None).toArray

  def playerAfter(player: Player): Player =
    if (player === x) o else x

  def markFor(player: Player): Char =
    if (player === x) 'X' else 'O'

  def state(
    playerOptFn: (TicTacToeState) => Option[Player],
    board: Array[Option[Player]]): TicTacToeState =
    TicTacToeState(playerOptFn, board, boardSize)

}

object TicTacToe {

  implicit val evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] =
    new Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] {

      def introMessage(ttt: TicTacToe) = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)

      def startState(ttt: TicTacToe): TicTacToeState =
        TicTacToeState(s => Some(ttt.x), ttt.startBoard, ttt.boardSize)

      def startFrom(ttt: TicTacToe, s: TicTacToeState): Option[TicTacToeState] =
        Some(startState(ttt))

      def players(g: TicTacToe): IndexedSeq[Player] =
        g.players

      def strategyFor(g: TicTacToe, player: Player): (TicTacToeState, TicTacToe) => TicTacToeMove =
        g.playerToStrategy(player)

      def displayerFor(g: TicTacToe, player: Player): String => Unit =
        g.playerToDisplayer(player)

      def parseMove(g: TicTacToe, input: String): Either[String, TicTacToeMove] = {
        val eitherI: Either[String, Int] = try {
          val position = input.toInt
          if (position >= 1 && position <= g.numPositions) {
            Right(position)
          } else {
            Left("Please enter a number between 1 and " + g.numPositions)
          }
        } catch {
          case e: Exception => {
            Left(input + " is not a valid move.  Please select again")
          }
        }
        eitherI.right.map { position =>
          TicTacToeMove(position, g.boardSize)
        }
      }

      def isValid(g: TicTacToe, state: TicTacToeState, move: TicTacToeMove): Either[String, TicTacToeMove] =
        if (state(move.position).isEmpty) {
          Right(move)
        } else {
          Left("That space is occupied.")
        }

      def displayOutcomeTo(
        game: TicTacToe,
        outcome: TicTacToeOutcome,
        observer: Player): String =
        outcome.winner map { wp =>
          s"${wp.referenceFor(observer)} beat " + evGame.players(game).filterNot(_ === wp).map(_.referenceFor(observer)).toList.mkString(" and ") + "!"
        } getOrElse ("The game was a draw.")

      def displayMoveTo(
        game: TicTacToe,
        mover: Player,
        move: TicTacToeMove,
        observer: Player): String =
        mover.referenceFor(observer) +
          " put an " + game.markFor(mover) +
          " in the " + move.description + "."

      def applyMove(s: TicTacToeState, game: TicTacToe, move: TicTacToeMove)(
        implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): TicTacToeState = {
        val nextMoverOptFn = (newState: TicTacToeState) =>
          if (outcome(newState, game).isDefined) {
            None
          } else {
            Some(game.playerAfter(s.moverOpt.get))
          }
        game.state(nextMoverOptFn, s.place(move.position, s.moverOpt.get))
      }

      def displayStateTo(s: TicTacToeState, viewer: Player, game: TicTacToe): String =
        s.displayTo(viewer, game)

      def mover(s: TicTacToeState): Option[Player] =
        s.moverOpt

      def moves(s: TicTacToeState, game: TicTacToe): Seq[TicTacToeMove] =
        mover(s).map { p => s.openPositions(game).map(TicTacToeMove(_, game.boardSize)) } getOrElse (List.empty)

      def outcome(s: TicTacToeState, game: TicTacToe): Option[TicTacToeOutcome] =
        s.outcome(game)

    }
}

package axle.game

import axle.string
import spire.implicits._

package object ttt {

  implicit val evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] =
    new Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] {

      def startState(ttt: TicTacToe): TicTacToeState =
        TicTacToeState(s => Some(ttt.x), ttt.startBoard, ttt.boardSize)

      def startFrom(ttt: TicTacToe, s: TicTacToeState): Option[TicTacToeState] =
        Some(startState(ttt))

      def players(g: TicTacToe): IndexedSeq[Player] =
        g.players

      def strategyFor(g: TicTacToe, player: Player): (TicTacToeState, TicTacToe) => TicTacToeMove =
        g.playerToStrategy(player)

      def isValid(g: TicTacToe, state: TicTacToeState, move: TicTacToeMove): Either[String, TicTacToeMove] =
        if (state.playerAt(move.position).isEmpty) {
          Right(move)
        } else {
          Left("That space is occupied.")
        }

      def applyMove(s: TicTacToeState, game: TicTacToe, move: TicTacToeMove): TicTacToeState = {
        val nextMoverOptFn = (newState: TicTacToeState) =>
          if (outcome(newState, game).isDefined) {
            None
          } else {
            Some(game.playerAfter(s.moverOpt.get))
          }
        TicTacToeState(nextMoverOptFn, s.place(move.position, s.moverOpt.get), game.boardSize)
      }

      def mover(s: TicTacToeState): Option[Player] =
        s.moverOpt

      def moves(s: TicTacToeState, game: TicTacToe): Seq[TicTacToeMove] =
        mover(s).map { p => s.openPositions(game).map(TicTacToeMove(_, game.boardSize)) } getOrElse (List.empty)

      def outcome(s: TicTacToeState, game: TicTacToe): Option[TicTacToeOutcome] = {
        import s._
        val winner = game.players.find(hasWon)
        if (winner.isDefined) {
          Some(TicTacToeOutcome(winner))
        } else if (openPositions(game).length === 0) {
          Some(TicTacToeOutcome(None))
        } else {
          None
        }
      }

      /**
       * IO related methods
       */

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

      def introMessage(ttt: TicTacToe) = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)

      def displayStateTo(s: TicTacToeState, observer: Player, game: TicTacToe): String = {
        val keyWidth = string(s.numPositions).length

        "Board:         Movement Key:\n" +
          0.until(s.boardSize).map(r => {
            s.row(r).map(playerOpt => playerOpt.map(game.markFor).getOrElse(" ")).mkString("|") +
              "          " +
              (1 + r * s.boardSize).until(1 + (r + 1) * s.boardSize).mkString("|") // TODO rjust(keyWidth)
          }).mkString("\n")
      }

      def displayMoveTo(
        game: TicTacToe,
        mover: Player,
        move: TicTacToeMove,
        observer: Player): String =
        mover.referenceFor(observer) +
          " put an " + game.markFor(mover) +
          " in the " + move.description + "."

      def displayOutcomeTo(
        game: TicTacToe,
        outcome: TicTacToeOutcome,
        observer: Player): String =
        outcome.winner map { wp =>
          s"${wp.referenceFor(observer)} beat " + evGame.players(game).filterNot(_ === wp).map(_.referenceFor(observer)).toList.mkString(" and ") + "!"
        } getOrElse ("The game was a draw.")

    }

}
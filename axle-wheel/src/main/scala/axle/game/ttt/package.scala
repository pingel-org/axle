package axle.game

import cats.implicits._

package object ttt {

  implicit val eqMove = cats.kernel.Eq.fromUniversalEquals[TicTacToeMove]

  implicit val evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove, TicTacToeState, TicTacToeMove] =
    new Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove, TicTacToeState, TicTacToeMove] {

      def startState(ttt: TicTacToe): TicTacToeState =
        TicTacToeState(s => Some(ttt.x), ttt.startBoard, ttt.boardSize)

      def startFrom(ttt: TicTacToe, s: TicTacToeState): Option[TicTacToeState] =
        Some(startState(ttt))

      def players(g: TicTacToe): IndexedSeq[Player] =
        g.players

      def isValid(g: TicTacToe, state: TicTacToeState, move: TicTacToeMove): Either[String, TicTacToeMove] =
        if (state.playerAt(move.position).isEmpty) {
          Right(move)
        } else {
          Left("That space is occupied.")
        }

      def applyMove(game: TicTacToe, state: TicTacToeState, move: TicTacToeMove): TicTacToeState = {
        val nextMoverOptFn: TicTacToeState => Option[Player] = (newState: TicTacToeState) =>
          mover(game, newState).map { player =>
            Some(game.playerAfter(player))
          } getOrElse { None }
        TicTacToeState(nextMoverOptFn, state.place(move.position, state.moverOpt.get), game.boardSize)
      }

      def mover(game: TicTacToe, state: TicTacToeState): Either[TicTacToeOutcome, Player] = {
        import state._
        val winner = game.players.find(hasWon)
        if (winner.isDefined) {
          Left(TicTacToeOutcome(winner))
        } else if (openPositions(game).length === 0) {
          Left(TicTacToeOutcome(None))
        } else {
          Right(state.moverOpt.get)
        }
      }

      def moves(game: TicTacToe, s: TicTacToeState): Seq[TicTacToeMove] =
        mover(game, s).map { p => s.openPositions(game).map(TicTacToeMove(_, game.boardSize)) } getOrElse (List.empty)

      def maskState(game: TicTacToe, state: TicTacToeState, observer: Player): TicTacToeState =
        state

      def maskMove(game: TicTacToe, move: TicTacToeMove, mover: Player, observer: Player): TicTacToeMove =
        move

    }

  implicit val evGameIO: GameIO[TicTacToe, TicTacToeOutcome, TicTacToeMove, TicTacToeState, TicTacToeMove] =
    new GameIO[TicTacToe, TicTacToeOutcome, TicTacToeMove, TicTacToeState, TicTacToeMove] {

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
        eitherI.map { position =>
          TicTacToeMove(position, g.boardSize)
        }
      }

      def introMessage(ttt: TicTacToe) = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)

      def displayStateTo(game: TicTacToe, s: TicTacToeState, observer: Player): String = {
        // val keyWidth = string(s.numPositions).length

        "Board:         Movement Key:\n" +
          0.until(s.boardSize).map(r => {
            s.row(r).map(playerOpt => playerOpt.map(game.markFor).getOrElse(" ")).mkString("|") +
              "          " +
              (1 + r * s.boardSize).until(1 + (r + 1) * s.boardSize).mkString("|") // TODO rjust(keyWidth)
          }).mkString("\n")
      }

      def displayMoveTo(
        game:     TicTacToe,
        move:     TicTacToeMove,
        mover:    Player,
        observer: Player): String =
        mover.referenceFor(observer) +
          " put an " + game.markFor(mover) +
          " in the " + move.description + "."

      def displayOutcomeTo(
        game:     TicTacToe,
        outcome:  TicTacToeOutcome,
        observer: Player): String =
        outcome.winner map { wp =>
          s"${wp.referenceFor(observer)} beat " + evGame.players(game).filterNot(_ === wp).map(_.referenceFor(observer)).toList.mkString(" and ") + "!"
        } getOrElse ("The game was a draw.")

    }

}

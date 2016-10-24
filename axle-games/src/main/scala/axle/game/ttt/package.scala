package axle.game

import axle.Show
import spire.algebra.Eq
import spire.implicits.eqOps

package object ttt {

  implicit val evState: State[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] =
    new State[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] {

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

      def displayTo(s: TicTacToeState, viewer: Player, game: TicTacToe)(
        implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): String =
        s.displayTo(viewer, game)

      def mover(s: TicTacToeState): Option[Player] =
        s.moverOpt

      def moves(s: TicTacToeState, game: TicTacToe): Seq[TicTacToeMove] =
        mover(s).map { p => s.openPositions(game).map(TicTacToeMove(_, game.boardSize)) } getOrElse (List.empty)

      def outcome(s: TicTacToeState, game: TicTacToe): Option[TicTacToeOutcome] =
        s.outcome(game)
    }

  implicit val evOutcome: Outcome[TicTacToeOutcome] =
    new Outcome[TicTacToeOutcome] {

      def displayTo[G, S, M](
        game: G,
        outcome: TicTacToeOutcome,
        observer: Player)(
          implicit evGame: Game[G, S, TicTacToeOutcome, M],
          eqp: Eq[Player],
          sp: Show[Player]): String = {
        outcome.winner map { wp =>
          s"${wp.referenceFor(observer)} beat " + evGame.players(game).filterNot(_ === wp).map(_.referenceFor(observer)).toList.mkString(" and ") + "!"
        } getOrElse ("The game was a draw.")
      }

    }

  implicit val evMove: Move[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] =
    new Move[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] {

      def displayTo(game: TicTacToe, mover: Player, move: TicTacToeMove, observer: Player)(
        implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove], eqp: Eq[Player], sp: Show[Player]): String =
        mover.referenceFor(observer) +
          " put an " + game.markFor(mover) +
          " in the " + move.description + "."
    }

}
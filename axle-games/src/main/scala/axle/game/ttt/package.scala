package axle.game

import axle.Show
import spire.algebra.Eq

package object ttt {

  implicit val evState: State[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] =
    new State[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] {

      def applyMove(s: TicTacToeState, game: TicTacToe, move: TicTacToeMove)(
        implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): TicTacToeState =
        s(move, game)

      def displayTo(s: TicTacToeState, viewer: Player, game: TicTacToe)(
        implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): String =
        s.displayTo(viewer, game)

      def mover(s: TicTacToeState): Player =
        s.player

      def moves(s: TicTacToeState, game: TicTacToe): Seq[TicTacToeMove] =
        s.moves(game)

      def outcome(s: TicTacToeState, game: TicTacToe): Option[TicTacToeOutcome] =
        s.outcome(game)
    }

  implicit val evOutcome: Outcome[TicTacToeOutcome] =
    new Outcome[TicTacToeOutcome] {

      def winner(outcome: TicTacToeOutcome): Option[Player] = outcome.winner
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
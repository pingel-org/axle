package axle.game

import axle.Show
import spire.algebra.Eq

package object ttt {

  implicit val evState: State[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] =
    new State[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] {

      def applyMove(s: TicTacToeState, move: TicTacToeMove, game: TicTacToe)(
        implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): Option[TicTacToeState] =
        s(move, game)

      def displayTo(s: TicTacToeState, viewer: Player, game: TicTacToe)(
        implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): String =
        s.displayTo(viewer, game)

      def eventQueues(s: TicTacToeState): Map[Player, List[Either[TicTacToeOutcome, TicTacToeMove]]] =
        s.eventQueues

      def mover(s: TicTacToeState): Player =
        s.player

      def moves(s: TicTacToeState, game: TicTacToe): Seq[TicTacToeMove] =
        s.moves(game)

      def outcome(s: TicTacToeState, game: TicTacToe): Option[TicTacToeOutcome] =
        s.outcome(game)

      def setEventQueues(s: TicTacToeState, qs: Map[Player, List[Either[TicTacToeOutcome, TicTacToeMove]]]): TicTacToeState =
        s.setEventQueues(qs)
    }

  implicit val evOutcome: Outcome[TicTacToeOutcome] =
    new Outcome[TicTacToeOutcome] {

      def winner(outcome: TicTacToeOutcome): Option[Player] = outcome.winner
    }

  implicit val evMove: Move[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] =
    new Move[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] {

      def displayTo(game: TicTacToe, move: TicTacToeMove, p: Player)(
        implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove], eqp: Eq[Player], sp: Show[Player]): String =
        move.player.referenceFor(p) +
          " put an " + game.markFor(move.player) +
          " in the " + move.description + "."

      def player(m: TicTacToeMove): Player = m.player
    }

}
package axle.game

import spire.algebra.Eq
import spire.implicits._
import axle.Show

package object poker {

  lazy val moveParser = MoveParser()

  def dealerMove(state: PokerState, game: Poker): PokerMove = {
    val dealer = state.mover
    state.numShown match {
      case 0 =>
        if (state.inFors.size === 0) {
          Deal(dealer)
        } else {
          Flop(dealer)
        }
      case 3 => Turn(dealer)
      case 4 => River(dealer)
      case 5 => Payout(dealer)
    }
  }

  implicit val evState: State[Poker, PokerState, PokerOutcome, PokerMove] =
    new State[Poker, PokerState, PokerOutcome, PokerMove] {

      def applyMove(s: PokerState, move: PokerMove, game: Poker)(
        implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): Option[PokerState] =
        s(move, game)

      def displayTo(s: PokerState, viewer: Player, game: Poker)(
        implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): String =
        s.displayTo(viewer, game)

      def eventQueues(s: PokerState): Map[Player, List[Either[PokerOutcome, PokerMove]]] =
        s.eventQueues

      def mover(s: PokerState): Player =
        s.mover

      def moves(s: PokerState, game: Poker): Seq[PokerMove] =
        s.moves(game)

      def outcome(s: PokerState, game: Poker): Option[PokerOutcome] =
        s.outcome(game)

      def setEventQueues(s: PokerState, qs: Map[Player, List[Either[PokerOutcome, PokerMove]]]): PokerState =
        s.setEventQueues(qs)
    }

  implicit val evOutcome: Outcome[PokerOutcome] =
    new Outcome[PokerOutcome] {

      def winner(outcome: PokerOutcome): Option[Player] = outcome.winner
    }

  implicit val evMove: Move[Poker, PokerState, PokerOutcome, PokerMove] =
    new Move[Poker, PokerState, PokerOutcome, PokerMove] {

      def displayTo(game: Poker, move: PokerMove, p: Player)(
        implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove], eqp: Eq[Player], sp: Show[Player]): String =
        move.player.referenceFor(p) + " " + move.description + "."

      def player(m: PokerMove): Player = m.player
    }

}
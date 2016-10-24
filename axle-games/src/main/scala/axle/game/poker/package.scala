package axle.game

import spire.algebra.Eq
import axle.Show
import axle.string
// import spire.implicits.eqOps

package object poker {

  lazy val moveParser = MoveParser()

  implicit val evState: State[Poker, PokerState, PokerOutcome, PokerMove] =
    new State[Poker, PokerState, PokerOutcome, PokerMove] {

      def applyMove(s: PokerState, game: Poker, move: PokerMove)(
        implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): PokerState =
        s(game, move)

      def displayTo(s: PokerState, observer: Player, game: Poker)(
        implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): String =
        s.displayTo(observer, game)

      def mover(s: PokerState): Option[Player] =
        s.moverOpt

      def moves(s: PokerState, game: Poker): Seq[PokerMove] =
        s.moves(game)

      def outcome(s: PokerState, game: Poker): Option[PokerOutcome] =
        s.outcome(game)
    }

  implicit val evOutcome: Outcome[PokerOutcome] =
    new Outcome[PokerOutcome] {

      def displayTo[G, S, M](
        game: G,
        outcome: PokerOutcome,
        observer: Player)(
          implicit evGame: Game[G, S, PokerOutcome, M],
          eqp: Eq[Player],
          sp: Show[Player]): String = {
        "Winner: " + outcome.winner.get.description + "\n" +
          "Hand  : " + outcome.hand.map(h => string(h) + " " + h.description).getOrElse("not shown") + "\n"
      }

    }

  implicit val evMove: Move[Poker, PokerState, PokerOutcome, PokerMove] =
    new Move[Poker, PokerState, PokerOutcome, PokerMove] {

      def displayTo(game: Poker, mover: Player, move: PokerMove, observer: Player)(
        implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove], eqp: Eq[Player], sp: Show[Player]): String =
        mover.referenceFor(observer) + " " + move.description + "."
    }

}
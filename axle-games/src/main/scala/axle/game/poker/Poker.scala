package axle.game.poker

import axle.game._
import axle.game.cards._

// (1 to numPlayers).map(i => player("P" + i, "Player " + i, "human"))
// def players: IndexedSeq[PokerPlayer] = _players

case class Poker(players: IndexedSeq[Player]) {

  val numPlayers = players.length

  val dealer = Player("D", "Dealer") // TODO ??? PokerPlayerDealer.move)

}

object Poker {

  implicit val pokerGame: Game[Poker, PokerState, PokerOutcome, PokerMove] =
    new Game[Poker, PokerState, PokerOutcome, PokerMove] {

      // def introMessage: String = "Welcome to Axle Texas Hold Em Poker"

      def introMessage(g: Poker) = """
Texas Hold Em Poker

Example moves:

  check
  raise 1.0
  call
  fold

"""

      def startState(g: Poker): PokerState =
        PokerState(
          state => g.dealer,
          Deck(),
          Vector(),
          0, // # of shared cards showing
          Map[Player, Seq[Card]](),
          0, // pot
          0, // current bet
          g.players.toSet, // stillIn
          Map(), // inFors
          g.players.map(player => (player, 100)).toMap, // piles
          None,
          Map())

      def startFrom(g: Poker, s: PokerState): Option[PokerState] = {

        if (s.stillIn.size > 0) {
          Some(PokerState(
            state => g.dealer,
            Deck(),
            Vector(),
            0,
            Map(),
            0,
            0,
            s.stillIn,
            Map(),
            s.piles,
            None,
            Map()))
        } else {
          None
        }
      }

      def displayerFor(g: Poker, player: Player): String => Unit = ???

      def players(g: Poker): IndexedSeq[Player] = g.players

      def strategyFor(g: Poker, player: Player): (PokerState, Poker) => PokerMove = ???

    }

}
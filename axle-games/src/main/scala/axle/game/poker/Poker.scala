package axle.game.poker

import axle.game._
import axle.game.cards._

case class Poker(
    playersStrategiesDisplayers: IndexedSeq[(Player, (PokerState, Poker) => PokerMove, String => Unit)]) {

  val players = playersStrategiesDisplayers.map(_._1)

  val numPlayers = players.length

  val dealer = Player("D", "Dealer")

  val allPlayers = (dealer, dealerMove _, (s: String) => {}) +: playersStrategiesDisplayers

  val playerToStrategy = allPlayers.map(tuple => tuple._1 -> tuple._2).toMap

  val playerToDisplayer = allPlayers.map(tuple => tuple._1 -> tuple._3).toMap

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

      def players(g: Poker): IndexedSeq[Player] =
        g.players

      def strategyFor(g: Poker, player: Player): (PokerState, Poker) => PokerMove =
        g.playerToStrategy(player)

      def displayerFor(g: Poker, player: Player): String => Unit =
        g.playerToDisplayer(player)

      def parseMove(g: Poker, input: String, mover: Player): Either[String, PokerMove] = {
        moveParser.parse(input)(mover)
      }

      def isValid(g: Poker, state: PokerState, move: PokerMove, game: Poker): Either[String, PokerMove] =
        Right(move) // TODO

    }

}
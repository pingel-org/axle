package axle.game.poker

import axle.game._
import axle.game.cards._

case class Poker(
    playersStrategiesDisplayers: IndexedSeq[(Player, (PokerState, Poker) => PokerMove, String => Unit)],
    dealerDisplayer: String => Unit) {

  val players = playersStrategiesDisplayers.map(_._1)

  val numPlayers = players.length

  val dealer = Player("D", "Dealer")

  val allPlayers = (dealer, randomMove, dealerDisplayer) +: playersStrategiesDisplayers

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
  raise 1
  call
  fold

"""

      def startState(g: Poker): PokerState =
        PokerState(
          state => Some(g.dealer),
          Deck(),
          Vector(),
          0, // # of shared cards showing
          Map[Player, Seq[Card]](),
          0, // pot
          0, // current bet
          g.players.toSet, // stillIn
          Map(), // inFors
          g.players.map(player => (player, 100)).toMap, // piles
          None)

      def startFrom(g: Poker, s: PokerState): Option[PokerState] = {

        if (s.stillIn.size > 0) {
          Some(PokerState(
            state => Some(g.dealer),
            Deck(),
            Vector(),
            0,
            Map(),
            0,
            0,
            s.stillIn,
            Map(),
            s.piles,
            None))
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
        moveParser.parse(input)
      }

      // TODO: this implementation works, but ideally there is more information in the error
      // string about why the move is invalid (eg player raised more than he had)
      def isValid(g: Poker, state: PokerState, move: PokerMove): Either[String, PokerMove] =
        if (state.moves(g).contains(move)) {
          Right(move)
        } else {
          Left("invalid move")
        }

    }

}
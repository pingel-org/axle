package axle.game.poker

import axle.game._
import axle.game.Strategies._
import axle.game.cards._
import axle.string

case class Poker(
    playersStrategiesDisplayers: IndexedSeq[(Player, (PokerState, Poker) => PokerMove, String => Unit)],
    dealerDisplayer: String => Unit)(implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]) {

  val players = playersStrategiesDisplayers.map(_._1)

  val numPlayers = players.length

  val dealer = Player("D", "Dealer")

  val allPlayers = (dealer, randomMove(evGame), dealerDisplayer) +: playersStrategiesDisplayers

  val playerToStrategy = allPlayers.map(tuple => tuple._1 -> tuple._2).toMap

  val playerToDisplayer = allPlayers.map(tuple => tuple._1 -> tuple._3).toMap

}

object Poker {

  implicit val evGame: Game[Poker, PokerState, PokerOutcome, PokerMove] =
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

        if (s.stillIn.size > 1) {
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

      def parseMove(g: Poker, input: String): Either[String, PokerMove] =
        moveParser.parse(input)

      // TODO: this implementation works, but ideally there is more information in the error
      // string about why the move is invalid (eg player raised more than he had)
      def isValid(g: Poker, state: PokerState, move: PokerMove): Either[String, PokerMove] =
        if (state.moves(g).contains(move)) {
          Right(move)
        } else {
          Left("invalid move")
        }

      def displayOutcomeTo(
        game: Poker,
        outcome: PokerOutcome,
        observer: Player)(
          implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): String = {
        "Winner: " + outcome.winner.get.description + "\n" +
          "Hand  : " + outcome.hand.map(h => string(h) + " " + h.description).getOrElse("not shown") + "\n"
      }

      def displayMoveTo(game: Poker, mover: Player, move: PokerMove, observer: Player)(
        implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): String =
        mover.referenceFor(observer) + " " + move.description + "."

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

}
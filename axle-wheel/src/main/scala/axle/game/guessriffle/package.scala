
package axle.game

import scala.util.Try

import cats.implicits._

import spire.math.Rational
import spire.random.Dist

import axle.stats.ProbabilityModel
import axle.stats.ConditionalProbabilityTable
import axle.stats.rationalProbabilityDist

import axle.game.cards._

package object guessriffle {

    implicit val evGame: Game[GuessRiffle, GuessRiffleState, GuessRiffleOutcome, GuessRiffleMove, GuessRiffleState, Option[GuessRiffleMove], Rational, ConditionalProbabilityTable] =
    new Game[GuessRiffle, GuessRiffleState, GuessRiffleOutcome, GuessRiffleMove, GuessRiffleState, Option[GuessRiffleMove], Rational, ConditionalProbabilityTable] {

      def probabilityDist: Dist[Rational] = rationalProbabilityDist

      def startState(game: GuessRiffle): GuessRiffleState =
        GuessRiffleState(Deck(), None, List.empty, 0, None)

      def startFrom(game: GuessRiffle, s: GuessRiffleState): Option[GuessRiffleState] =
        Some(startState(game))

      def players(game: GuessRiffle): IndexedSeq[Player] =
        Vector(game.dealer, game.player)

      def strategyFor(
        game:   GuessRiffle,
        player: Player): (GuessRiffle, GuessRiffleState) => ConditionalProbabilityTable[GuessRiffleMove, Rational] =
        player match {
          case game.player => game.strategy
          case game.dealer => game.dealerStrategy
          case _           => game.strategy // TODO unreachable
        }

      def isValid(
        g:     GuessRiffle,
        state: GuessRiffleState,
        move:  GuessRiffleMove): Either[String, GuessRiffleMove] =
        Right(move) // TODO

      def applyMove(
        game:  GuessRiffle,
        state: GuessRiffleState,
        move:  GuessRiffleMove): GuessRiffleState =
        if( state.remaining.head === move.card ) {
          state.copy(remaining = state.remaining.tail, numCorrect = state.numCorrect + 1)
        } else {
          state.copy(remaining = state.remaining.tail)
        }

      def mover(
        game: GuessRiffle,
        s:    GuessRiffleState): Option[Player] =
        if (s.riffledDeck.isEmpty) {
          Some(game.dealer)
        } else {
          Some(game.player)
        }

      def moverM(
        game: GuessRiffle,
        s:    GuessRiffleState): Option[Player] =
        mover(game, s)

      def moves(
        game: GuessRiffle,
        s:    GuessRiffleState): Seq[GuessRiffleMove] =
        s.remaining.map(GuessRiffleMove)
 
      def maskState(game: GuessRiffle, state: GuessRiffleState, observer: Player): GuessRiffleState =
        if (observer === game.player) {
          state.copy(remaining = List.empty)
        } else {
          state
        }

      def maskMove(game: GuessRiffle, move: GuessRiffleMove, mover: Player, observer: Player): Option[GuessRiffleMove] =
        Some(move)

      def outcome(
        game:  GuessRiffle,
        state: GuessRiffleState): Option[GuessRiffleOutcome] =
        Some(GuessRiffleOutcome(state.numCorrect))
 
      implicit def probabilityModelPM: ProbabilityModel[ConditionalProbabilityTable] =
        ConditionalProbabilityTable.probabilityWitness

    }

    implicit val evGameIO: GameIO[GuessRiffle, GuessRiffleOutcome, GuessRiffleMove, GuessRiffleState, Option[GuessRiffleMove]] =
    new GameIO[GuessRiffle, GuessRiffleOutcome, GuessRiffleMove, GuessRiffleState, Option[GuessRiffleMove]] {

      def displayerFor(g: GuessRiffle, player: Player): String => Unit =
        player match {
          case g.player => g.displayer
          case g.dealer => g.dealerDisplayer
          case _        => g.displayer
        }

      def parseMove(g: GuessRiffle, input: String): Either[String, GuessRiffleMove] =
        Try(GuessRiffleMove(Card(Rank(input(0)), Suit(input(1))))).toEither.left.map(throwable => input + " is not a valid move.  Please select again")

      def introMessage(ttt: GuessRiffle) =
        "Guess Riffle Shuffle"

      def displayStateTo(game: GuessRiffle, s: GuessRiffleState, observer: Player): String = {
        if (observer === game.player) {
          s"${s.numCorrect} correct ${s.initialDeck.cards.size - s.remaining.size - s.numCorrect} incorrect with ${s.remaining.size} cards remaining"
        } else {
          s"${s.numCorrect} correct ${s.initialDeck.cards.size - s.remaining.size - s.numCorrect} incorrect with ${s.remaining.size} cards remaining"
        }
      }

      def displayMoveTo(
        game:     GuessRiffle,
        move:     Option[GuessRiffleMove],
        mover:    Player,
        observer: Player): String =
        mover.referenceFor(observer) + " " + move.map("guessed " + _.card.show).getOrElse("???")

      def displayOutcomeTo(
        game:     GuessRiffle,
        outcome:  GuessRiffleOutcome,
        observer: Player): String =
        game.player.referenceFor(observer) + s"${outcome.numCorrect} correct"

    }
}
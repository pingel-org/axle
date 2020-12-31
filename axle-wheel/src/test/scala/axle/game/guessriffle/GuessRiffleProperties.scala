package axle.game.guessriffle

import org.scalacheck.Properties
import org.scalacheck.Prop.forAllNoShrink

import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._
import cats.Eq
import cats.Order

import spire.random.Random
import spire.random.Seed
import spire.math.Rational

import axle.algebra.RegionEq
import axle.math.Σ
import axle.game._
import axle.game.cards._
import axle.game.guessriffle.evGame._
import axle.probability._
import axle.stats._
import axle.quanta._
import axle.syntax.kolmogorov._

class GuessRiffleProperties extends Properties("GuessRiffle Properties") {

  // `Properties` interferes with these that should be implicit
  val monad = ConditionalProbabilityTable.monadWitness[Rational]
  val eqPlayer: Eq[Player] = Eq.fromUniversalEquals[Player]
  val eqCard: Eq[Card] = Eq.fromUniversalEquals[Card]
  val eqDeck: Eq[Deck] = Eq.fromUniversalEquals[Deck]
  val eqRational: Eq[Rational] = Rational.RationalAlgebra

  def containsCorrectGuess(game: GuessRiffle, fromState: GuessRiffleState, moveDist: ConditionalProbabilityTable[GuessRiffleMove, Rational]): Boolean =
    mover(game, fromState).map( mover =>
      if( eqPlayer.eqv(mover, game.player) ) {
        val correctCard = fromState.remaining.head
        moveDist.p(GuessCard(correctCard)) > Rational(0)
      } else {
        true
      }
    ) getOrElse true

  property(s"perfectOptionsPlayerStrategy always has non-zero chance of guessing correctly") = {

    val player = Player("P", "Player")
    val pGame = GuessRiffle(player)

    forAllNoShrink { (seed: Int) =>
      stateStreamMap(
        pGame,
        startState(pGame),
        ((p: Player) => (
          if (eqPlayer.eqv(p, GuessRiffle.dealer))
            GuessRiffle.dealerStrategy
          else
            GuessRiffle.perfectOptionsPlayerStrategy
          ).andThen(Option.apply(_))),
        containsCorrectGuess _,
        Random.generatorFromSeed(Seed(seed)).sync ).get forall { _._2 }
    }
  }

  def isCorrectMoveForState(game: GuessRiffle, state: GuessRiffleState)(move: GuessRiffleMove): Boolean =
    move match {
      case GuessCard(card) => (mover(game, state).map( p => eqPlayer.eqv(p, game.player)).getOrElse(false)) && eqCard.eqv(state.remaining.head, card)
      case _ => true
    }

  def entropyOfGuess(
    game: GuessRiffle,
    fromState: GuessRiffleState,
    moveDist: ConditionalProbabilityTable[GuessRiffleMove, Rational])(
    implicit
    infoConverterDouble: InformationConverter[Double]): Option[UnittedQuantity[Information, Double]] =
    mover(game, fromState).map( mover =>
      if( eqPlayer.eqv(mover, game.player) ) {
        // val correctCard = fromState.remaining.head
        // val isCorrectDist = moveDist.map({ move => move === GuessCard(correctCard) })
        Some(entropy[GuessRiffleMove, Rational](moveDist))
      } else {
        None
      }
    ) getOrElse None

  def probabilityAllCorrect(
    game: GuessRiffle,
    fromState: GuessRiffleState,
    strategies: Player => GuessRiffleState => Option[ConditionalProbabilityTable[GuessRiffleMove, Rational]],
    seed: Int): Rational =
    stateStrategyMoveStream(
      game,
      fromState,
      (p: Player) => (s: GuessRiffleState) => strategies(p)(s),
      Random.generatorFromSeed(Seed(seed)).sync)
    .get
    .filter(args => mover(game, args._1).map( p => eqPlayer.eqv(p, game.player)).getOrElse(false))
    .map({ tuple =>
      val stateIn = tuple._1
      val strategy = tuple._2._1
      monad.map(strategy)(isCorrectMoveForState(game, stateIn))
    })
    .reduce({ (incoming, current) =>
      monad.flatMap(incoming)( a => monad.map(current)( b => a && b ))
    })
    .P(RegionEq(true))

  implicit val doubleField: spire.algebra.Field[Double] = spire.implicits.DoubleAlgebra
  implicit val doubleOrder: cats.kernel.Order[Double] = spire.implicits.DoubleAlgebra
  implicit val infoConverterDouble: InformationConverter[Double] = {
    import axle.jung._
    Information.converterGraphK2[Double, DirectedSparseGraph]
  }
  
  property("perfectOptionsPlayerStrategy's P(all correct) >> that of random mover (except when unshuffled), and its entropy is higher") = {

    val player = Player("P", "Player")
    val game = GuessRiffle(player)

    val rm = {
      (state: GuessRiffleState) =>
        ConditionalProbabilityTable.uniform[GuessRiffleMove, Rational](evGame.moves(game, state))
    } andThen(Option.apply _)

    // leverages the fact that s0 will be the same for both games. Not generally true
    val s0 = startState(game)
    val orderInfoDouble: Order[UnittedQuantity[Information, Double]] = UnittedQuantity.orderUQ[Information, Double]

    forAllNoShrink { (seed: Int) =>

      val s1 = applyMove(game, s0, Riffle())

      val probabilityPerfectChoicesAllCorrect =
        probabilityAllCorrect(
          game,
          s1,
          ((p: Player) => (
            if (eqPlayer.eqv(p, GuessRiffle.dealer))
              GuessRiffle.dealerStrategy.andThen(Option.apply)
            else
              GuessRiffle.perfectOptionsPlayerStrategy.andThen(Option.apply)
            )),
          seed)

      val entropiesP =
        stateStreamMap(
          game,
          s1,
          ((p: Player) => (
            if (eqPlayer.eqv(p, GuessRiffle.dealer))
              GuessRiffle.dealerStrategy
            else
              GuessRiffle.perfectOptionsPlayerStrategy
            ).andThen(Option.apply(_))),
          entropyOfGuess _,
          Random.generatorFromSeed(Seed(seed)).sync 
        ).get.flatMap(_._2).toList

      val ep = Σ(entropiesP)

      val probabilityRandomChoicesAllCorrect =
        probabilityAllCorrect(
          game,
          s1,
          ((p: Player) => (
            if (eqPlayer.eqv(p, GuessRiffle.dealer))
              GuessRiffle.dealerStrategy.andThen(Option.apply)
            else
              rm
            )),
          seed)

      // randomMove
      val entropiesR = stateStreamMap(
        game,
        s1,
        ((p: Player) => (
          if (eqPlayer.eqv(p, GuessRiffle.dealer))
            GuessRiffle.dealerStrategy.andThen(Option.apply(_))
          else
            rm
          )),
        entropyOfGuess _,
        Random.generatorFromSeed(Seed(seed)).sync 
      ).get.flatMap(_._2).toList

      val er = Σ(entropiesR)

      (eqDeck.eqv(s1.initialDeck, s1.riffledDeck.get) &&
        eqRational.eqv(probabilityPerfectChoicesAllCorrect, probabilityRandomChoicesAllCorrect) &&
        orderInfoDouble.eqv(ep, er)) || {
        (probabilityPerfectChoicesAllCorrect > probabilityRandomChoicesAllCorrect) && orderInfoDouble.lt(ep, er)
      }
    }
  }

}

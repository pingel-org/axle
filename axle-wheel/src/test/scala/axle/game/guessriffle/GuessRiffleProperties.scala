package axle.game.guessriffle

import org.scalacheck.Properties
import org.scalacheck.Prop.forAllNoShrink

import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

//import spire.math._
import spire.random.Random
import spire.random.Seed
import spire.math.Rational

import axle.algebra.RegionEq
import axle.math.Σ
import axle.game.guessriffle.evGame._
import axle.probability._
import axle.stats._
import axle.game._
import axle.quanta._
import axle.syntax.kolmogorov._

class GuessRiffleProperties extends Properties("GuessRiffle Properties") {

  val monad = ConditionalProbabilityTable.monadWitness[Rational]

  def containsCorrectGuess(game: GuessRiffle, fromState: GuessRiffleState, moveDist: ConditionalProbabilityTable[GuessRiffleMove, Rational]): Boolean =
    mover(game, fromState).map( mover =>
      if( mover === game.player ) {
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
        ((p: Player) => GuessRiffle.perfectOptionsPlayerStrategy.andThen(Option.apply(_))),
        containsCorrectGuess _,
        Random.generatorFromSeed(Seed(seed)).sync ).get forall { _._2 }
    }
  }

  def isCorrectMoveForState(game: GuessRiffle, state: GuessRiffleState)(move: GuessRiffleMove): Boolean =
    move match {
      case GuessCard(card) => (mover(game, state).map( _ === game.player).getOrElse(false)) && state.remaining.head === card
      case _ => true
    }

  def entropyOfGuess(
    game: GuessRiffle,
    fromState: GuessRiffleState,
    moveDist: ConditionalProbabilityTable[GuessRiffleMove, Rational])(
    implicit
    infoConverterDouble: InformationConverter[Double]): Option[UnittedQuantity[Information, Double]] =
    mover(game, fromState).map( mover =>
      if( mover === game.player ) {
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
    strategies: Player => GuessRiffleState => ConditionalProbabilityTable[GuessRiffleMove, Rational],
    seed: Int): Rational =
    stateStrategyMoveStream(
      game,
      fromState,
      (p: Player) => (s: GuessRiffleState) => Option(strategies(p)(s)),
      Random.generatorFromSeed(Seed(seed)).sync)
    .get
    .filter(args => mover(game, args._1).map( _ === game.player).getOrElse(false))
    .map({ case (stateIn, (strategy, _), _) =>
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

    // leverages the fact that s0 will be the same for both games. Not generally true
    val s0 = startState(game)

    forAllNoShrink { (seed: Int) =>

      val s1 = applyMove(game, s0, Riffle())

      // val perfectStrategies: Player => GuessRiffleState => ConditionalProbabilityTable[GuessRiffleMove, Rational] = 
      //   _ => GuessRiffle.perfectOptionsPlayerStrategy

      val probabilityPerfectChoicesAllCorrect = probabilityAllCorrect(game, s1, _ => GuessRiffle.perfectOptionsPlayerStrategy, seed)

      val entropiesP = stateStreamMap(
        game,
        s1,
        _ => GuessRiffle.perfectOptionsPlayerStrategy.andThen(Option.apply _),
        entropyOfGuess _,
        Random.generatorFromSeed(Seed(seed)).sync 
      ).get.flatMap(_._2).toList

      val ep = Σ(entropiesP)

      // val randomStrategies: Player => GuessRiffleState => ConditionalProbabilityTable[GuessRiffleMove, Rational] = 
      //   _ => GuessRiffle.perfectOptionsPlayerStrategy

      val probabilityRandomChoicesAllCorrect = probabilityAllCorrect(game, s1, _ => GuessRiffle.perfectOptionsPlayerStrategy, seed)

      // randomMove
      val entropiesR = stateStreamMap(
        game,
        s1,
        _ => GuessRiffle.perfectOptionsPlayerStrategy.andThen(Option.apply _),
        entropyOfGuess _,
        Random.generatorFromSeed(Seed(seed)).sync 
      ).get.flatMap(_._2).toList

      val er = Σ(entropiesR)

      (s1.initialDeck === s1.riffledDeck.get && probabilityPerfectChoicesAllCorrect === probabilityRandomChoicesAllCorrect && ep === er) || {
        (probabilityPerfectChoicesAllCorrect > probabilityRandomChoicesAllCorrect) && (ep < er)
      }
    }
  }

}

package axle

import scala.Stream.cons
import spire.random.Generator
import axle.stats.rationalProbabilityDist

package object game {

  def moveStateStream[G, S, O, M, MS, MM](
    game: G,
    fromState: S,
    gen: Generator)(
      implicit evGame: Game[G, S, O, M, MS, MM]): Stream[(S, M, S)] =
    evGame.mover(game, fromState).map(mover => {
      val strategy = evGame.strategyFor(game, mover)
      val move = strategy(game, evGame.maskState(game, fromState, mover)).observe(gen)
      val toState = evGame.applyMove(game, fromState, move)
      cons((fromState, move, toState), moveStateStream(game, toState, gen))
    }) getOrElse {
      Stream.empty
    }

  def play[G, S, O, M, MS, MM](game: G)(
    implicit evGame: Game[G, S, O, M, MS, MM],
    evGameIO: GameIO[G, O, M, MS, MM]): S =
    play(game, evGame.startState(game), true)

  def play[G, S, O, M, MS, MM](
    game: G,
    start: S,
    intro: Boolean = true)(
      implicit evGame: Game[G, S, O, M, MS, MM],
      evGameIO: GameIO[G, O, M, MS, MM]): S = {

    evGame.players(game) foreach { observer =>
      val display = evGameIO.displayerFor(game, observer)
      if (intro) {
        display(evGameIO.introMessage(game))
      }
      display(evGameIO.displayStateTo(game, evGame.maskState(game, start, observer), observer))
    }

    val lastState = moveStateStream(game, start) map {
      case (fromState, move, toState) => {
        val mover = evGame.mover(game, fromState)
        mover foreach { mover =>
          evGame.players(game) foreach { observer =>
            val display = evGameIO.displayerFor(game, observer)
            display(evGameIO.displayMoveTo(game, evGame.maskMove(game, move, mover, observer), mover, observer))
            display(evGameIO.displayStateTo(game, evGame.maskState(game, toState, observer), observer))
          }
        }
        toState
      }
    } last

    evGame.players(game) foreach { observer =>
      val display = evGameIO.displayerFor(game, observer)
      display("")
      display(evGameIO.displayStateTo(game, evGame.maskState(game, lastState, observer), observer))
      evGame.outcome(game, lastState) foreach { outcome =>
        display(evGameIO.displayOutcomeTo(game, outcome, observer))
      }
    }

    lastState
  }

  def gameStream[G, S, O, M, MS, MM](
    game: G,
    start: S,
    intro: Boolean = true)(
      implicit evGame: Game[G, S, O, M, MS, MM],
      evGameIO: GameIO[G, O, M, MS, MM]): Stream[S] = {
    val end = play(game, start, intro)
    cons(end, gameStream(game, evGame.startFrom(game, end).get, false))
  }

  def playContinuously[G, S, O, M, MS, MM](
    game: G,
    start: S)(
      implicit evGame: Game[G, S, O, M, MS, MM],
      evGameIO: GameIO[G, O, M, MS, MM]): S =
    gameStream(game, start).last

}

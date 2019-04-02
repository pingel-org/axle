package axle

import scala.Stream.cons

import cats.kernel.Order

import spire.algebra.Ring
import spire.random.Generator
import spire.random.Dist

import axle.stats.ProbabilityModel
import axle.syntax.probabilitymodel._

package object game {

  def moveStateStream[G, S, O, M, MS, MM, V, PM[_, _]](
    game:      G,
    fromState: S,
    gen:       Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM, V, PM],
    prob:   ProbabilityModel[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): Stream[(S, M, S)] =
    evGame.mover(game, fromState).map(mover => {
      val strategyFn = evGame.strategyFor(game, mover)
      val strategy = strategyFn(game, evGame.maskState(game, fromState, mover))
      val move = strategy.observe(gen)
      val toState = evGame.applyMove(game, fromState, move)
      cons((fromState, move, toState), moveStateStream(game, toState, gen))
    }) getOrElse {
      Stream.empty
    }

  def play[G, S, O, M, MS, MM, V, PM[_, _]](game: G, gen: Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM, V, PM],
    prob:     ProbabilityModel[PM],
    evGameIO: GameIO[G, O, M, MS, MM],
    distV:    Dist[V],
    ringV:    Ring[V],
    orderV:   Order[V]): S =
    play(game, evGame.startState(game), true, gen)

  def play[G, S, O, M, MS, MM, V, PM[_, _]](
    game:  G,
    start: S,
    intro: Boolean   = true,
    gen:   Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM, V, PM],
    prob:     ProbabilityModel[PM],
    evGameIO: GameIO[G, O, M, MS, MM],
    distV: Dist[V],
    ringV: Ring[V],
    orderV: Order[V]): S = {

    evGame.players(game) foreach { observer =>
      val display = evGameIO.displayerFor(game, observer)
      if (intro) {
        display(evGameIO.introMessage(game))
      }
      display(evGameIO.displayStateTo(game, evGame.maskState(game, start, observer), observer))
    }

    val mss: Stream[(S, M, S)] = moveStateStream(game, start, gen)

    val lastState = mss map {
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

  def gameStream[G, S, O, M, MS, MM, V, PM[_, _]](
    game:  G,
    start: S,
    intro: Boolean   = true,
    gen:   Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM, V, PM],
    prob:     ProbabilityModel[PM],
    evGameIO: GameIO[G, O, M, MS, MM],
    distV:    Dist[V],
    ringV:    Ring[V],
    orderV:   Order[V]): Stream[S] = {
    val end = play(game, start, intro, gen)
    cons(end, gameStream(game, evGame.startFrom(game, end).get, false, gen))
  }

  def playContinuously[G, S, O, M, MS, MM, V, PM[_, _]](
    game:  G,
    start: S,
    gen:   Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM, V, PM],
    prob:     ProbabilityModel[PM],
    evGameIO: GameIO[G, O, M, MS, MM],
    distV:    Dist[V],
    ringV:    Ring[V],
    orderV:   Order[V]): S =
    gameStream(game, start, true, gen).last

}

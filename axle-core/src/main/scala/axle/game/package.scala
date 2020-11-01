package axle

import cats.Monad
import cats.kernel.Order

import spire.algebra.Field
import spire.algebra.Ring
import spire.random.Generator
import spire.random.Dist
import spire.implicits.additiveGroupOps

import axle.algebra._
import axle.probability._
import axle.syntax.sampler._

package object game {

  def moveStateStream[G, S, O, M, MS, MM, V, PM[_, _]](
    game:      G,
    fromState: S,
    strategies: Player => (G, MS) => PM[M, V],
    gen:       Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): LazyList[(S, M, S)] =
    evGame.mover(game, fromState).map { mover => {
      val strategyFn: (G, MS) => PM[M, V] = strategies(mover)
      val strategy = strategyFn(game, evGame.maskState(game, fromState, mover))
      val move = strategy.sample(gen)
      val toState = evGame.applyMove(game, fromState, move)
      LazyList.cons((fromState, move, toState), moveStateStream(game, toState, strategies, gen))
    }} getOrElse {
      LazyList.empty
    }

  def moveFromRandomState[G, S, O, M, MS, MM, V, PM[_, _]](
    game:      G,
    stateModel: PM[S, V],
    strategies: Player => (G, MS) => PM[M, V],
    mapToProb: Map[S, V] => PM[S, V], // TODO replace this
    gen:       Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    kolm:   Kolmogorov[PM],
    bayes:  Bayes[PM],
    monad:  Monad[PM[?, V]],
    eqS:    cats.kernel.Eq[S],
    eqM:    cats.kernel.Eq[M],
    distV:  Dist[V],
    fieldV: Field[V],
    orderV: Order[V]): (Option[(S, M)], PM[S, V]) = {

    val openStateModel: PM[S, V] = bayes.filter(stateModel)(RegionIf(evGame.mover(game, _).isDefined))

    val fromState: S = prob.sample(openStateModel)(gen)
    // val probabilityOfFromState: V = prob.probabilityOf(stateModel)(RegionEq(fromState))

    evGame.mover(game, fromState).map { mover => {
      val strategyFn = strategies(mover)
      val strategy = strategyFn(game, evGame.maskState(game, fromState, mover))
      val move = strategy.sample(gen)
      val toState = evGame.applyMove(game, fromState, move)

      import cats.syntax.all._
      if( fromState === toState ) {
        (Some((fromState, move)), stateModel)
      } else {
        val probabilityOfMove: V = kolm.probabilityOf(strategy)(RegionEq(move))
        // val mass = probabilityOfFromState * probabilityOfMove // TODO scale mass down
        val redistributed = monad.flatMap(stateModel)( s =>
          if( s === fromState) {
            mapToProb(Map(fromState -> (Field[V].one - probabilityOfMove), toState -> probabilityOfMove))
          } else {
            monad.pure(s)
          })
        (Some((fromState, move)), redistributed)
      }
    }} getOrElse {
      (None, stateModel)
    }
  }
  
  def stateStreamMap[G, S, O, M, MS, MM, V, PM[_, _], T](
    game:        G,
    fromState:   S,
    strategies: Player => (G, MS) => PM[M, V],
    strategyToT: (G, S, PM[M, V]) => T,
    gen:         Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): LazyList[(S, T, S)] =
    evGame.mover(game, fromState).map { mover => {
      val strategyFn = strategies(mover)
      val strategy = strategyFn(game, evGame.maskState(game, fromState, mover))
      val move = strategy.sample(gen)
      val toState = evGame.applyMove(game, fromState, move)
      LazyList.cons((fromState, strategyToT(game, fromState, strategy), toState), stateStreamMap(game, toState, strategies, strategyToT, gen))
    }} getOrElse {
      LazyList.empty
    }

  def stateStrategyMoveStream[G, S, O, M, MS, MM, V, PM[_, _]](
    game:        G,
    fromState:   S,
    strategies: Player => (G, MS) => PM[M, V],
    gen:         Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): LazyList[(S, PM[M, V], M, S)] =
    evGame.mover(game, fromState).map { mover => {
      val strategyFn = strategies(mover)
      val strategy = strategyFn(game, evGame.maskState(game, fromState, mover))
      val move = strategy.sample(gen)
      val toState = evGame.applyMove(game, fromState, move)
      LazyList.cons((fromState, strategy, move, toState), stateStrategyMoveStream(game, toState, strategies, gen))
    }} getOrElse {
      LazyList.empty
    }
    
  def play[G, S, O, M, MS, MM, V, PM[_, _], F[_]](
    game: G,
    strategies: Player => (G, MS) => PM[M, V],
    playerToDisplayer: Player => String => F[Unit],
    gen: Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    prob:     Sampler[PM],
    evGameIO: GameIO[G, O, M, MS, MM],
    distV:    Dist[V],
    ringV:    Ring[V],
    orderV:   Order[V]): S =
    play(game, strategies, playerToDisplayer, evGame.startState(game), true, gen)

  def play[G, S, O, M, MS, MM, V, PM[_, _], F[_]](
    game:  G,
    strategies: Player => (G, MS) => PM[M, V],
    playerToDisplayer: Player => String => F[Unit],
    start: S,
    intro: Boolean   = true,
    gen:   Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    prob:     Sampler[PM],
    evGameIO: GameIO[G, O, M, MS, MM],
    distV: Dist[V],
    ringV: Ring[V],
    orderV: Order[V]): S = {

    evGame.players(game) foreach { observer =>
      val display = playerToDisplayer(observer)
      if (intro) {
        display(evGameIO.introMessage(game))
      }
      display(evGameIO.displayStateTo(game, evGame.maskState(game, start, observer), observer))
    }

    val mss: LazyList[(S, M, S)] = moveStateStream(game, start, strategies, gen)

    val lastState = mss map {
      case (fromState, move, toState) => {
        val mover = evGame.mover(game, fromState)
        mover foreach { mover =>
          evGame.players(game) foreach { observer =>
            val display = playerToDisplayer(observer)
            display(evGameIO.displayMoveTo(game, evGame.maskMove(game, move, mover, observer), mover, observer))
            display(evGameIO.displayStateTo(game, evGame.maskState(game, toState, observer), observer))
          }
        }
        toState
      }
    } last

    evGame.players(game) foreach { observer =>
      val display = playerToDisplayer(observer)
      display("")
      display(evGameIO.displayStateTo(game, evGame.maskState(game, lastState, observer), observer))
      evGame.outcome(game, lastState) foreach { outcome =>
        display(evGameIO.displayOutcomeTo(game, outcome, observer))
      }
    }

    lastState
  }

  def gameStream[G, S, O, M, MS, MM, V, PM[_, _], F[_]](
    game:  G,
    strategies: Player => (G, MS) => PM[M, V],
    playerToDisplayer: Player => String => F[Unit],
    start: S,
    intro: Boolean   = true,
    gen:   Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    prob:     Sampler[PM],
    evGameIO: GameIO[G, O, M, MS, MM],
    distV:    Dist[V],
    ringV:    Ring[V],
    orderV:   Order[V]): LazyList[S] = {
    val end = play(game, strategies, playerToDisplayer, start, intro, gen)
    LazyList.cons(end, gameStream(game, strategies, playerToDisplayer, evGame.startFrom(game, end).get, false, gen))
  }

  def playContinuously[G, S, O, M, MS, MM, V, PM[_, _], F[_]](
    game:  G,
    strategies: Player => (G, MS) => PM[M, V],
    playerToDisplayer: Player => String => F[Unit],
    start: S,
    gen:   Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    prob:     Sampler[PM],
    evGameIO: GameIO[G, O, M, MS, MM],
    distV:    Dist[V],
    ringV:    Ring[V],
    orderV:   Order[V]): S =
    gameStream(game, strategies, playerToDisplayer, start, true, gen).last

}

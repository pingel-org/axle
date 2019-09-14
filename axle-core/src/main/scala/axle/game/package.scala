package axle

import scala.Stream.cons

import cats.kernel.Order

import spire.algebra.Field
import spire.algebra.Ring
import spire.random.Generator
import spire.random.Dist
import spire.implicits.additiveSemigroupOps
import spire.implicits.multiplicativeSemigroupOps

// import axle.algebra.Region
// import axle.algebra.RegionSet
// import axle.stats.Variable
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

  def moveFromRandomState[G, S, O, M, MS, MM, V, PM[_, _]](
    game:      G,
    stateModel: PM[S, V],
    gen:       Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM, V, PM],
    prob:   ProbabilityModel[PM],
    eqS:    cats.kernel.Eq[S],
    eqM:    cats.kernel.Eq[M],
    distV:  Dist[V],
    fieldV: Field[V],
    orderV: Order[V]): (Option[(S, M)], PM[S, V]) = {

    val openStateModel: PM[S, V] = prob.filter(stateModel)((s: S) => evGame.mover(game, s).isDefined)

    val fromState: S = prob.observe(openStateModel)(gen)
    val probabilityOfFromState: V = prob.probabilityOf(stateModel)(eqS.eqv(_, fromState))

    evGame.mover(game, fromState).map(mover => {
      val strategyFn = evGame.strategyFor(game, mover)
      val strategy = strategyFn(game, evGame.maskState(game, fromState, mover))
      val move = strategy.observe(gen)
      val probabilityOfMove: V = prob.probabilityOf(strategy)(eqM.eqv(_, move))
      val toState = evGame.applyMove(game, fromState, move)

      import cats.syntax.all._
      if( fromState === toState ) {
        (Some((fromState, move)), stateModel)
      } else {
        // TODO this Map is not efficient
        val updateM: Map[S, V] = Map(fromState -> fieldV.negate(probabilityOfMove), toState -> probabilityOfMove)
        val updatingModel: PM[S, V] = ??? // prob.construct(Variable[S]("S"), RegionSet(updateM.keySet), (rS: Region[S]) => updateM.find(kv => rS(kv._1)).get._2) 
        // Note that updatingModel violates probability axioms
        val adjoined = prob.adjoin(stateModel)(updatingModel)
        import axle.algebra.tuple2Field
        val mapped = prob.mapValues[S, (V, V), V](adjoined)({ case (v1, v2) => 
          v1 + (probabilityOfFromState * v2)
        })
        (Some((fromState, move)), mapped)
      }
    }) getOrElse {
      (None, stateModel)
    }
  }
  
  def stateStreamMap[G, S, O, M, MS, MM, V, PM[_, _], T](
    game:        G,
    fromState:   S,
    strategyToT: (G, S, PM[M, V]) => T,
    gen:         Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM, V, PM],
    prob:   ProbabilityModel[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): Stream[(S, T, S)] =
    evGame.mover(game, fromState).map(mover => {
      val strategyFn = evGame.strategyFor(game, mover)
      val strategy = strategyFn(game, evGame.maskState(game, fromState, mover))
      val move = strategy.observe(gen)
      val toState = evGame.applyMove(game, fromState, move)
      cons((fromState, strategyToT(game, fromState, strategy), toState), stateStreamMap(game, toState, strategyToT, gen))
    }) getOrElse {
      Stream.empty
    }

  def stateStrategyMoveStream[G, S, O, M, MS, MM, V, PM[_, _]](
    game:        G,
    fromState:   S,
    gen:         Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM, V, PM],
    prob:   ProbabilityModel[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): Stream[(S, PM[M, V], M, S)] =
    evGame.mover(game, fromState).map(mover => {
      val strategyFn = evGame.strategyFor(game, mover)
      val strategy = strategyFn(game, evGame.maskState(game, fromState, mover))
      val move = strategy.observe(gen)
      val toState = evGame.applyMove(game, fromState, move)
      cons((fromState, strategy, move, toState), stateStrategyMoveStream(game, toState, gen))
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

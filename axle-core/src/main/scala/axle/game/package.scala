package axle

import cats.Monad
import cats.kernel.Order
import cats.effect.Sync
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring
import spire.random.Generator
import spire.random.Dist
import spire.implicits.additiveGroupOps

import axle.algebra._
import axle.probability._
import axle.syntax.sampler._

package object game {

  def userInput[G, S, O, M, MS, MM,
    F[_]: cats.effect.Sync](
    game: G,
    state: MS,
    reader: () => F[String],
    writer: String => F[Unit]
  )(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    evGameIO: GameIO[G, O, M, MS, MM]
  ): F[M] = {

    val fInput = for {
      _ <- writer("Enter move: ")
      input <- reader()
      _ <- writer(input)
    } yield input

    val fEitherCPT: F[Either[String, M]] =
      fInput.map { input => 
        evGameIO.parseMove(game, input).flatMap { parsedMove => {
          evGame.isValid(game, state, parsedMove)
        }}
      }

    fEitherCPT.flatMap(_.map(Monad[F].pure).getOrElse(userInput(game, state, reader, writer)))
  }

  def interactiveMove[G, S, O, M, MS, MM, F[_]: Sync](
      game: G,
      player: Player,
      reader: () => F[String],
      writer: String => F[Unit]
    )(implicit
      evGame:   Game[G, S, O, M, MS, MM],
      evGameIO: GameIO[G, O, M, MS, MM]
    ): MS => F[M] =
      (state: MS) =>
        for {
           _ <- writer(evGameIO.displayStateTo(game, state, player))
          move <- userInput(game, state, reader, writer)
        } yield move

  def nextMoveState[
    G, S, O, M, MS, MM, V,
    PM[_, _],
    F[_]: Monad](
    game:      G,
    fromState: S,
    strategies: Player => MS => F[PM[M, V]],
    gen:       Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): F[Option[(S, M, S)]] =
    evGame.mover(game, fromState) map { mover => {
      val strategyFn: MS => F[PM[M, V]] = strategies(mover)
      val fStrategy: F[PM[M, V]] = strategyFn(evGame.maskState(game, fromState, mover))
      fStrategy map { strategy =>
        val move: M = strategy.sample(gen)
        val toState: S = evGame.applyMove(game, fromState, move)
        Option((fromState, move, toState))
      }
    }} getOrElse(Monad[F].pure(Option.empty[(S, M, S)]))


  def lastState[
    G, S, O, M, MS, MM, V,
    PM[_, _],
    F[_]: Monad](
    game:      G,
    fromState: S,
    strategies: Player => MS => F[PM[M, V]],
    gen:       Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): F[Option[(S, M, S)]] =
    foled[S, (S, M, S), F](
      fromState,
      (s: S) => nextMoveState(game, s, strategies, gen),
      _._3)

  def moveStateStream[
    G, S, O, M, MS, MM, V,
    PM[_, _],
    F[_]: Monad](
    game:      G,
    fromState: S,
    strategies: Player => MS => F[PM[M, V]],
    gen:       Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): F[LazyList[(S, M, S)]] =
    chain[S, (S, M, S), F, LazyList](
      fromState,
      (s: S) => nextMoveState(game, s, strategies, gen),
      _._3,
      LazyList.empty,
      b => ll => ll.prepended(b)
    )

  def moveFromRandomState[
    G, S, O, M, MS, MM, V,
    PM[_, _],
    F[_]: Monad](
    game:      G,
    stateModel: PM[S, V],
    strategies: Player => MS => F[PM[M, V]],
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
    orderV: Order[V]): F[(Option[(S, M)], PM[S, V])] = {

    val openStateModel: PM[S, V] = bayes.filter(stateModel)(RegionIf(evGame.mover(game, _).isRight))

    val fromState: S = prob.sample(openStateModel)(gen)
    // val probabilityOfFromState: V = prob.probabilityOf(stateModel)(RegionEq(fromState))

    evGame.mover(game, fromState).map { mover => {
      val strategyFn = strategies(mover)
      val fStrategy: F[PM[M, V]] = strategyFn(evGame.maskState(game, fromState, mover))
      fStrategy map { strategy =>

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
          (Option((fromState, move)), redistributed)
        }
      }
    }} getOrElse {
      Monad[F].pure[(Option[(S, M)], PM[S, V])]((None, stateModel))
    }
  }
  
  def mapNextState[
    G, S, O, M, MS, MM, V,
    PM[_, _], T,
    F[_]: Monad](
    game:        G,
    fromState:   S,
    strategies: Player => MS => F[PM[M, V]],
    strategyToT: (G, S, PM[M, V]) => T,
    gen:         Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    distV: Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): F[Option[(S, T, S)]] =
    evGame.mover(game, fromState).map { mover => {
      val strategyFn = strategies(mover)
      val fStrategy: F[PM[M, V]] = strategyFn(evGame.maskState(game, fromState, mover))
      fStrategy map { strategy =>
        val move = strategy.sample(gen)
        val toState = evGame.applyMove(game, fromState, move)
        Option((fromState, strategyToT(game, fromState, strategy), toState))
      }
  }} getOrElse(Monad[F].pure(Option.empty[(S, T, S)]))

  def stateStreamMap[
    G, S, O, M, MS, MM, V,
    PM[_, _], T,
    F[_]: Monad](
    game:        G,
    fromState:   S,
    strategies: Player => MS => F[PM[M, V]],
    strategyToT: (G, S, PM[M, V]) => T,
    gen:         Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    distV: Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): F[LazyList[(S, T, S)]] =
    chain[S, (S, T, S), F, LazyList](
      fromState,
      (s: S) => mapNextState(game, s, strategies, strategyToT, gen),
      _._3,
      LazyList.empty,
      b => ll => ll.prepended(b)
    )

  def nextStateStrategyMoveState[
    G, S, O, M, MS, MM, V,
    PM[_, _],
    F[_]: Monad](
    game:        G,
    fromState:   S,
    strategies: Player => MS => F[PM[M, V]],
    gen:         Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): F[Option[(S, (PM[M, V], M), S)]] =
    evGame.mover(game, fromState).map { mover => {
      val strategyFn = strategies(mover)
      val fStrategy: F[PM[M, V]] = strategyFn(evGame.maskState(game, fromState, mover))
      fStrategy map { strategy =>
        val move = strategy.sample(gen)
        val toState = evGame.applyMove(game, fromState, move)
        Option((fromState, (strategy, move), toState))
      }
  }} getOrElse(Monad[F].pure(Option.empty))

  def stateStrategyMoveStream[
    G, S, O, M, MS, MM, V,
    PM[_, _],
    F[_]: Monad](
    game:        G,
    fromState:   S,
    strategies: Player => MS => F[PM[M, V]],
    gen:         Generator)(
    implicit
    evGame: Game[G, S, O, M, MS, MM],
    prob:   Sampler[PM],
    distV:  Dist[V],
    ringV:  Ring[V],
    orderV: Order[V]): F[LazyList[(S, (PM[M, V], M), S)]] =
    chain[S, (S, (PM[M, V], M), S), F, LazyList](
      fromState,
      (s: S) => nextStateStrategyMoveState(game, s, strategies, gen),
      _._3,
      LazyList.empty,
      b => ll => ll.prepended(b)
    )

  def play[G, S, O, M, MS, MM, V, PM[_, _], F[_]: Monad](
    game: G,
    strategies: Player => MS => F[PM[M, V]],
    gen: Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    prob:     Sampler[PM],
    distV:    Dist[V],
    ringV:    Ring[V],
    orderV:   Order[V]): F[S] =
    play(game, strategies, evGame.startState(game), gen)

  def play[G, S, O, M, MS, MM, V, PM[_, _], F[_]: Monad](
    game:  G,
    strategies: Player => MS => F[PM[M, V]],
    start: S,
    gen:   Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    prob:     Sampler[PM],
    distV: Dist[V],
    ringV: Ring[V],
    orderV: Order[V]): F[S] =
    lastState(game, start, strategies, gen) map { lastTripleOpt =>
      val lastState = lastTripleOpt.get._3 // NOTE Option.get
      lastState
    }

  def playWithIntroAndOutcomes[G, S, O, M, MS, MM, V, PM[_, _], F[_]: Monad](
    game:  G,
    strategies: Player => MS => F[PM[M, V]],
    start: S,
    playerToWriter: Player => String => F[()],
    gen:   Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    evGameIO: GameIO[G, O, M, MS, MM],
    prob:     Sampler[PM],
    distV: Dist[V],
    ringV: Ring[V],
    orderV: Order[V]): F[S] =
    for {
      _ <- evGame.players(game).map { player =>
             playerToWriter(player)(evGameIO.introMessage(game))
           }.last
      endState <- play(game, strategies, start, gen)
      _ <- evGame.players(game).map { player =>
             playerToWriter(player)(evGame.mover(game, endState).swap.map(outcome => evGameIO.displayOutcomeTo(game, outcome, player)).getOrElse(""))
           }.last
    } yield endState

  def gameStream[G, S, O, M, MS, MM, V, PM[_, _], F[_]: Monad](
    game:  G,
    strategies: Player => MS => F[PM[M, V]],
    start: S,
    continue: Int => Boolean,
    gen:   Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    prob:     Sampler[PM],
    distV:    Dist[V],
    ringV:    Ring[V],
    orderV:   Order[V]): F[LazyList[S]] =
    chain[(S, Int), (S, Int), F, LazyList](
      (start, 0),
      (si: (S, Int)) => {
        play(game, strategies, si._1, gen) map { end =>
          if(continue(si._2)) {
            evGame.startFrom(game, end).map { s => (s, si._2 + 1) }
          } else {
            Option.empty
          }
        }},
      s => s,
     LazyList.empty,
     b => ll => ll.prepended(b)
    ) map { _.map { _._1 } }

  def playContinuously[G, S, O, M, MS, MM, V, PM[_, _], F[_]: Monad](
    game:  G,
    strategies: Player => MS => F[PM[M, V]],
    start: S,
    gen:   Generator)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    prob:     Sampler[PM],
    distV:    Dist[V],
    ringV:    Ring[V],
    orderV:   Order[V]): F[S] =
    gameStream(game, strategies, start, _ => true, gen).map(_.last)

}

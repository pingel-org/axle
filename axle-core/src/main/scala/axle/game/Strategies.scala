package axle.game

import scala.annotation.nowarn

import cats.Monad
import cats.kernel.Order
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring
import spire.math.ConvertableTo

object Strategies {

  def outcomeRingHeuristic[G, S, O, M, MS, MM, V, N: Ring, PM[_, _]](
    game: G,
    f: (O, Player) => N
    )(implicit
      evGame: Game[G, S, O, M, MS, MM]): S => Map[Player, N] =
    (state: S) => evGame.players(game).map(p => {
      val score: N = evGame.mover(game, state).swap.map(o => { f(o, p): N }).getOrElse(Ring[N].zero)
      (p, score)
    }).toMap

  def aiMover[G, S, O, M, MS, MM, N: Order](
    game: G,
    unmask: MS => S,
    lookahead: Int,
    heuristic: S => Map[Player, N])(
    implicit
    evGame: Game[G, S, O, M, MS, MM]
    ): MS => M =
    (state: MS) => {
      val (move, newState, values) = minimax(game, unmask(state), lookahead, heuristic)
      move
    }

  def hardCodedStringStrategy[G, S, O, M, MS, MM](
      game: G
    )(
    input: (G, MS) => String)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    evGameIO: GameIO[G, O, M, MS, MM]
    ): MS => M =
    (state: MS) => {
      val parsed = evGameIO.parseMove(game, input(game, state)).toOption.get
      val validated = evGame.isValid(game, state, parsed)
      validated.toOption.get
    }

  def fuzzStrategy[
    M, MS,
    V: Field: Order,
    PM[_, _]](
      strategy: MS => M
    )(implicit
      monadPM: Monad[PM[?, V]]
    ): MS => PM[M, V] =
      (state: MS) => monadPM.pure(strategy(state))

  import axle.probability.ConditionalProbabilityTable
  def randomMove[
    G, S, O, M, MS, MM,
    V: Order: Field: ConvertableTo,
    PM[_, _]](game: G)(
    implicit
    evGame: Game[G, S, O, M, MS, MM]): MS => ConditionalProbabilityTable[M, V] =
    (state: MS) => {
      val opens = evGame.moves(game, state).toVector
      val p = Field[V].reciprocal(ConvertableTo[V].fromInt(opens.length))
      ConditionalProbabilityTable[M, V](opens.map(open => open -> p).toMap)
    }

  /**
   * Given a game and state, minimax returns the move and resulting state that maximizes
   * the outcome for the state's mover, assuming that other players also follow the minimax
   * strategy through the given depth.  Beyond that depth (or when a terminal state is encountered),
   * the heuristic function is applied to the state.
   *
   * The third return value is a Map of Player to estimated best value from the returned state.
   */

  def minimax[G, S, O, M, MS, MM, V, N: Order](
    game:      G,
    state:     S,
    depth:     Int,
    heuristic: S => Map[Player, N])(
    implicit
    evGame: Game[G, S, O, M, MS, MM]): (M, S, Map[Player, N]) = {

    // TODO capture as type constraint
    assert(evGame.mover(game, state).isRight)

    val mover = evGame.mover(game, state).right.get : @nowarn // TODO .get
    val ms = evGame.maskState(game, state, mover) // TODO move this elsewhere
    val moveValue = evGame.moves(game, ms).map(move => {
      val newState = evGame.applyMove(game, state, move)
      // if (evGame.outcome(game, newState).isDefined || depth == 0) {
      //   (move, state, heuristic(newState))
      // } else {
      //   (move, state, minimax(game, newState, depth - 1, heuristic)._3)
      // }
      evGame.mover(game, newState).map { mover =>
        // || depth == 0
        (move, state, minimax(game, newState, depth - 1, heuristic)._3)
      } getOrElse {
        (move, state, heuristic(newState))
      }
    })
    moveValue.maxBy(mcr => (mcr._3)(mover))
  }

  /**
   * α-β pruning generalized for N-player non-zero-sum games
   *
   * 2-player zero-sum version described at:
   *
   *   http://en.wikipedia.org/wiki/Alpha-beta_pruning
   *
   */

  def alphabeta[G, S, O, M, MS, MM, V, N: Order, PM[_, _]](
    game:      G,
    state:     S,
    depth:     Int,
    heuristic: S => Map[Player, N])(
    implicit
    evGame: Game[G, S, O, M, MS, MM]): (M, Map[Player, N]) =
    _alphabeta(game, state, depth, Map.empty, heuristic)

  def _alphabeta[G, S, O, M, MS, MM, V, N: Order, PM[_, _]](
    game:      G,
    state:     S,
    depth:     Int,
    cutoff:    Map[Player, N],
    heuristic: S => Map[Player, N])(
    implicit
    evGame: Game[G, S, O, M, MS, MM]): (M, Map[Player, N]) = {

    assert(evGame.mover(game, state).isRight && depth > 0) // TODO capture as type constraint

    //      val initial = AlphaBetaFold(game, dummy[M], cutoff, false)
    //      val ms = evGame.maskState(game, state, ???) // TODO move this elsewhere
    //      val result = evGame.moves(game, ms).foldLeft(initial)(_.process(_, state, heuristic))
    //      (result.move, result.cutoff)
    ???
  }

}

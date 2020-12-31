package axle.game

import cats.kernel.Order
import cats.implicits._

import spire.algebra.Ring

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
      val (moveOpt, values) = minimax(game, unmask(state), lookahead, heuristic)
      moveOpt.get // TODO guarantee that this doesn't throw using types
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

  /**
   * Given a game and state, minimax returns the move and resulting state that maximizes
   * the outcome for the state's mover, assuming that other players also follow the minimax
   * strategy through the given depth.  Beyond that depth (or when a terminal state is encountered),
   * the heuristic function is applied to the state.
   *
   * The third return value is a Map of Player to estimated best value from the returned state.
   */

  def minimax[G, S, O, M, MS, MM, N: Order](
    game:      G,
    state:     S,
    depth:     Int,
    heuristic: S => Player => N)(
    implicit
    evGame: Game[G, S, O, M, MS, MM]): (Option[M], Player => N) =
    if( depth == 0 ) {
      (None, heuristic(state))
    } else {
      evGame.mover(game, state).fold(
        outcome => (None, heuristic(state)),
        mover =>
          evGame.moves(game, evGame.maskState(game, state, mover)).map(move => {
            (Option(move),
              minimax(game, evGame.applyMove(game, state, move), depth - 1, heuristic)._2)
          }).maxBy(mcr => (mcr._2)(mover))
      )
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

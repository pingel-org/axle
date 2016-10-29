package axle.game

import scala.Stream.cons
import scala.util.Random.nextInt

import spire.algebra.Eq
import spire.algebra.Order
import spire.algebra.Ring
import spire.compat.ordering
import spire.implicits.eqOps

object Strategies {

  def outcomeRingHeuristic[G, S, O, M, N: Ring](game: G, f: (O, Player) => N)(
    implicit evGame: Game[G, S, O, M]): S => Map[Player, N] =
    (state: S) => evGame.players(game).map(p => {
      val score = evGame.outcome(game, state).map(o => f(o, p)).getOrElse(Ring[N].zero)
      (p, score)
    }).toMap

  def aiMover[G, S, O, M, N: Order](lookahead: Int, heuristic: S => Map[Player, N])(
    implicit evGame: Game[G, S, O, M]) =
    (state: S, ttt: G) => {
      val (move, newState, values) = minimax(ttt, state, lookahead, heuristic)
      move
    }

  def hardCodedStringStrategy[G, S, O, M](
    input: (S, G) => String)(
      implicit evGame: Game[G, S, O, M],
      evGameIO: GameIO[G, S, O, M]): (S, G) => M =
    (state: S, game: G) => {
      val parsed = evGameIO.parseMove(game, input(state, game)).right.toOption.get
      val validated = evGame.isValid(game, state, parsed)
      validated.right.toOption.get
    }

  def userInputStream(display: String => Unit, read: () => String): Stream[String] = {
    display("Enter move: ")
    val command = read()
    display(command)
    cons(command, userInputStream(display, read))
  }

  def interactiveMove[G, S, O, M](
    implicit evGame: Game[G, S, O, M],
    evGameIO: GameIO[G, S, O, M]): (S, G) => M = (state: S, game: G) => {

    val mover = evGame.mover(state).get // TODO .get

    val display = evGameIO.displayerFor(game, mover)

    val stream = userInputStream(display, axle.getLine).
      map(input => {
        val parsed = evGameIO.parseMove(game, input)
        parsed.left.foreach(display)
        parsed.right.flatMap(move => {
          val validated = evGame.isValid(game, state, move)
          validated.left.foreach(display)
          validated
        })
      })

    stream.find(esm => esm.isRight).get.right.toOption.get
  }

  def randomMove[G, S, O, M](implicit evGame: Game[G, S, O, M]): (S, G) => M =
    (state: S, game: G) => {
      val opens = evGame.moves(game, state).toList
      opens(nextInt(opens.length))
    }

  def minimax[G, S, O, M, N: Order: Eq](
    game: G,
    state: S,
    depth: Int,
    heuristic: S => Map[Player, N])(
      implicit evGame: Game[G, S, O, M]): (M, S, Map[Player, N]) =
    if (evGame.outcome(game, state).isDefined || depth <= 0) {
      (null.asInstanceOf[M], null.asInstanceOf[S], heuristic(state)) // TODO null
    } else {
      val moveValue = evGame.moves(game, state).map(move => {
        val newState = evGame.applyMove(game, state, move)
        (move, state, minimax(game, newState, depth - 1, heuristic)._3)
      })
      val mover = evGame.mover(state).get // TODO .get
      val bestValue = moveValue.map(mcr => (mcr._3)(mover)).max
      val matches = moveValue.filter(mcr => (mcr._3)(mover) === bestValue).toIndexedSeq
      matches(nextInt(matches.length))
    }

  /**
   * α-β pruning generalized for N-player non-zero-sum games
   *
   * 2-player zero-sum version described at:
   *
   *   http://en.wikipedia.org/wiki/Alpha-beta_pruning
   *
   */

  def alphabeta[G, S, O, M, N: Order](
    game: G,
    state: S,
    depth: Int,
    heuristic: S => Map[Player, N])(
      implicit evGame: Game[G, S, O, M]): (M, Map[Player, N]) =
    _alphabeta(game, state, depth, Map.empty, heuristic)

  def _alphabeta[G, S, O, M, N: Order](
    game: G,
    state: S,
    depth: Int,
    cutoff: Map[Player, N],
    heuristic: S => Map[Player, N])(
      implicit evGame: Game[G, S, O, M]): (M, Map[Player, N]) =
    if (evGame.outcome(game, state).isDefined || depth <= 0) {
      (null.asInstanceOf[M], heuristic(state)) // TODO null
    } else {
      val initial = AlphaBetaFold(game, null.asInstanceOf[M], cutoff, false)
      val result = evGame.moves(game, state).foldLeft(initial)(_.process(_, state, heuristic))
      (result.move, result.cutoff)
    }

}
package axle

import scala.util.Random.nextInt
import scala.Stream.cons
import spire.compat.integral
import spire.compat.ordering
import spire.algebra.Order
import spire.algebra.Eq
import spire.implicits.eqOps
import spire.implicits._
import util.Random.nextInt

package object game {

  val dropOutput = (s: String) => {}

  def didIWinHeuristic[G, S, O, M](game: G)(
    implicit evGame: Game[G, S, O, M],
    evOutcome: Outcome[O],
    evState: State[G, S, O, M]): S => Map[Player, Double] =
    (state: S) => evGame.players(game).map(p => {
      val score =
        evState.outcome(state, game).flatMap(out =>
          evOutcome.winner(out).map(winner =>
            if (winner === p) 1d else -1d)).getOrElse(0d)
      (p, score)
    }).toMap

  def aiMover[G, S, O, M, N: Order](lookahead: Int, heuristic: S => Map[Player, N])(
    implicit evGame: Game[G, S, O, M],
    evState: State[G, S, O, M]) =
    (state: S, ttt: G) => {
      val (move, newState, values) = minimax(ttt, state, lookahead, heuristic)
      move
    }

  def hardCodedStrategy[G, S, O, M](
    input: (S, G) => String)(
      implicit evGame: Game[G, S, O, M]): (S, G) => M =
    (state: S, game: G) => evGame.parseMove(game, input(state, game)).right.toOption.get

  def userInputStream(display: String => Unit, read: () => String): Stream[String] = {
    display("Enter move: ")
    val command = read()
    display(command)
    cons(command, userInputStream(display, read))
  }

  def interactiveMove[G, S, O, M](
    implicit evGame: Game[G, S, O, M],
    evState: State[G, S, O, M]): (S, G) => M = (state: S, game: G) => {

    val mover = evState.mover(state).get // TODO .get

    val display = evGame.displayerFor(game, mover)

    val stream = userInputStream(display, axle.getLine).
      map(input => {
        val parsed = evGame.parseMove(game, input)
        parsed.left.foreach(display)
        parsed.right.flatMap(move => {
          val validated = evGame.isValid(game, state, move)
          validated.left.foreach(display)
          validated
        })
      })

    stream.find(esm => esm.isRight).get.right.toOption.get
  }

  def randomMove[G, S, O, M](implicit evState: State[G, S, O, M]): (S, G) => M =
    (state: S, game: G) => {
      val opens = evState.moves(state, game).toList
      opens(nextInt(opens.length))
    }

  def prefixedDisplay(prefix: String)(display: String => Unit): String => Unit =
    (s: String) => s.split("\n").foreach(line => display(prefix + "> " + line))

  def introMessage[G, S, O, M](g: G)(implicit evGame: Game[G, S, O, M]): String =
    evGame.introMessage(g)

  def startState[G, S, O, M](g: G)(implicit evGame: Game[G, S, O, M]): S =
    evGame.startState(g)

  def startFrom[G, S, O, M](g: G, s: S)(implicit evGame: Game[G, S, O, M]): Option[S] =
    evGame.startFrom(g, s)

  def minimax[G, S, O, M, N: Order: Eq](
    game: G,
    state: S,
    depth: Int,
    heuristic: S => Map[Player, N])(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M]): (M, S, Map[Player, N]) =
    if (evState.outcome(state, game).isDefined || depth <= 0) {
      (null.asInstanceOf[M], null.asInstanceOf[S], heuristic(state)) // TODO null
    } else {
      val moveValue = evState.moves(state, game).map(move => {
        val newState = evState.applyMove(state, game, move)
        (move, state, minimax(game, newState, depth - 1, heuristic)._3)
      })
      val mover = evState.mover(state).get // TODO .get
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
      implicit evGame: Game[G, S, O, M], evState: State[G, S, O, M]): (M, Map[Player, N]) =
    _alphabeta(game, state, depth, Map.empty, heuristic)

  def _alphabeta[G, S, O, M, N: Order](
    g: G,
    state: S,
    depth: Int,
    cutoff: Map[Player, N],
    heuristic: S => Map[Player, N])(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M]): (M, Map[Player, N]) =
    if (evState.outcome(state, g).isDefined || depth <= 0) {
      (null.asInstanceOf[M], heuristic(state)) // TODO null
    } else {
      val initial = AlphaBetaFold(g, null.asInstanceOf[M], cutoff, false)
      val result = evState.moves(state, g).foldLeft(initial)(_.process(_, state, heuristic))
      (result.move, result.cutoff)
    }

  def moveStateStream[G, S, O, M](
    game: G,
    fromState: S)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): Stream[(S, M, S)] =
    evState.mover(fromState).map(mover => {
      val strategy = evGame.strategyFor(game, mover)
      val move = strategy.apply(fromState, game)
      val toState = evState.applyMove(fromState, game, move)
      cons((fromState, move, toState), moveStateStream(game, toState))
    }) getOrElse {
      Stream.empty
    }

  def play[G, S, O, M](game: G)(
    implicit evGame: Game[G, S, O, M],
    evState: State[G, S, O, M],
    evOutcome: Outcome[O],
    evMove: Move[G, S, O, M]): S =
    play(game, startState(game), true)

  def play[G, S, O, M](
    game: G,
    start: S,
    intro: Boolean = true)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): S = {

    evGame.players(game) foreach { observer =>
      val display = evGame.displayerFor(game, observer)
      if (intro) {
        display(evGame.introMessage(game))
      }
      display(evState.displayTo(start, observer, game))
    }

    val lastState = moveStateStream(game, start) map {
      case (fromState, move, toState) => {
        evState.mover(fromState) foreach { mover =>
          evGame.players(game) foreach { observer =>
            evMove.displayTo(game, mover, move, observer)
          }
          val display = evGame.displayerFor(game, mover)
          display(evState.displayTo(toState, mover, game))
        }
        toState
      }
    } last

    evGame.players(game) foreach { observer =>
      val display = evGame.displayerFor(game, observer)
      display("")
      display(evState.displayTo(lastState, observer, game))
      evState.outcome(lastState, game) foreach { outcome =>
        evOutcome.displayTo(game, outcome, observer)
      }
    }

    lastState
  }

  def gameStream[G, S, O, M](
    game: G,
    start: S,
    intro: Boolean = true)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): Stream[S] = {
    val end = play(game, start, intro)
    val tail = evGame.startFrom(game, end) map { newStart =>
      gameStream(game, newStart, false)
    } getOrElse Stream.empty
    cons(end, tail)
  }

  def playContinuously[G, S, O, M](
    game: G,
    start: S)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): S =
    gameStream(game, start).last

}
package axle

import scala.util.Random.nextInt
import scala.Stream.cons
import scala.Stream.empty
import spire.compat.integral
import spire.compat.ordering
import spire.algebra.Order
import spire.algebra.Eq
import spire.implicits.eqOps
import spire.implicits._
import util.Random.nextInt

package object game {

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

  def interactiveMove[G, S, O, M](
    implicit evGame: Game[G, S, O, M],
    evState: State[G, S, O, M]): (S, G) => M = (state: S, game: G) => {

    val display = evGame.displayerFor(game, evState.mover(state))

    val stream = userInputStream(display, axle.getLine).
      map(input => {
        val parsed = evGame.parseMove(game, input, evState.mover(state))
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

  def userInputStream(display: String => Unit, read: () => String): Stream[String] = {
    display("Enter move: ")
    val command = read()
    display(command)
    cons(command, userInputStream(display, read))
  }

  def scriptToLastMoveState[G, S, O, M](
    game: G,
    moves: List[M])(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M]): (M, S) =
    scriptedMoveStateStream(game, evGame.startState(game), moves.iterator).last

  // From Player:

  def introduceGame[G, S, O, M](
    player: Player,
    game: G)(
      implicit evGame: Game[G, S, O, M]): Unit = {
    val display = evGame.displayerFor(game, player)
    display(evGame.introMessage(game))
  }

  def endGame[G, S, O, M](
    game: G,
    player: Player,
    state: S)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O]): Unit = {
    val display = evGame.displayerFor(game, player)
    display("")
    display(evState.displayTo(state, player, game))
    evState.outcome(state, game).foreach(outcome => evOutcome.displayTo(game, outcome, player))
  }

  // From State:

  def broadcastMove[G, S, O, M](
    game: G,
    state: S,
    move: M)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evMove: Move[G, S, O, M]): Unit =
    evGame.players(game) foreach { observer =>
      evMove.displayTo(game, evState.mover(state), move, observer)
    }

  def broadcastOutcome[G, S, O, M](
    game: G,
    state: S,
    outcome: O)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O]): Unit =
    evGame.players(game) foreach { observer =>
      evOutcome.displayTo(game, outcome, observer)
    }

  // From Game:

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
      val bestValue = moveValue.map(mcr => (mcr._3)(evState.mover(state))).max
      val matches = moveValue.filter(mcr => (mcr._3)(evState.mover(state)) === bestValue).toIndexedSeq
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
    s0: S)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): Stream[(M, S)] =
    if (evState.outcome(s0, game).isDefined) {
      empty
    } else {
      val mover = evState.mover(s0)
      val display = evGame.displayerFor(game, mover)
      display(evState.displayTo(s0, mover, game))
      val strategy = evGame.strategyFor(game, mover)
      val move = strategy.apply(s0, game)
      val s1 = evState.applyMove(s0, game, move)
      broadcastMove(game, s1, move)
      cons((move, s1), moveStateStream(game, s1))
    }

  def scriptedMoveStateStream[G, S, O, M](
    game: G,
    state: S,
    moveIt: Iterator[M])(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M]): Stream[(M, S)] =
    if (evState.outcome(state, game).isDefined || !moveIt.hasNext) {
      empty
    } else {
      val move = moveIt.next
      val nextState = evState.applyMove(state, game, move)
      cons((move, nextState), scriptedMoveStateStream(game, nextState, moveIt))
    }

  def play[G, S, O, M](game: G)(
    implicit evGame: Game[G, S, O, M],
    evState: State[G, S, O, M],
    evOutcome: Outcome[O],
    evMove: Move[G, S, O, M]): Option[S] =
    play(game, startState(game), true)

  def play[G, S, O, M](
    game: G,
    start: S,
    intro: Boolean = true)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): Option[S] = {
    if (intro) {
      evGame.players(game) foreach { player =>
        introduceGame(player, game)
      }
    }
    moveStateStream(game, start).lastOption.map({
      case (lastMove, s) => {
        val outcome = evState.outcome(s, game).get // TODO .get
        broadcastOutcome(game, s, outcome)
        evGame.players(game) foreach { player =>
          endGame(game, player, s)
        }
        s
      }
    })
  }

  def gameStream[G, S, O, M](
    game: G,
    start: S,
    intro: Boolean = true)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): Stream[S] =
    play(game, start, intro).flatMap(end => {
      evGame.startFrom(game, end).map(newStart =>
        cons(end, gameStream(game, newStart, false)))
    }).getOrElse(empty)

  def playContinuously[G, S, O, M](
    game: G,
    start: S)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): S =
    gameStream(game, start).last

}
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
        evGame.parseMove(input, evState.mover(state)).map(move => {
          val validated = evGame.isValid(state, move, game)
          validated.left.map(display)
          validated
        })
      })

    stream.find(oem => oem.isDefined && oem.get.isRight).get.get.right.toOption.get
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

  def displayEvents[G, S, O, M](
    game: G,
    player: Player,
    events: List[Either[O, M]])(
      implicit evGame: Game[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): Unit = {
    val display = evGame.displayerFor(game, player)
    display("")
    display(events.map(event =>
      event match {
        case Left(outcome) => evOutcome.displayTo(game, outcome, player)
        case Right(move)   => evMove.displayTo(game, move, player)
      }).mkString("  "))
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

  def displayEvents[G, S, O, M](
    game: G,
    state: S)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): S = {
    val qs = evState.eventQueues(state)
    evGame.players(game).foreach(p => {
      val events = qs.get(p).getOrElse(List.empty)
      displayEvents(game, p, events)
    })
    evState.setEventQueues(state, qs ++ evGame.players(game).map(p => (p -> Nil)))
  }

  def broadcast[G, S, O, M](
    game: G,
    state: S,
    event: Either[O, M])(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M]): S = {
    val qs = evState.eventQueues(state)
    evState.setEventQueues(state, evGame.players(game).map(p => {
      (p -> (qs.get(p).getOrElse(Nil) ++ List(event)))
    }).toMap)
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
        val newState = evState.applyMove(state, move, game)
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
      val s1 = displayEvents(game, s0)
      val mover = evState.mover(s1)
      val display = evGame.displayerFor(game, mover)
      display(evState.displayTo(s1, mover, game))
      val strategy = evGame.strategyFor(game, mover)
      val move = strategy.apply(s1, game)
      val s2 = evState.applyMove(s1, move, game)
      val s3 = broadcast(game, s2, Right(move))
      cons((move, s3), moveStateStream(game, s3))
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
      val nextState = evState.applyMove(state, move, game)
      cons((move, nextState), scriptedMoveStateStream(game, nextState, moveIt))
    }

  // note default start was game.startState
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
      case (lastMove, s0) => {
        val s1 = evState.outcome(s0, game).map(o => broadcast(game, s0, Left(o))).getOrElse(s0)
        val s2 = displayEvents(game, s1)
        evGame.players(game) foreach { player =>
          endGame(game, player, s2)
        }
        s2
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

  // Note: start default was game.startState
  def playContinuously[G, S, O, M](
    game: G,
    start: S)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[G, S, O, M]): S =
    gameStream(game, start).last

}
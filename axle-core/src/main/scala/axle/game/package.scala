package axle

import scala.util.Random.nextInt
import scala.Stream.cons
import scala.Stream.empty
import spire.compat.integral
import spire.math.Real

package object game {

  def userInputStream(): Stream[String] = {
    // TODO: reuse "displayer" for println
    print("Enter move: ")
    val command = scala.io.StdIn.readLine() // TODO echo characters as typed (shouldn't have to use jline for this)
    println(command)
    cons(command, userInputStream)
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
      evMove: Move[M]): Unit = {
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
    display(evState.displayTo(player))
    evState.outcome(state).foreach(outcome => evOutcome.displayTo(game, outcome, player))
  }

  // From State:

  def displayEvents[G, S, O, M](
    game: G,
    state: S)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[M]): S = {
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

  def minimax[G, S, O, M](
    game: G,
    state: S,
    depth: Int,
    heuristic: S => Map[Player, Real])(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M]): (M, S, Map[Player, Real]) =
    if (evState.outcome(state).isDefined || depth <= 0) {
      (null.asInstanceOf[M], null.asInstanceOf[S], heuristic(state)) // TODO null
    } else {
      // TODO: .get
      val moveValue = evState.moves(state).map(move => {
        val newState = evState.applyMove(state, move, game).get // TODO: .get
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

  def alphabeta[G, S, O, M](
    game: G,
    state: S,
    depth: Int,
    heuristic: S => Map[Player, Double])(
      implicit evGame: Game[G, S, O, M], evState: State[G, S, O, M]): (M, Map[Player, Double]) =
    _alphabeta(game, state, depth, evGame.players(game).map((_, Double.MinValue)).toMap, heuristic)

  def _alphabeta[G, S, O, M](
    g: G,
    state: S,
    depth: Int,
    cutoff: Map[Player, Double],
    heuristic: S => Map[Player, Double])(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M]): (M, Map[Player, Double]) =
    if (evState.outcome(state).isDefined || depth <= 0) {
      (null.asInstanceOf[M], heuristic(state)) // TODO null
    } else {
      val result = evState.moves(state).foldLeft(AlphaBetaFold[G, S, O, M](g, null.asInstanceOf[M], cutoff, false))(
        (in: AlphaBetaFold[G, S, O, M], move: M) => in.process(move, state, heuristic))
      (result.move, result.cutoff)
    }

  def moveStateStream[G, S, O, M](
    game: G,
    s0: S)(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M],
      evOutcome: Outcome[O],
      evMove: Move[M]): Stream[(M, S)] =
    if (evState.outcome(s0).isDefined) {
      empty
    } else {
      val s1 = displayEvents(game, s0)
      val strategy = evGame.strategyFor(game, evState.mover(s1))
      val (move, _) = strategy.apply(s1, game, evGame)
      val s2 = evState.applyMove(s1, move, game).get // TODO .get
      val s3 = broadcast(game, s2, Right(move))
      cons((move, s3), moveStateStream(game, s3))
    }

  def scriptedMoveStateStream[G, S, O, M](
    game: G,
    state: S,
    moveIt: Iterator[M])(
      implicit evGame: Game[G, S, O, M],
      evState: State[G, S, O, M]): Stream[(M, S)] =
    if (evState.outcome(state).isDefined || !moveIt.hasNext) {
      empty
    } else {
      val move = moveIt.next
      val nextState = evState.applyMove(state, move, game).get // TODO .get
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
      evMove: Move[M]): Option[S] = {
    if (intro) {
      evGame.players(game) foreach { player =>
        introduceGame(player, game)
      }
    }
    moveStateStream(game, start).lastOption.map({
      case (lastMove, s0) => {
        val s1 = evState.outcome(s0).map(o => broadcast(game, s0, Left(o))).getOrElse(s0)
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
      evMove: Move[M]): Stream[S] =
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
      evMove: Move[M]): S =
    gameStream(game, start).last

}
package axle

import scala.util.Random.nextInt
import scala.Stream.cons
import scala.Stream.empty
import spire.compat.integral
import spire.math.Real

package object game {

  def userInputStream(): Stream[String] = {
    print("Enter move: ")
    val command = scala.io.StdIn.readLine() // TODO echo characters as typed (shouldn't have to use jline for this)
    println(command)
    cons(command, userInputStream)
  }

  def scriptToLastMoveState[G, S, O, M](
    g: G,
    game: Game[G, S, O, M],
    moves: List[M])(
      implicit evState: State[G, S, O, M]): (M, S) =
    scriptedMoveStateStream(g, game, game.startState(g), moves.iterator).last

  // From Player:

  def introduceGame[G, S, O, M](
    player: Player,
    g: G,
    game: Game[G, S, O, M]): Unit = {
    val display = game.displayerFor(player)
    display(game.introMessage(g))
  }

  def displayEvents[G, S, O, M](
    player: Player,
    events: List[Either[O, M]],
    game: Game[G, S, O, M]): Unit = {
    val display = game.displayerFor(player)
    display("")
    display(events.map(event => {
      display(event.toString) // displayTo(player, game) // TODO avoid toString.  use evEvent
    }).mkString("  "))
  }

  def endGame[G, S, O, M](
    player: Player,
    state: S,
    game: Game[G, S, O, M])(
      implicit evState: State[G, S, O, M]): Unit = {
    val display = game.displayerFor(player)
    display("")
    display(evState.displayTo(player, game))
    evState.outcome(state).foreach(outcome => display(outcome.toString)) // TODO avoid toString
  }

  // From State:

  def displayEvents[G, S, O, M](
    state: S,
    game: Game[G, S, O, M])(
      implicit evState: State[G, S, O, M]): S = {
    val qs = evState.eventQueues(state)
    evState.players(state).foreach(p => {
      val events = qs.get(p).getOrElse(List.empty)
      displayEvents(p, events, game)
    })
    evState.setEventQueues(state, qs ++ evState.players(state).map(p => (p -> Nil)))
  }

  def broadcast[G, S, O, M](
    state: S,
    event: Either[O, M])(
      implicit evState: State[G, S, O, M]): S = {
    val qs = evState.eventQueues(state)
    evState.setEventQueues(state, evState.players(state).map(p => {
      (p -> (qs.get(p).getOrElse(Nil) ++ List(event)))
    }).toMap)
  }

  // From Game:

  def minimax[G, S, O, M](
    g: G,
    game: Game[G, S, O, M],
    state: S,
    depth: Int,
    heuristic: S => Map[Player, Real])(
      implicit evState: State[G, S, O, M]): (M, S, Map[Player, Real]) =
    if (evState.outcome(state).isDefined || depth <= 0) {
      (null.asInstanceOf[M], null.asInstanceOf[S], heuristic(state)) // TODO null
    } else {
      // TODO: .get
      val moveValue = evState.moves(state).map(move => {
        val newState = evState.applyMove(state, move, g, game).get // TODO: .get
        (move, state, minimax(g, game, newState, depth - 1, heuristic)._3)
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
    g: G,
    game: Game[G, S, O, M],
    state: S,
    depth: Int,
    heuristic: S => Map[Player, Double])(
      implicit evState: State[G, S, O, M]): (M, Map[Player, Double]) =
    _alphabeta(g, game, state, depth, evState.players(state).map((_, Double.MinValue)).toMap, heuristic)

  case class AlphaBetaFold[G, S, O, M](
      g: G,
      game: Game[G, S, O, M],
      move: M,
      cutoff: Map[Player, Double],
      done: Boolean)(
          implicit evState: State[G, S, O, M]) {

    def process(
      m: M,
      state: S,
      heuristic: S => Map[Player, Double]): AlphaBetaFold[G, S, O, M] =
      if (done) {
        this
      } else {
        val α = heuristic(evState.applyMove(state, m, g, game).get)
        // TODO: forall other players ??
        if (cutoff(evState.mover(state)) <= α(evState.mover(state))) {
          AlphaBetaFold(g, game, m, α, false) // TODO move = m?
        } else {
          AlphaBetaFold(g, game, m, cutoff, true)
        }
      }
  }

  def _alphabeta[G, S, O, M](
    g: G,
    game: Game[G, S, O, M],
    state: S,
    depth: Int,
    cutoff: Map[Player, Double],
    heuristic: S => Map[Player, Double])(
      implicit evState: State[G, S, O, M]): (M, Map[Player, Double]) =
    if (evState.outcome(state).isDefined || depth <= 0) {
      (null.asInstanceOf[M], heuristic(state)) // TODO null
    } else {
      val result = evState.moves(state).foldLeft(AlphaBetaFold[G, S, O, M](g, game, null.asInstanceOf[M], cutoff, false))(
        (in: AlphaBetaFold[G, S, O, M], move: M) => in.process(move, state, heuristic))
      (result.move, result.cutoff)
    }

  def moveStateStream[G, S, O, M](
    g: G,
    game: Game[G, S, O, M],
    s0: S)(
      implicit evState: State[G, S, O, M]): Stream[(M, S)] =
    if (evState.outcome(s0).isDefined) {
      empty
    } else {
      val s1 = displayEvents(s0, game)
      val strategy = game.strategyFor(evState.mover(s1))
      val (move, _) = strategy.apply(s1, game) // TODO: figure out why in some cases the second argument (a State) wasn't modified (eg minimax)
      val s2 = evState.applyMove(s1, move, g, game).get // TODO .get
      val s3 = broadcast(s2, Right(move))
      cons((move, s3), moveStateStream(g, game, s3))
    }

  def scriptedMoveStateStream[G, S, O, M](
    g: G,
    game: Game[G, S, O, M],
    state: S,
    moveIt: Iterator[M])(
      implicit evState: State[G, S, O, M]): Stream[(M, S)] =
    if (evState.outcome(state).isDefined || !moveIt.hasNext) {
      empty
    } else {
      val move = moveIt.next
      val nextState = evState.applyMove(state, move, g, game).get // TODO .get
      cons((move, nextState), scriptedMoveStateStream(g, game, nextState, moveIt))
    }

  // note default start was game.startState
  def play[G, S, O, M](
    g: G,
    game: Game[G, S, O, M],
    start: S,
    intro: Boolean = true)(
      implicit evState: State[G, S, O, M]): Option[S] = {
    if (intro) {
      evState.players(start) foreach { player =>
        introduceGame(player, g, game)
      }
    }
    moveStateStream(g, game, start).lastOption.map({
      case (lastMove, s0) => {
        val s1 = evState.outcome(s0).map(o => broadcast(s0, Left(o))).getOrElse(s0)
        val s2 = displayEvents(s1, game)
        evState.players(s2) foreach { player =>
          endGame(player, s2, game)
        }
        s2
      }
    })
  }

  def gameStream[G, S, O, M](
    g: G,
    game: Game[G, S, O, M],
    start: S,
    intro: Boolean = true)(
      implicit evState: State[G, S, O, M]): Stream[S] =
    play(g, game, start, intro).flatMap(end => {
      game.startFrom(g, end).map(newStart =>
        cons(end, gameStream(g, game, newStart, false)))
    }).getOrElse(empty)

  // Note: start default was game.startState
  def playContinuously[G, S, O, M](
    g: G,
    game: Game[G, S, O, M],
    start: S)(
      implicit evState: State[G, S, O, M]): S =
    gameStream(g, game, start).last

}
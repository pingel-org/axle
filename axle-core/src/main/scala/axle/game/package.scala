package axle

import scala.util.Random.nextInt
import scala.Stream.cons
import scala.Stream.empty

import spire.algebra.Eq
import spire.implicits.eqOps
import spire.compat.integral
import spire.math.Real

package object game {

  def scriptToLastMoveState[G <: Game[G]](game: G, moves: List[G#MOVE]): (G#MOVE, G#STATE) =
    scriptedMoveStateStream(game, game.startState, moves.iterator).last

  // From State:

  def displayEvents[G <: Game[G]](state: G#STATE, players: Seq[G#PLAYER], game: G): G#STATE = {
    val qs = state.eventQueues
    players.foreach(p => p.displayEvents(qs.get(p).getOrElse(Nil), game))
    state.setEventQueues(qs ++ players.map(p => (p -> Nil)))
  }

  def broadcast[G <: Game[G], E <: Event[G]](state: G#STATE, players: Seq[G#PLAYER], event: E): G#STATE = {
    val qs = state.eventQueues
    state.setEventQueues(players.map(p => {
      (p -> (qs.get(p).getOrElse(Nil) ++ List(event)))
    }).toMap)
  }

  // From Game:

  def minimax[G <: Game[G]](game: G, state: G#STATE, depth: Int, heuristic: G#STATE => Map[G#PLAYER, Real]): (G#MOVE, G#STATE, Map[G#PLAYER, Real]) =
    if (state.outcome(game).isDefined || depth <= 0) {
      (null.asInstanceOf[G#MOVE], null.asInstanceOf[G#STATE], heuristic(state)) // TODO null
    } else {
      // TODO: .get
      val moveValue = state.moves(game).map(move => {
        val newState = state(move, game).get // TODO: .get
        (move, state, minimax(game, newState, depth - 1, heuristic)._3)
      })
      val bestValue = moveValue.map(mcr => (mcr._3)(state.player)).max
      val matches = moveValue.filter(mcr => (mcr._3)(state.player) === bestValue).toIndexedSeq
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

  def alphabeta[G <: Game[G]](game: G, state: G#STATE, depth: Int, heuristic: G#STATE => Map[G#PLAYER, Double]): (G#MOVE, Map[G#PLAYER, Double]) =
    _alphabeta(game, state, depth, game.players.map((_, Double.MinValue)).toMap, heuristic)

  case class AlphaBetaFold[G <: Game[G]](game: G, move: G#MOVE, cutoff: Map[G#PLAYER, Double], done: Boolean) {

    def process(m: G#MOVE, state: G#STATE, heuristic: G#STATE => Map[G#PLAYER, Double]): AlphaBetaFold[G] =
      if (done) {
        this
      } else {
        val α = heuristic(state(m, game).get)
        if (cutoff(state.player) <= α(state.player)) // TODO: forall other players ??
          AlphaBetaFold(game, m, α, false) // TODO move = m?
        else
          AlphaBetaFold(game, m, cutoff, true)
      }
  }

  def _alphabeta[G <: Game[G]](game: G, state: G#STATE, depth: Int, cutoff: Map[G#PLAYER, Double], heuristic: G#STATE => Map[G#PLAYER, Double]): (G#MOVE, Map[G#PLAYER, Double]) =
    if (state.outcome(game).isDefined || depth <= 0) {
      (null.asInstanceOf[G#MOVE], heuristic(state)) // TODO null
    } else {
      val result = state.moves(game).foldLeft(AlphaBetaFold[G](game, null.asInstanceOf[G#MOVE], cutoff, false))(
        (in: AlphaBetaFold[G], move: G#MOVE) => in.process(move, state, heuristic))
      (result.move, result.cutoff)
    }

  def moveStateStream[G <: Game[G]](game: G, s0: G#STATE): Stream[(G#MOVE, G#STATE)] =
    if (s0.outcome(game).isDefined) {
      empty
    } else {
      val s1 = displayEvents(s0, Seq(s0.player), game)
      val (move, _) = s1.player.move(s1, game) // TODO: figure out why in some cases the second argument (a State) wasn't modified (eg minimax)
      val s2 = s1(move, game).get // TODO .get
      val s3 = broadcast(s2, game.players, move)
      cons((move, s3), moveStateStream(game, s3))
    }

  def scriptedMoveStateStream[G <: Game[G]](game: G, state: G#STATE, moveIt: Iterator[G#MOVE]): Stream[(G#MOVE, G#STATE)] =
    if (state.outcome(game).isDefined || !moveIt.hasNext) {
      empty
    } else {
      val move = moveIt.next
      val nextState = state(move, game).get // TODO .get
      cons((move, nextState), scriptedMoveStateStream(game, nextState, moveIt))
    }

  // note default start was game.startState
  def play[G <: Game[G]](game: G, start: G#STATE, intro: Boolean = true): Option[G#STATE] = {
    if (intro) {
      game.players foreach { player =>
        player.introduceGame(game)
      }
    }
    moveStateStream(game, start).lastOption.map({
      case (lastMove, s0) => {
        val s1 = s0.outcome(game).map(o => broadcast(s0, game.players, o)).getOrElse(s0)
        val s2 = displayEvents(s1, game.players, game)
        game.players foreach { player =>
          player.endGame(s2, game)
        }
        s2
      }
    })
  }

  def gameStream[G <: Game[G]](game: G, start: G#STATE, intro: Boolean = true): Stream[G#STATE] =
    play(game, start, intro).flatMap(end => {
      game.startFrom(end).map(newStart =>
        cons(end, gameStream(game, newStart, false)))
    }).getOrElse(empty)

  // Note: start default was game.startState
  def playContinuously[G <: Game[G]](game: G, start: G#STATE): G#STATE =
    gameStream(game, start).last

  // Note: from Outcome

  def displayTo[G <: Game[G]](outcome: G#OUTCOME, player: G#PLAYER, game: G)(implicit eqp: Eq[G#PLAYER], sp: Show[G#PLAYER]): String =
    outcome.winner map { wp =>
      if (wp === player) {
        "You have beaten " + game.players.collect({ case p if !(p === player) => string(p) }).toList.mkString(" and ") + "!"
      } else {
        "%s beat you!".format(wp)
      }
    } getOrElse ("The game was a draw.")

}
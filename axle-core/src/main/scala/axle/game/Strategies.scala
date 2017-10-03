package axle.game

import scala.Stream.cons
import scala.util.Random.nextInt

import axle.stats.ConditionalProbabilityTable0
import axle.stats.Variable

import cats.kernel.Order
import cats.Order.catsKernelOrderingForOrder
import cats.implicits._
import spire.math.Rational
import spire.algebra.Ring
import spire.implicits._
import spire.random.Dist

object Strategies {

  implicit val distDouble = implicitly[Dist[Double]].map(Rational.apply)

  def outcomeRingHeuristic[G, S, O, M, MS, MM, N: Ring](game: G, f: (O, Player) => N)(
    implicit evGame: Game[G, S, O, M, MS, MM]): S => Map[Player, N] =
    (state: S) => evGame.players(game).map(p => {
      val score = evGame.outcome(game, state).map(o => f(o, p)).getOrElse(Ring[N].zero)
      (p, score)
    }).toMap

  def aiMover[G, S, O, M, MS, MM, N: Order](lookahead: Int, heuristic: S => Map[Player, N])(
    implicit evGame: Game[G, S, O, M, MS, MM]): (G, S) => ConditionalProbabilityTable0[M, Rational] =
    (ttt: G, state: S) => {
      val (move, newState, values) = minimax(ttt, state, lookahead, heuristic)
      val v = Variable[M]("ai move", Vector(move))
      ConditionalProbabilityTable0[M, Rational](Map(move -> Rational(1)), v)
    }

  def hardCodedStringStrategy[G, S, O, M, MS, MM](
    input: (G, MS) => String)(
      implicit evGame: Game[G, S, O, M, MS, MM],
      evGameIO: GameIO[G, O, M, MS, MM]): (G, MS) => ConditionalProbabilityTable0[M, Rational] =
    (game: G, state: MS) => {
      val parsed = evGameIO.parseMove(game, input(game, state)).right.toOption.get
      val validated = evGame.isValid(game, state, parsed)
      val move = validated.right.toOption.get
      val v = Variable("hard-coded", Vector(move))
      ConditionalProbabilityTable0[M, Rational](Map(move -> Rational(1)), v)
    }

  def userInputStream(display: String => Unit, read: () => String): Stream[String] = {
    display("Enter move: ")
    val command = read()
    display(command)
    cons(command, userInputStream(display, read))
  }

  def interactiveMove[G, S, O, M, MS, MM](
    implicit evGame: Game[G, S, O, M, MS, MM],
    evGameIO: GameIO[G, O, M, MS, MM]): (G, MS) => ConditionalProbabilityTable0[M, Rational] =
    (game: G, state: MS) => {

      val mover = evGame.moverM(game, state).get // TODO .get

      val display = evGameIO.displayerFor(game, mover)

      val stream = userInputStream(display, () => axle.getLine).
        map(input => {
          val parsed = evGameIO.parseMove(game, input)
          parsed.left.foreach(display)
          parsed.right.flatMap(move => {
            val validated = evGame.isValid(game, state, move)
            validated.left.foreach(display)
            validated
          })
        })

      val move = stream.find(esm => esm.isRight).get.right.toOption.get
      val v = Variable("interactive", Vector(move))
      ConditionalProbabilityTable0[M, Rational](Map(move -> Rational(1)), v)
    }

  def randomMove[G, S, O, M, MS, MM](implicit evGame: Game[G, S, O, M, MS, MM]): (G, MS) => ConditionalProbabilityTable0[M, Rational] =
    (game: G, state: MS) => {
      val opens = evGame.moves(game, state).toVector
      val p = Rational(1L, opens.length.toLong)
      val v = Variable("random", opens)
      ConditionalProbabilityTable0[M, Rational](opens.map(_ -> p).toMap, v)
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
    game: G,
    state: S,
    depth: Int,
    heuristic: S => Map[Player, N])(
      implicit evGame: Game[G, S, O, M, MS, MM]): (M, S, Map[Player, N]) = {

    // TODO capture as type constraint
    assert(evGame.outcome(game, state).isEmpty)

    val mover = evGame.mover(game, state).get // TODO .get
    val ms = evGame.maskState(game, state, mover) // TODO move this elsewhere
    val moveValue = evGame.moves(game, ms).map(move => {
      val newState = evGame.applyMove(game, state, move)
      if (evGame.outcome(game, newState).isDefined || depth === 0) {
        (move, state, heuristic(newState))
      } else {
        (move, state, minimax(game, newState, depth - 1, heuristic)._3)
      }
    })
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

  def alphabeta[G, S, O, M, MS, MM, N: Order](
    game: G,
    state: S,
    depth: Int,
    heuristic: S => Map[Player, N])(
      implicit evGame: Game[G, S, O, M, MS, MM]): (M, Map[Player, N]) =
    _alphabeta(game, state, depth, Map.empty, heuristic)

  def _alphabeta[G, S, O, M, MS, MM, N: Order](
    game: G,
    state: S,
    depth: Int,
    cutoff: Map[Player, N],
    heuristic: S => Map[Player, N])(
      implicit evGame: Game[G, S, O, M, MS, MM]): (M, Map[Player, N]) = {

    assert(evGame.outcome(game, state).isEmpty && depth > 0) // TODO capture as type constraint

    //      val initial = AlphaBetaFold(game, dummy[M], cutoff, false)
    //      val ms = evGame.maskState(game, state, ???) // TODO move this elsewhere
    //      val result = evGame.moves(game, ms).foldLeft(initial)(_.process(_, state, heuristic))
    //      (result.move, result.cutoff)
    ???
  }

}

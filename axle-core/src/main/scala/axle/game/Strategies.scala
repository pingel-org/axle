package axle.game

import cats.Monad
//import cats.effect.IO
import cats.effect.Sync
import cats.kernel.Order
import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring
import spire.math.ConvertableTo

//import axle.algebra.RegionEq
import axle.probability.ConditionalProbabilityTable

object Strategies {

  def outcomeRingHeuristic[G, S, O, M, MS, MM, V, N: Ring, PM[_, _]](game: G, f: (O, Player) => N)(
    implicit
    evGame: Game[G, S, O, M, MS, MM]): S => Map[Player, N] =
    (state: S) => evGame.players(game).map(p => {
      val score = evGame.outcome(game, state).map(o => f(o, p)).getOrElse(Ring[N].zero)
      (p, score)
    }).toMap

  def aiMover[G, S, O, M, MS, MM, V: Order: Field, N: Order, PM[_, _]](lookahead: Int, heuristic: S => Map[Player, N])(
    implicit
    monad:  Monad[PM[?, V]],
    evGame: Game[G, S, O, M, MS, MM]): (G, S) => PM[M, V] =
    (ttt: G, state: S) => {
      val (move, newState, values) = minimax(ttt, state, lookahead, heuristic)
      monad.pure[M](move)
    }

  def hardCodedStringStrategy[
    G, S, O, M, MS, MM,
    V: Order: Field,
    PM[_, _],
    F[_]](
    input: (G, MS) => String)(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    evGameIO: GameIO[G, O, M, MS, MM]): (G, MS) => ConditionalProbabilityTable[M, V] =
    (game: G, state: MS) => {
      val parsed = evGameIO.parseMove(game, input(game, state)).toOption.get
      val validated = evGame.isValid(game, state, parsed)
      val move = validated.toOption.get
      ConditionalProbabilityTable[M, V](Map(move -> Field[V].one))
    }

  def userInputStream[F[_]: Sync](
    reader: () => F[String],
    writer: String => F[Unit]
  ): LazyList[F[String]] = {

    val commandF = for {
      _ <- writer("Enter move: ")
      command <- reader()
      _ <- writer(command)
    } yield command
    
    LazyList.cons(commandF, userInputStream(reader, writer))
  }

  def interactiveMove[
    G, S, O, M, MS, MM,
    V: Order: Field,
    PM[_, _],
    F[_]: Sync: Monad](
      playerToReader: Player => () => F[String],
      playerToWriter: Player => String => F[Unit]
    )(
    implicit
    evGame:   Game[G, S, O, M, MS, MM],
    evGameIO: GameIO[G, O, M, MS, MM]): (G, MS) => F[ConditionalProbabilityTable[M, V]] =
    (game: G, state: MS) => {

      val mover = evGame.moverM(game, state).get // TODO .get

      val reader = playerToReader(mover)
      val writer = playerToWriter(mover)

      val llFEitherMoves: LazyList[F[Either[String, M]]] =
        userInputStream(reader, writer).map { inputF => {
          inputF.map { input =>
            evGameIO.parseMove(game, input).flatMap { parsedMove => {
              evGame.isValid(game, state, parsedMove)
            }}
          }
        }}

      val llFOptCPT: LazyList[F[Option[ConditionalProbabilityTable[M, V]]]] =
        llFEitherMoves.map { fEitherMove =>
          fEitherMove.map { eitherMove =>
            eitherMove.map { move =>
              ConditionalProbabilityTable[M, V](Map(move -> Field[V].one))
            }.toOption
          }
        }

      llFOptCPT
  }

  def randomMove[G, S, O, M, MS, MM, V: Order: Field: ConvertableTo, PM[_, _]](
    implicit
    evGame: Game[G, S, O, M, MS, MM]): (G, MS) => ConditionalProbabilityTable[M, V] =
    (game: G, state: MS) => {
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

  def minimax[G, S, O, M, MS, MM, V, N: Order, PM[_, _]](
    game:      G,
    state:     S,
    depth:     Int,
    heuristic: S => Map[Player, N])(
    implicit
    evGame: Game[G, S, O, M, MS, MM]): (M, S, Map[Player, N]) = {

    // TODO capture as type constraint
    assert(evGame.outcome(game, state).isEmpty)

    val mover = evGame.mover(game, state).get // TODO .get
    val ms = evGame.maskState(game, state, mover) // TODO move this elsewhere
    val moveValue = evGame.moves(game, ms).map(move => {
      val newState = evGame.applyMove(game, state, move)
      if (evGame.outcome(game, newState).isDefined || depth == 0) {
        (move, state, heuristic(newState))
      } else {
        (move, state, minimax(game, newState, depth - 1, heuristic)._3)
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

    assert(evGame.outcome(game, state).isEmpty && depth > 0) // TODO capture as type constraint

    //      val initial = AlphaBetaFold(game, dummy[M], cutoff, false)
    //      val ms = evGame.maskState(game, state, ???) // TODO move this elsewhere
    //      val result = evGame.moves(game, ms).foldLeft(initial)(_.process(_, state, heuristic))
    //      (result.move, result.cutoff)
    ???
  }

}

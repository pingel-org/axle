package axle.game

import spire.algebra.Order
import spire.implicits._

case class AlphaBetaFold[G, S, O, M, N: Order](
    game: G,
    move: M,
    cutoff: Map[Player, N],
    done: Boolean)(
        implicit evGame: Game[G, S, O, M]) {

  def process(
    move: M,
    state: S,
    heuristic: S => Map[Player, N]): AlphaBetaFold[G, S, O, M, N] =
    if (done) {
      this
    } else {
      val α = heuristic(evGame.applyMove(game, state, move))
      // TODO: forall other players ??
      val mover = evGame.mover(state).get
      val c = cutoff.get(mover)
      if (c.isEmpty || c.get <= α(mover)) {
        AlphaBetaFold(game, move, α, false) // TODO move = m?
      } else {
        AlphaBetaFold(game, move, cutoff, true)
      }
    }
}
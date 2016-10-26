package axle.game

import spire.algebra.Order
import spire.implicits._

case class AlphaBetaFold[G, S, O, M, N: Order](
    g: G,
    move: M,
    cutoff: Map[Player, N],
    done: Boolean)(
        implicit evGame: Game[G, S, O, M]) {

  def process(
    m: M,
    state: S,
    heuristic: S => Map[Player, N]): AlphaBetaFold[G, S, O, M, N] =
    if (done) {
      this
    } else {
      val α = heuristic(evGame.applyMove(state, g, m))
      // TODO: forall other players ??
      val mover = evGame.mover(state).get
      val c = cutoff.get(mover)
      if (c.isEmpty || c.get <= α(mover)) {
        AlphaBetaFold(g, m, α, false) // TODO move = m?
      } else {
        AlphaBetaFold(g, m, cutoff, true)
      }
    }
}
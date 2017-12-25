package axle.game

import cats.kernel.Order
import cats.implicits._

case class AlphaBetaFold[G, S, O, M, MS, MM, N: Order](
  game:   G,
  move:   M,
  cutoff: Map[Player, N],
  done:   Boolean)(
  implicit
  evGame: Game[G, S, O, M, MS, MM]) {

  def process(
    move:      M,
    state:     S,
    heuristic: S => Map[Player, N]): AlphaBetaFold[G, S, O, M, MS, MM, N] =
    if (done) {
      this
    } else {
      val α = heuristic(evGame.applyMove(game, state, move))
      // TODO: forall other players ??
      val mover = evGame.mover(game, state).get
      val c = cutoff.get(mover)
      if (c.isEmpty || c.get <= α(mover)) {
        AlphaBetaFold(game, move, α, false) // TODO move = m?
      } else {
        AlphaBetaFold(game, move, cutoff, true)
      }
    }
}

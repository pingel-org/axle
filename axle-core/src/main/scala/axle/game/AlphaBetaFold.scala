package axle.game

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
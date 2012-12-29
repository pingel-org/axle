package axle.game.poker

class PokerPlayerAI(aitttPlayerId: String, aitttDescription: String = "minimax")(implicit game: Poker)
  extends PokerPlayer(aitttPlayerId, aitttDescription) {

  val heuristic = (state: PokerState) => game.players.map(p => {
    (p, state.outcome.map(out => if (out.winner == Some(p)) 1.0 else -1.0).getOrElse(0.0))
  }).toMap

  def move(state: PokerState): (PokerMove, PokerState) = {
    val (move, newState, values) = game.minimax(state, 3, heuristic)
    (move, newState)
  }
}

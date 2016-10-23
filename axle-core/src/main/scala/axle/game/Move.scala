package axle.game

import spire.algebra.Eq
import axle.Show

trait Move[G, S, O, M] {

  def displayTo(
    game: G,
    move: M,
    player: Player)(
      implicit evGame: Game[G, S, O, M],
      eqp: Eq[Player],
      sp: Show[Player]): String
}

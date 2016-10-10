package axle.game

import spire.algebra.Eq
import axle.Show

trait Move[M] {

  def player: Player

  def displayTo[G, S, O](
    game: G,
    move: M,
    player: Player)(
      implicit evGame: Game[G, S, O, M],
      eqp: Eq[Player],
      sp: Show[Player]): String
}

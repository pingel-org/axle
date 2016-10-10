package axle.game

import spire.algebra.Eq
import axle.Show

trait Event[E] {

  def displayTo[G, S, O, M](
    event: E,
    player: Player,
    game: Game[G, S, O, M])(
      implicit eqp: Eq[Player],
      sp: Show[Player]): String

}
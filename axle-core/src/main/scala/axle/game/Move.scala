package axle.game

import spire.algebra.Eq
import axle.Show

trait Move[M] {

  def player: Player

  def displayTo[G, S, O](
    move: M,
    player: Player,
    game: Game[G, S, O, M])(
      implicit eqp: Eq[Player],
      sp: Show[Player]): String
}

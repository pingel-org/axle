package axle.game

import axle.Show
import spire.algebra.Eq

trait Outcome[O] {

  def winner(outcome: O): Option[Player]

  // TODO: merge/unify with displayTo of Move
  def displayTo[G, S, M](
    game: G,
    outcome: O,
    observer: Player)(
      implicit evGame: Game[G, S, O, M],
      eqp: Eq[Player],
      sp: Show[Player]): String

}
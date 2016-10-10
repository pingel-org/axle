package axle.game

import axle.Show
import spire.algebra.Eq
import spire.implicits.eqOps

trait Outcome[O] {

  def winner(outcome: O): Option[Player]

  // TODO: merge/unify with displayTo of Move
  def displayTo[G, S, M](
    game: G,
    outcome: O,
    player: Player)(
      implicit evGame: Game[G, S, O, M],
      eqp: Eq[Player],
      sp: Show[Player]): String =
    winner(outcome) map { wp =>
      if (wp === player) {
        "You have beaten " // TODO + game.players.collect({ case p if !(p === player) => string(p) }).toList.mkString(" and ") + "!"
      } else {
        "%s beat you!".format(wp)
      }
    } getOrElse ("The game was a draw.")

}
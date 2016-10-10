package axle.game

import axle.Show
import spire.algebra.Eq
import spire.implicits.eqOps

trait Outcome[O] extends Event[O] {

  def winner: Option[Player]

  def displayTo[G, S, X, M](
    outcome: O,
    player: Player,
    game: Game[G, S, X, M])(
      implicit eqp: Eq[Player],
      sp: Show[Player]): String =
    outcome.winner map { wp =>
      if (wp === player) {
        "You have beaten " // TODO + game.players.collect({ case p if !(p === player) => string(p) }).toList.mkString(" and ") + "!"
      } else {
        "%s beat you!".format(wp)
      }
    } getOrElse ("The game was a draw.")

}

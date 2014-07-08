package axle.game

import spire.algebra.Eq
import spire.implicits.eqOps

class Outcome[G <: Game[G]](winner: Option[G#PLAYER])(implicit game: G)
  extends Event[G] {

  def displayTo(player: G#PLAYER)(implicit eqp: Eq[G#PLAYER]): String =
    winner map { wp =>
      if (wp === player) {
        "You have beaten " + game.players.collect({ case p if !(p === player) => p.toString }).toList.mkString(" and ") + "!"
      } else {
        "%s beat you!".format(wp)
      }
    } getOrElse ("The game was a draw.")
}

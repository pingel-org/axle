package axle.game

import axle.Show
import axle.string
import spire.algebra.Eq
import spire.implicits.eqOps

trait Outcome[G <: Game[G]] extends Event[G] {

  def winner: Option[G#PLAYER]

  implicit def game: G

  def displayTo(player: G#PLAYER)(implicit eqp: Eq[G#PLAYER], sp: Show[G#PLAYER]): String =
    winner map { wp =>
      if (wp === player) {
        "You have beaten " + game.players.collect({ case p if !(p === player) => string(p) }).toList.mkString(" and ") + "!"
      } else {
        "%s beat you!".format(wp)
      }
    } getOrElse ("The game was a draw.")
}

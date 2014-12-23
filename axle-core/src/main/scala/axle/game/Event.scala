package axle.game

import spire.algebra.Eq
import axle.Show

trait Event[G <: Game[G]] {

  def displayTo(player: G#PLAYER)(implicit eqp: Eq[G#PLAYER], sp: Show[G#PLAYER]): String

}

package axle.game

import spire.algebra.Eq

trait Event[G <: Game[G]] {

  def displayTo(player: G#PLAYER)(implicit eqp: Eq[G#PLAYER]): String

}

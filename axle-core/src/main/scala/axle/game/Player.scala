package axle.game

import axle.Show
import spire.algebra.Eq

case class Player(id: String, description: String) {

  def referenceFor(observer: Player): String =
    if (observer == this) {
      "You"
    } else {
      description
    }

}

object Player {

  implicit def showPlayer: Show[Player] = new Show[Player] {

    def text(player: Player): String = player.description
  }

  implicit def eqPlayer: Eq[Player] = new Eq[Player] {

    def eqv(left: Player, right: Player): Boolean =
      left.==(right)
  }

}

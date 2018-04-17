package axle.game

import cats.Show
import cats.kernel.Eq

case class Player(id: String, description: String) {

  def referenceFor(observer: Player): String =
    if (observer == this) {
      "You"
    } else {
      description
    }

}

object Player {

  implicit def showPlayer: Show[Player] = _.description

  implicit def eqPlayer: Eq[Player] =
    (left, right) => left.==(right)

}

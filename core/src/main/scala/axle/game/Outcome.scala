package axle.game

class Outcome[G <: Game[G]](winner: Option[G#PLAYER])(implicit game: G)
  extends Event[G] {

  def displayTo(player: G#PLAYER): String =
    winner.map(wp =>
      if (wp equals player) {
        "You have beaten " + game.players().filter(p => !(p equals player)).map(_.toString).toList.mkString(" and ") + "!"
      } else {
        "%s beat you!".format(wp)
      }
    ).getOrElse("The game was a draw.")
}

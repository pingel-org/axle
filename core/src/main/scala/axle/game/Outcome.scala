package axle.game

class Outcome[GAME <: Game](winner: Option[GAME#PLAYER])(implicit game: GAME)
  extends Event[GAME] {

  def displayTo(player: GAME#PLAYER): String =
    winner.map(wp =>
      if (wp equals player) {
        "You have beaten " + game.players().filter(p => !(p equals player)).map(_.toString).toList.mkString(" and ") + "!"
      } else {
        "%s beat you!".format(wp)
      }
    ).getOrElse("The game was a draw.")
}

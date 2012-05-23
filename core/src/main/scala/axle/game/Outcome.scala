
package axle.game

case class Outcome[GAME <: Game](game: GAME, winner: Option[Player[GAME]])
  extends Event {

  def displayTo(player: Player[_]): Unit = winner match {
    case None => "The game was a draw."
    case Some(player) =>
      "You have beaten %s! and ".format(game.players.values.filter(_ != winner).map(_.toString).toList.mkString(" and "))

    case _ => "%s beat you!".format(winner)
  }

}

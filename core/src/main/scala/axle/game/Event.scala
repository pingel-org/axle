package axle.game

trait Event[GAME <: Game] {

  def displayTo(player: GAME#PLAYER): String

}

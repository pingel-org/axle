package axle.game

trait Event[G <: Game[G]] {

  def displayTo(player: G#PLAYER): String

}

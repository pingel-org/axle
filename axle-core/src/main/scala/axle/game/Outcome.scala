package axle.game

trait Outcome[G <: Game[G]] extends Event[G] {

  def winner: Option[G#PLAYER]

}

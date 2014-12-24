package axle.game

trait Move[G <: Game[G]]
  extends Event[G] {

  def player: G#PLAYER

}

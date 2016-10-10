package axle.game

trait Move[M] extends Event[M] {

  def player: Player

}

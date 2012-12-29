package axle.game

abstract class Player[G <: Game[G]](_id: String, description: String) {

  def id() = _id

  def move(state: G#STATE): (G#MOVE, G#STATE)

  override def toString(): String = description

  def introduceGame(): Unit = {}

  def displayEvents(): Unit = {}

  def notify(event: Event[G]): Unit = {}

  def endGame(state: G#STATE): Unit = {}
}

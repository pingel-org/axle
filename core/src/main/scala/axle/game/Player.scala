package axle.game

abstract class Player[GAME <: Game](_id: String, description: String) {

  def id() = _id

  def move(state: GAME#STATE): (GAME#MOVE, GAME#STATE)

  override def toString(): String = description

  def introduceGame(): Unit = {}

  def displayEvents(): Unit = {}

  def notify(event: Event[GAME]): Unit = {}

  def endGame(state: GAME#STATE): Unit = {}
}

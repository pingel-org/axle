
package axle.game

abstract case class Player[GAME <: Game](id: String, description: String) {

  def chooseMove(state: State[GAME], game: GAME): Move[GAME]

  override def toString(): String = description

  def introduceGame(): Unit = {}

  def notify(event: Event): Unit = {}

  def endGame(): Unit = {}
}


package axle.game

abstract case class Player(game: Game, id: String, description: String) {

  def chooseMove(): Move

  override def toString(): String = description

  def introduceGame(): Unit = {}

  def notify(event: Event): Unit = {}

  def endGame(): Unit = {}
}

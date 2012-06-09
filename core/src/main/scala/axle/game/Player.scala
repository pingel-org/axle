
package axle.game

abstract class Player[GAME <: Game](id: String, description: String) {

  def getId() = id
  
  def chooseMove(state: State[GAME], game: GAME): Move[GAME]

  override def toString(): String = description

  def introduceGame(game: GAME): Unit = {}

  def notify(event: Event): Unit = {}

  def endGame(state: State[GAME], game: GAME): Unit = {}
}

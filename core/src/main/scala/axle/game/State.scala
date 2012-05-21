
package axle.game

abstract case class State(game: Game, player: Player) {

  def applyMove(move: Move): Outcome

  // Currently there is only one mutable game "state" reused by the game

}

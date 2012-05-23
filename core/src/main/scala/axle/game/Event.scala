
package axle.game

trait Event { // game: Game

  def displayTo(player: Player[_]): Unit

}


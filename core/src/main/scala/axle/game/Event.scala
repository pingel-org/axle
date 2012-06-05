
package axle.game

trait Event { // game: Game

  def displayTo(player: Player[Game], game: Game): String

}


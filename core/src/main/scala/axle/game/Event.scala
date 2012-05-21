
package axle.game

abstract class Event(game: Game) {

  def displayTo(player: Player): Unit

}


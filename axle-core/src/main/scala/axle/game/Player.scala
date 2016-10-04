package axle.game

import axle.Show

trait Player[G <: Game[G]] {

  def id: String

  def description: String

  def move(state: G#STATE, game: G): (G#MOVE, G#STATE)

  def introduceGame(game: G): Unit = {}

  def displayEvents(events: List[Event[G]], game: G): Unit = {}

  def endGame(state: G#STATE, game: G): Unit = {}
}

object Player {
  
  implicit def showPlayer[G <: Game[G]]: Show[Player[G]] = new Show[Player[G]] {
    
    def text(player: Player[G]): String = player.description
  }
  
}

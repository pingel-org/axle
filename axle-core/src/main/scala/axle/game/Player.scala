package axle.game

import axle.Show

abstract class Player[G <: Game[G]] {

  def id: String
  
  def description: String
  
  def move(state: G#STATE): (G#MOVE, G#STATE)

  def introduceGame(): Unit = {}

  def displayEvents(events: List[Event[G]]): Unit = {}

  def endGame(state: G#STATE): Unit = {}
}

object Player {
  
  implicit def showPlayer[G <: Game[G]]: Show[Player[G]] = new Show[Player[G]] {
    
    def text(player: Player[G]): String = player.description
  }
  
}

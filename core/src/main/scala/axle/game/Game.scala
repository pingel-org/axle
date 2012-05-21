
package axle.game

/**
 *  subclasses of Game must:
 *  1) set self.state during init
 *  2) call addPlayer during init
 */

abstract class Game {
    
  var state: State
  
  var players = Map[String, Player]() // id -> player

  def introMessage(): Unit

  def addPlayer(player: Player): Unit = players += player.id -> player

  def playerById(id: String): Player = players(id)

  def play(): Unit = {
    
    for( player <- players.values ) {
      player.introduceGame()
    }
    
    var outcome: Option[Outcome] = None
    while ( outcome.isEmpty ) {
      val move = state.player.chooseMove()
      for( player <- players.values ) {
          player.notify(move)
      }
      outcome = Some(state.applyMove(move))
    }
    
    for( player <- players.values ) {
      player.notify(outcome.get)
      player.endGame()
    }
    
  }

}

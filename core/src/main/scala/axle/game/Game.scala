
package axle.game

/**
 *  subclasses of Game must:
 *  1) set self.state during init
 *  2) call addPlayer during init
 */

trait Game {

  type G = this.type

  //  var state: State[G]
  //  var players = Map[String, Player[G]]() // id -> player

  //  def addPlayer[P <: Player[G]](player: P): Unit = players += player.id -> player

  def players(): Map[String, Player[G]]

  def playerById(id: String): Player[G] = players()(id)

  def introMessage(): Unit

  def play(start: State[G]): Unit = {

    for (player <- players.values) {
      player.introduceGame()
    }

    var state = start
    while (!state.isTerminal) {
      val move = state.player.chooseMove(state, this)
      for (player <- players.values) {
        player.notify(move)
      }
      state = state.applyMove(move)
    }

    val outcome = state.getOutcome
    for (player <- players.values) {
      player.notify(outcome)
      player.endGame()
    }

  }

}

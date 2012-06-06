
package axle.game

/**
 *  subclasses of Game must:
 *  1) set self.state during init
 *  2) call addPlayer during init
 */

trait Game {

  type G <: Game

  def players(): Map[String, Player[G]]

  def playerById(id: String) = players()(id)

  def introMessage(): Unit

  def moveStateStream(state: State[G]): Stream[(Move[G], State[G])] = state.isTerminal match {
    case true => Stream.empty
    case false => {
      val move = state.player.chooseMove(state, this.asInstanceOf[G]) // TODO cast
      for (player <- players.values) {
        player.notify(move)
      }
      val nextState = state.applyMove(move)
      Stream.cons((move, nextState), moveStateStream(nextState))
    }
  }

  def scriptedMoveStateStream(state: State[G], moveIt: Iterator[Move[G]]): Stream[(Move[G], State[G])] = (state.isTerminal || ! moveIt.hasNext ) match {
    case true => Stream.empty
    case false => {
      val move = moveIt.next
      val nextState = state.applyMove(move)
      Stream.cons((move, nextState), scriptedMoveStateStream(nextState, moveIt))
    }
  }

  def play(start: State[G]): Unit = {
    for (player <- players.values) {
      player.introduceGame(this.asInstanceOf[G]) // TODO cast
    }
    val lastMoveState = moveStateStream(start).last
    lastMoveState._2.getOutcome.map(outcome =>
      for (player <- players.values) {
        player.notify(outcome)
        player.endGame(lastMoveState._2, this.asInstanceOf[G]) // TODO cast
      }
    )
  }

}

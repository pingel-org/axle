package axle.game.poker

import axle.game._

object PokerPlayerInteractive {

  val moveParser = MoveParser()

  def move(state: PokerState, game: Poker): PokerMove = {
    // displayEvents()
    val display = game.playerToDisplayer(state.mover)
    display(state.displayTo(state.mover, game))
    userInputStream(display, axle.getLine)
      .flatMap(moveParser.parse(_)(state.mover))
      .find(move => state(move, game).isDefined).get
  }

}

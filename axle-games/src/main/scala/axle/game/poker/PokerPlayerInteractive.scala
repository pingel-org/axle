package axle.game.poker

import axle.game._

object PokerPlayerInteractive {

  val moveParser = MoveParser()

  def move(state: PokerState, game: Poker): PokerMove = {
    // displayEvents()
    println(state.displayTo(state.mover, game))
    userInputStream()
      .flatMap(moveParser.parse(_)(state.mover))
      .find(move => state(move, game).isDefined).get
  }

}

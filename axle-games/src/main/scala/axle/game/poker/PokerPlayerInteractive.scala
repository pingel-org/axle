package axle.game.poker

import axle.game._

object PokerPlayerInteractive {

  val moveParser = MoveParser()

  def move(state: PokerState, game: Poker, evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): (PokerMove, PokerState) = {
    // displayEvents()
    println(state.displayTo(state.mover, game))
    val move = userInputStream()
      .flatMap(moveParser.parse(_)(state.mover))
      .find(move => state(move, game).isDefined).get
    (move, state(move, game).get) // TODO .get
  }

}

package axle.game.poker

import axle.game._

object PokerPlayerInteractive {

  val moveParser = MoveParser()

  // TODO echo characters as typed (shouldn't have to use jline for this)
  def read(): String = scala.io.StdIn.readLine()

  def move(state: PokerState, game: Poker): PokerMove = {
    // displayEvents()
    val display = game.playerToDisplayer(state.mover)
    display(state.displayTo(state.mover, game))
    userInputStream(display, read)
      .flatMap(moveParser.parse(_)(state.mover))
      .find(move => state(move, game).isDefined).get
  }

}

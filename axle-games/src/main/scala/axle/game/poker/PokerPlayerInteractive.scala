package axle.game.poker

import axle.game._
import Stream.cons

case class PokerPlayerInteractive(id: String, description: String = "human")
  extends PokerPlayer() {

  override def introduceGame(game: Poker): Unit = {
    val intro = """
Texas Hold Em Poker

Example moves:

  check
  raise 1.0
  call
  fold

"""
    println(intro)
  }

  override def displayEvents(events: List[Event[Poker]], game: Poker): Unit = {
    println()
    println(events.map(_.displayTo(this, game)).mkString("  "))
  }

  override def endGame(state: PokerState, game: Poker): Unit = {
    println()
    println(state.displayTo(state.player, game))
    state.outcome(game).foreach(println)
  }

  def userInputStream(): Stream[String] = {
    print("Enter move: ")
    val command = scala.io.StdIn.readLine() // TODO echo characters as typed (shouldn't have to use jline for this)
    println(command)
    cons(command, userInputStream)
  }

  val moveParser = MoveParser()

  def move(state: PokerState, game: Poker): (PokerMove, PokerState) = {
    // displayEvents()
    println(state.displayTo(this, game))
    val move = userInputStream()
      .flatMap(moveParser.parse(_)(state.player, game))
      .find(move => state(move, game).isDefined).get
    (move, state(move, game).get) // TODO .get
  }

}

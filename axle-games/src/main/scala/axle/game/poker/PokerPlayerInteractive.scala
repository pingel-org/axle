package axle.game.poker

import axle.game._
import Stream.cons

case class PokerPlayerInteractive(id: String, description: String = "human")(implicit game: Poker)
  extends PokerPlayer() {

  override def introduceGame(): Unit = {
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

  override def displayEvents(events: List[Event[Poker]]): Unit = {
    println()
    println(events.map(_.displayTo(this)).mkString("  "))
  }

  override def endGame(state: PokerState): Unit = {
    println()
    println(state.displayTo(state.player))
    state.outcome.map(println)
  }

  def userInputStream(): Stream[String] = {
    print("Enter move: ")
    val command = scala.io.StdIn.readLine() // TODO echo characters as typed (shouldn't have to use jline for this)
    println(command)
    cons(command, userInputStream)
  }

  val moveParser = new MoveParser()

  def move(state: PokerState): (PokerMove, PokerState) = {
    // displayEvents()
    println(state.displayTo(this))
    val move = userInputStream()
      .flatMap(moveParser.parse(_)(state.player, game))
      .find(move => state(move).isDefined).get
    (move, state(move).get) // TODO .get
  }

}

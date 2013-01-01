package axle.game.poker

import collection._
import axle.game._
import Stream.cons

class PokerPlayerInteractive(id: String, description: String = "human")(implicit game: Poker)
  extends PokerPlayer(id, description) {

  val eventQueue = mutable.ListBuffer[Event[Poker]]()

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

  override def notify(event: Event[Poker]): Unit = {
    eventQueue += event
  }

  override def displayEvents(): Unit = {
    println()
    val info = eventQueue.map(_.displayTo(this)).mkString("  ")
    println(info)
    eventQueue.clear()
  }

  override def endGame(state: PokerState): Unit = {
    displayEvents()
    println(state.displayTo(state.player))
    state.outcome.map(oc => println(oc))
  }

  def userInputStream(): Stream[String] = {
    print("Enter move: ")
    val command = readLine() // TODO echo characters as typed
    println(command)
    cons(command, userInputStream)
  }

  def parseMove(player: PokerPlayer, moveStr: String): Option[PokerMove] = {
    val tokens = moveStr.split("\\s+")
    if (tokens.length > 0) {
      tokens(0) match {
        // TODO 'check' is a 'call' when currentBet == 0.  Might want to model this.
        case "c" | "check" | "call" => Some(Call(player))
        case "r" | "raise" => {
          if (tokens.length == 2)
            try {
              Some(Raise(player, tokens(1).toInt))
            } catch {
              case e: Exception => None
            }
          else
            None
        }
        case "f" | "fold" => Some(Fold(player))
        case _ => None
      }
    } else {
      None
    }
  }

  def move(state: PokerState): (PokerMove, PokerState) = {
    displayEvents()
    println(state.displayTo(this))
    val move = userInputStream()
      .flatMap(parseMove(state.player, _))
      .find(move => state(move).isDefined).get
    (move, state(move).get) // TODO .get
  }

}

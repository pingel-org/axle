package axle.game.poker

import axle.game._
import Stream.{cons, empty}
import collection._
import util.Random.nextInt

abstract class PokerPlayer(id: String, _description: String)(implicit game: Poker)
extends Player[Poker](id, _description) {

  def description() = _description
}

class AIPokerPlayer(aitttPlayerId: String, aitttDescription: String = "minimax")(implicit game: Poker)
  extends PokerPlayer(aitttPlayerId, aitttDescription) {

  val heuristic = (state: PokerState) => game.players.map(p => {
    (p, state.outcome.map(out => if (out.winner == Some(p)) 1.0 else -1.0).getOrElse(0.0))
  }).toMap

  def move(state: PokerState): (PokerMove, PokerState) = {
    val (move, newState, values) = game.minimax(state, 3, heuristic)
    (move, newState)
  }
}

class RandomPokerPlayer(id: String, description: String = "random")(implicit game: Poker)
  extends PokerPlayer(id, description) {

  def move(state: PokerState): (PokerMove, PokerState) = {
    val opens = state.moves
    val move = opens(nextInt(opens.length))
    (move, state(move).get)
  }
}

class DealerPokerPlayer(id: String, description: String = "dealer")(implicit game: Poker)
  extends PokerPlayer(id, description) {

  def move(state: PokerState): (PokerMove, PokerState) = {
    val move = state.numShown match {
      case 0 =>
        if (state.inFors.size == 0)
          Deal()
        else
          Flop()
      case 3 => Turn()
      case 4 => River()
    }
    (move, state(move).get) // TODO .get
  }
}

class InteractivePokerPlayer(id: String, description: String = "human")(implicit game: Poker)
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
    println(state.outcome)
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

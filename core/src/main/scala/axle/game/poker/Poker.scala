package axle.game.poker

import axle.game._

import axle._
import util.Random.nextInt
import collection._
import Stream.cons

class Poker extends Game {

  poker =>

  type PLAYER = PokerPlayer
  type MOVE = PokerMove
  type STATE = PokerState
  type OUTCOME = PokerOutcome

  // TODO: dynamic # of players
  val player1 = player("P1", "Player 1", "human")
  val player2 = player("P2", "Player 2", "ai")
  val player3 = player("P3", "Player 3", "ai")

  //  def state(player: PokerPlayer, deck: Deck) =
  //    new PokerState(player, deck)

  def move(player: PokerPlayer) = null // PokerMove(player)

  def player(id: String, description: String, which: String) = which match {
    case "random" => new RandomPokerPlayer(id, description)
    case "ai" => new AIPokerPlayer(id, description)
    case _ => new InteractivePokerPlayer(id, description)
  }

  def startState() = {
    val deck = Deck()

    // TODO clean up these range calculations
    val hands = players.zipWithIndex.map({ case (player, i) => (player, deck.cards.apply(i * 2 to i * 2 + 1)) }).toMap
    val shared = deck.cards(players.size * 2 to players.size * 2 + 4) // flop, river

    PokerState(
      player1,
      Deck(deck.cards((players.size * 2 + 5) until deck.cards.length)),
      shared,
      0, // # of shared cards showing
      hands,
      0.0, // pot
      0.0, // current bet // TODO big/small blind
      players.map(player => (player, 0.0)).toMap // inFor
    )
  }

  def introMessage() = "Welcome to Poker"

  def players() = immutable.Set(player1, player2, player3)

  def playerAfter(player: PokerPlayer): PokerPlayer =
    if (player == player1)
      player2
    else if (player == player2)
      player3
    else
      player1

  class PokerMove(_pokerPlayer: PokerPlayer) extends Move(_pokerPlayer) {
    def player() = _pokerPlayer
    def description(): String = "something"
    def displayTo(p: PokerPlayer): String =
      (if (_pokerPlayer != p) { "I will" } else { "You have" }) +
        " done " + description() + "."
  }

  case class Pass(pokerPlayer: PokerPlayer) extends PokerMove(pokerPlayer)
  case class Call(pokerPlayer: PokerPlayer) extends PokerMove(pokerPlayer)
  case class Raise(pokerPlayer: PokerPlayer, amount: Double) extends PokerMove(pokerPlayer)
  case class Fold(pokerPlayer: PokerPlayer) extends PokerMove(pokerPlayer)

  case class PokerState(
    player: PokerPlayer,
    deck: Deck,
    shared: Seq[Card], // flop, river, etc
    numShown: Int,
    hands: Map[PokerPlayer, Seq[Card]],
    pot: Double,
    currentBet: Double,
    inFor: Map[PokerPlayer, Double])
    extends State() {

    override def toString(): String =
      "Current bet: " + currentBet + "\n" +
        "Pot: " + pot + "\n" +
        "Shared: " + shared.zipWithIndex.map({ case (card, i) => if (i < numShown) card.toString else "??" }).mkString(" ") + "\n" +
        "\n" +
        players.map(player => player.id + ": " + hands(player).map(_.toString).mkString(" ") + "   $" + inFor(player)).mkString("\n")

    def hasWon(player: PokerPlayer) = true

    def moves(): Seq[PokerMove] = List()

    def outcome(): Option[PokerOutcome] = {
      val winner = poker.players.find(hasWon(_))
      if (winner.isDefined) {
        Some(PokerOutcome(winner))
      } else { // no moves left
        Some(PokerOutcome(None))
      }
      //      else {
      //        None
      //      }
    }

    def apply(move: PokerMove): PokerState = move match {
      case Raise(player, amount) => null
      // TODO
    }

  }

  case class PokerOutcome(winner: Option[PokerPlayer]) extends Outcome(winner)

  abstract class PokerPlayer(id: String, description: String) extends Player(id, description)

  class AIPokerPlayer(aitttPlayerId: String, aitttDescription: String = "minimax")
    extends PokerPlayer(aitttPlayerId, aitttDescription) {

    val heuristic = (state: PokerState) => players.map(p => {
      (p, state.outcome.map(out => if (out.winner == Some(p)) 1.0 else -1.0).getOrElse(0.0))
    }).toMap

    def chooseMove(state: PokerState): PokerMove = poker.minimax(state, 3, heuristic)._1
  }

  class RandomPokerPlayer(id: String, description: String = "random")
    extends PokerPlayer(id, description) {

    def chooseMove(state: PokerState): PokerMove = {
      val opens = state.moves
      opens(nextInt(opens.length))
    }
  }

  class InteractivePokerPlayer(id: String, description: String = "human")
    extends PokerPlayer(id, description) {

    val eventQueue = mutable.ListBuffer[Event]()

    override def introduceGame(): Unit = {
      val intro = """
Poker
Description of moves goes here"""
      println(intro)
    }

    override def endGame(state: PokerState): Unit = {
      displayEvents()
      println(state)
    }

    override def notify(event: Event): Unit = {
      eventQueue += event
    }

    def displayEvents(): Unit = {
      val info = eventQueue.map(_.displayTo(this)).mkString("  ")
      println(info)
      eventQueue.clear()
    }

    def userInputStream(): Stream[String] = {
      print("Enter move: ")
      val num = readLine()
      println
      cons(num, userInputStream)
    }

    def isValidMove(num: String, state: PokerState): Boolean = {
      true
    }

    def chooseMove(state: PokerState): PokerMove = {
      displayEvents()
      println(state)
      null // PokerMove(this, userInputStream().find(input => isValidMove(input, state)).map(_.toInt).get)
    }

  }

}

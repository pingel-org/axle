package axle.game.poker

import axle.game._

import axle._
import util.Random.nextInt
import collection._
import Stream.cons

class Poker(numPlayers: Int) extends Game {

  poker =>

  type PLAYER = PokerPlayer
  type MOVE = PokerMove
  type STATE = PokerState
  type OUTCOME = PokerOutcome

  val dealer = player("D", "Dealer", "dealer")

  val _players = (1 to numPlayers).map(i => player("P" + i, "Player " + i, "human"))

  //  def state(player: PokerPlayer, deck: Deck) =
  //    new PokerState(player, deck)

  def move(player: PokerPlayer) = null // PokerMove(player)

  def player(id: String, description: String, which: String) = which match {
    case "random" => new RandomPokerPlayer(id, description)
    case "ai" => new AIPokerPlayer(id, description)
    case "dealer" => new DealerPokerPlayer(id, description)
    case _ => new InteractivePokerPlayer(id, description)
  }

  def startState() =
    PokerState(
      dealer,
      Deck(),
      List(),
      0, // # of shared cards showing
      Map(),
      0.0, // pot
      0.0, // current bet // TODO big/small blind
      Map(),
      players.map(player => (player, 100.0)).toMap // piles
    )

  def introMessage() = "Welcome to Poker"

  def players() = _players.toSet

  def playerAfter(player: PokerPlayer): PokerPlayer =
    if (player == dealer) {
      _players(0)
    } else if (_players.indexOf(player) == (_players.length - 1)) {
      dealer
    } else {
      _players(_players.indexOf(player) + 1)
    }

  class PokerMove(_pokerPlayer: PokerPlayer) extends Move(_pokerPlayer) {
    def player() = _pokerPlayer
    def description(): String = "something"
    def displayTo(p: PokerPlayer): String =
      (if (_pokerPlayer != p) { "I will" } else { "You have" }) +
        " done " + description() + "."
  }

  case class See(pokerPlayer: PokerPlayer) extends PokerMove(pokerPlayer)
  case class Call(pokerPlayer: PokerPlayer) extends PokerMove(pokerPlayer)
  case class Raise(pokerPlayer: PokerPlayer, amount: Double) extends PokerMove(pokerPlayer)
  case class Fold(pokerPlayer: PokerPlayer) extends PokerMove(pokerPlayer)
  case class Deal() extends PokerMove(dealer)
  case class Flop() extends PokerMove(dealer)
  case class Turn() extends PokerMove(dealer)
  case class River() extends PokerMove(dealer)

  case class PokerState(
    player: PokerPlayer,
    deck: Deck,
    shared: Seq[Card], // flop, river, etc
    numShown: Int,
    hands: Map[PokerPlayer, Seq[Card]],
    pot: Double,
    currentBet: Double,
    inFors: Map[PokerPlayer, Double],
    piles: Map[PokerPlayer, Double])
    extends State() {

    override def toString(): String =
      "Current bet: " + currentBet + "\n" +
        "Pot: " + pot + "\n" +
        "Shared: " + shared.zipWithIndex.map({ case (card, i) => if (i < numShown) card.toString else "??" }).mkString(" ") + "\n" +
        "\n" +
        players.map(player => {
          val handString = hands.get(player).map(_.map(_.toString).mkString(" ")).getOrElse("--")
          val inForString = inFors.get(player).map(_.toString).getOrElse("--")
          val pileString = piles.get(player).map(_.toString).getOrElse("--")
          player.id + ": hand " + handString + " in for $" + inForString + ", $" + pileString + " remaining"
        }).mkString("\n")

    def moves(): Seq[PokerMove] = List()

    def outcome(): Option[PokerOutcome] =
      if (numShown < 5) {
        None
      } else {
        // TODO: another round of betting after river is shown
        val winner = poker._players.sortBy(_.id).last // TODO: sort by best hand (not player id)
        Some(PokerOutcome(winner))
      }

    def apply(move: PokerMove): PokerState = move match {
      case Deal() => {
        // TODO clean up these range calculations
        val hands = _players.zipWithIndex.map({ case (player, i) => (player, deck.cards.apply(i * 2 to i * 2 + 1)) }).toMap
        val shared = deck.cards(_players.size * 2 to _players.size * 2 + 4) // flop, river
        val unused = deck.cards((_players.size * 2 + 5) until deck.cards.length)
        PokerState(
          _players(0),
          Deck(unused),
          shared,
          0, // # of shared cards showing
          hands,
          0.0, // pot
          0.0, // current bet // TODO big/small blind
          players.map(player => (player, 0.0)).toMap, // inFor
          piles
        )
      }
      case Raise(player, amount) => {
        PokerState(player, deck, shared, numShown, hands, pot, currentBet, inFors, piles)
      }
      case See(player) => {
        PokerState(player, deck, shared, numShown, hands, pot, currentBet, inFors, piles)
      }
      case Call(player) => {
        PokerState(player, deck, shared, numShown, hands, pot, currentBet, inFors, piles)
      }
      case Fold(player) => {
        PokerState(player, deck, shared, numShown, hands, pot, currentBet, inFors, piles)
      }
      case Flop() => {
        PokerState(player, deck, shared, numShown, hands, pot, currentBet, inFors, piles)
      }
      case Turn() => {
        PokerState(player, deck, shared, numShown, hands, pot, currentBet, inFors, piles)
      }
      case River() => {
        PokerState(player, deck, shared, numShown, hands, pot, currentBet, inFors, piles)
      }
    }

  }

  case class PokerOutcome(winner: PokerPlayer) extends Outcome(Some(winner))

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

  class DealerPokerPlayer(id: String, description: String = "dealer")
    extends PokerPlayer(id, description) {

    def chooseMove(state: PokerState): PokerMove = state.numShown match {
      case 0 =>
        if (state.pot == 0.0)
          Deal()
        else
          Flop()
      case 3 => Turn()
      case 4 => River()
    }
  }

  class InteractivePokerPlayer(id: String, description: String = "human")
    extends PokerPlayer(id, description) {

    val eventQueue = mutable.ListBuffer[Event]()

    override def introduceGame(): Unit = {
      val intro = """
Texas Hold Em Poker

Example moves:
        
  raise 1.0
  see
  fold
  call
        
"""
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

    def parseMove(moveStr: String): Option[PokerMove] = {
      Some(Raise(this, 1.0)) // TODO
    }

    def isValidMove(state: PokerState, move: PokerMove): Boolean = {
      util.Random.nextDouble < 0.6 // TODO
    }

    def chooseMove(state: PokerState): PokerMove = {
      displayEvents()
      println(state)
      userInputStream().flatMap(parseMove(_)).find(move => isValidMove(state, move)).get
    }

  }

}

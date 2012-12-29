package axle.game.poker

import axle.game._

import axle._
import util.Random.nextInt
import collection._
import axle.game.cards._
import Stream.cons

class Poker(numPlayers: Int) extends Game {

  poker =>

  type PLAYER = PokerPlayer
  type MOVE = PokerMove
  type STATE = PokerState
  type OUTCOME = PokerOutcome

  implicit val pokerHandOrdering = new PokerHandOrdering()
  implicit val pokerHandCategoryOrdering = new PokerHandCategoryOrdering()

  val dealer = player("D", "Dealer", "dealer")

  val _players = (1 to numPlayers).map(i => player("P" + i, "Player " + i, "human"))

  def player(id: String, description: String, which: String) = which match {
    case "random" => new RandomPokerPlayer(id, description)
    case "ai" => new AIPokerPlayer(id, description)
    case "dealer" => new DealerPokerPlayer(id, description)
    case _ => new InteractivePokerPlayer(id, description)
  }

  def startState() =
    PokerState(
      state => dealer,
      Deck(),
      Vector(),
      0, // # of shared cards showing
      Map(),
      0, // pot
      0, // current bet
      _players.toSet, // stillIn
      Map(), // inFors
      players.map(player => (player, 100)).toMap // piles
    )

  def startFrom(s: PokerState) = {
    val newPiles = s.outcome.map(o => {
      s.piles + (o.winner -> (s.piles(o.winner) + s.pot))
    }).getOrElse(s.piles)
    PokerState(
      state => dealer,
      Deck(),
      Vector(),
      0,
      Map(),
      0,
      0,
      _players.filter(newPiles(_) > 0).toSet,
      Map(),
      newPiles
    )
  }

  def introMessage() = "Welcome to Axle Texas Hold Em Poker"

  def players() = _players.toSet

  abstract class PokerMove(_pokerPlayer: PokerPlayer) extends Move(_pokerPlayer) {
    def player() = _pokerPlayer
    def description(): String
    def displayTo(p: PokerPlayer): String =
      (if (_pokerPlayer != p) _pokerPlayer.description else "You") + " " + description() + "."
  }

  case class Call(pokerPlayer: PokerPlayer) extends PokerMove(pokerPlayer) {
    def description() = "call"
  }
  case class Raise(pokerPlayer: PokerPlayer, amount: Int) extends PokerMove(pokerPlayer) {
    def description() = "raise the bet by " + amount
  }
  case class Fold(pokerPlayer: PokerPlayer) extends PokerMove(pokerPlayer) {
    def description() = "fold"
  }
  case class Deal() extends PokerMove(dealer) {
    def description() = "initial deal"
  }
  case class Flop() extends PokerMove(dealer) {
    def description() = "reveal the flop"
  }
  case class Turn() extends PokerMove(dealer) {
    def description() = "reveal the turn"
  }
  case class River() extends PokerMove(dealer) {
    def description() = "reveal the river"
  }

  case class PokerState(
    playerFn: PokerState => PokerPlayer,
    deck: Deck,
    shared: IndexedSeq[Card], // flop, river, etc
    numShown: Int,
    hands: Map[PokerPlayer, Seq[Card]],
    pot: Int,
    currentBet: Int,
    stillIn: Set[PokerPlayer],
    inFors: Map[PokerPlayer, Int],
    piles: Map[PokerPlayer, Int])
    extends State() {

    lazy val _player = playerFn(this)

    def player() = _player

    def firstBetter() = _players.find(stillIn.contains(_)).get

    // TODO: another round of betting after river is shown
    def betterAfter(before: PokerPlayer): Option[PokerPlayer] = {
      if (stillIn.forall(p => inFors.get(p).map(_ == currentBet).getOrElse(false))) {
        None
      } else {
        // 'psi' !stillIn.contains(p) after a fold
        val psi = _players.filter(p => stillIn.contains(p) || p == before)
        Some(psi((psi.indexOf(before) + 1) % psi.length))
      }
    }

    def displayTo(viewer: PokerPlayer): String =
      "To: " + player + "\n" +
        "Current bet: " + currentBet + "\n" +
        "Pot: " + pot + "\n" +
        "Shared: " + shared.zipWithIndex.map({
          case (card, i) => if (i < numShown) card.toString else "??"
        }).mkString(" ") + "\n" +
        "\n" +
        players.map(p => {
          p.id + ": " +
            " hand " + (
              hands.get(p).map(_.map(c =>
                if (viewer == p || (numShown == 5 && stillIn.size > 1)) // TODO update this logic when there is betting after the river
                  c.toString
                else
                  "??"
              ).mkString(" ")).getOrElse("--")
            ) + " " +
              (if (stillIn.contains(p))
                "in for $" + inFors.get(p).map(_.toString).getOrElse("--")
              else
                "out") +
              ", $" + piles.get(p).map(_.toString).getOrElse("--") + " remaining"
        }).mkString("\n")

    def moves(): Seq[PokerMove] = List()

    def outcome(): Option[PokerOutcome] =
      if (numShown < 5 && stillIn.size > 1) {
        None
      } else {
        if (stillIn.size == 1) {
          Some(PokerOutcome(stillIn.toIndexedSeq.head, None))
        } else {
          val (winner, hand) = hands
            .filter({ case (p, cards) => stillIn.contains(p) }).toList
            .map({ case (p, cards) => (p, (shared ++ cards).combinations(5).map(PokerHand(_)).toList.max) })
            .maxBy(_._2)
          Some(PokerOutcome(winner, Some(hand)))
        }
      }

    // TODO big/small blind
    // TODO: is there a limit to the number of raises that can occur?
    // TODO: maximum bet
    // TODO: how to handle player exhausting pile during game?

    def apply(move: PokerMove): Option[PokerState] = move match {

      case Deal() => {
        // TODO clean up these range calculations
        val cards = Vector() ++ deck.cards
        val hands = _players.zipWithIndex.map({ case (player, i) => (player, cards(i * 2 to i * 2 + 1)) }).toMap
        val shared = cards(_players.size * 2 to _players.size * 2 + 4)
        val unused = cards((_players.size * 2 + 5) until cards.length)
        Some(PokerState(
          _.firstBetter,
          Deck(unused),
          shared,
          numShown,
          hands,
          pot,
          currentBet,
          stillIn,
          Map(),
          piles
        ))
      }

      case Raise(player, amount) => {
        val diff = currentBet + amount - inFors.get(player).getOrElse(0)
        if (piles(player) - diff >= 0) {
          Some(PokerState(
            _.betterAfter(player).getOrElse(dealer),
            deck,
            shared,
            numShown,
            hands,
            pot + diff,
            currentBet + amount,
            stillIn,
            inFors + (player -> (currentBet + amount)),
            piles + (player -> (piles(player) - diff))
          ))
        } else {
          None
        }
      }

      case Call(player) => {
        val diff = currentBet - inFors.get(player).getOrElse(0)
        if (piles(player) - diff >= 0) {
          Some(PokerState(
            _.betterAfter(player).getOrElse(dealer),
            deck,
            shared,
            numShown,
            hands,
            pot + diff,
            currentBet,
            stillIn,
            inFors + (player -> currentBet),
            piles + (player -> (piles(player) - diff))
          ))
        } else {
          None
        }
      }

      case Fold(player) =>
        Some(PokerState(
          _.betterAfter(player).getOrElse(dealer),
          deck, shared, numShown, hands, pot, currentBet, stillIn - player, inFors - player, piles))

      case Flop() =>
        Some(PokerState(
          _.firstBetter,
          deck, shared, 3, hands, pot, 0, stillIn, Map(), piles))

      case Turn() =>
        Some(PokerState(
          _.firstBetter,
          deck, shared, 4, hands, pot, 0, stillIn, Map(), piles))

      case River() =>
        Some(PokerState(
          _.firstBetter,
          deck, shared, 5, hands, pot, 0, stillIn, Map(), piles))

    }
  }

  case class PokerOutcome(winner: PokerPlayer, hand: Option[PokerHand]) extends Outcome(Some(winner)) {

    override def toString(): String =
      "Winner: " + winner.description + "\n" +
        "Hand  : " + hand.map(h => h.toString + " " + h.description).getOrElse("not shown") + "\n"

  }

  abstract class PokerPlayer(id: String, _description: String) extends Player(id, _description) {
    def description() = _description
  }

  class AIPokerPlayer(aitttPlayerId: String, aitttDescription: String = "minimax")
    extends PokerPlayer(aitttPlayerId, aitttDescription) {

    val heuristic = (state: PokerState) => players.map(p => {
      (p, state.outcome.map(out => if (out.winner == Some(p)) 1.0 else -1.0).getOrElse(0.0))
    }).toMap

    def move(state: PokerState): (PokerMove, PokerState) = {
      val (move, newState, values) = poker.minimax(state, 3, heuristic)
      (move, newState)
    }
  }

  class RandomPokerPlayer(id: String, description: String = "random")
    extends PokerPlayer(id, description) {

    def move(state: PokerState): (PokerMove, PokerState) = {
      val opens = state.moves
      val move = opens(nextInt(opens.length))
      (move, state(move).get)
    }
  }

  class DealerPokerPlayer(id: String, description: String = "dealer")
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

  class InteractivePokerPlayer(id: String, description: String = "human")
    extends PokerPlayer(id, description) {

    val eventQueue = mutable.ListBuffer[Event]()

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

    override def notify(event: Event): Unit = {
      eventQueue += event
    }

    override def displayEvents(): Unit = {
      println()
      val info = eventQueue.map(_.displayTo(this)).mkString("  ")
      println(info)
      eventQueue.clear()
    }

    override def endGame(state: STATE): Unit = {
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

}

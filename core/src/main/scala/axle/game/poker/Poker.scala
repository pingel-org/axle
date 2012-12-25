package axle.game.poker

import axle.game._

import axle._
import util.Random.nextInt
import collection._
import axle.game.cards._
import axle.game.cards.Implicits._
import Stream.cons

class Poker(numPlayers: Int) extends Game {

  poker =>

  type PLAYER = PokerPlayer
  type MOVE = PokerMove
  type STATE = PokerState
  type OUTCOME = PokerOutcome

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
      0.0, // pot
      0.0, // current bet
      _players.toSet, // stillIn
      Map(), // inFors
      players.map(player => (player, 100.0)).toMap // piles
    )

  def introMessage() = "Welcome to Axle Texas Hold Em Poker"

  def players() = _players.toSet

  abstract class PokerMove(_pokerPlayer: PokerPlayer) extends Move(_pokerPlayer) {
    def player() = _pokerPlayer
    def description(): String
    def displayTo(p: PokerPlayer): String =
      (if (_pokerPlayer != p) _pokerPlayer.id else "You") + " " + description() + "."
  }

  case class Call(pokerPlayer: PokerPlayer) extends PokerMove(pokerPlayer) {
    def description() = "call"
  }
  case class Raise(pokerPlayer: PokerPlayer, amount: Double) extends PokerMove(pokerPlayer) {
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
    pot: Double,
    currentBet: Double,
    stillIn: Set[PokerPlayer],
    inFors: Map[PokerPlayer, Double],
    piles: Map[PokerPlayer, Double])
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

    override def toString(): String =
      "To: " + player + "\n" +
        "Current bet: " + currentBet + "\n" +
        "Pot: " + pot + "\n" +
        "Shared: " + shared.zipWithIndex.map({
          case (card, i) => if (i < numShown) card.toString else "??"
        }).mkString(" ") + "\n" +
        "\n" +
        players.map(player => {
          player.id + ": " +
            " hand " + hands.get(player).map(_.map(_.toString).mkString(" ")).getOrElse("--") + " " +
            (if (stillIn.contains(player))
              "in for $" + inFors.get(player).map(_.toString).getOrElse("--")
            else
              "out") +
            ", $" + piles.get(player).map(_.toString).getOrElse("--") + " remaining"
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

    def apply(move: PokerMove): PokerState = {
      move match {

        case Deal() => {
          // TODO big/small blind
          // TODO clean up these range calculations
          val cards = Vector() ++ deck.cards
          val hands = _players.zipWithIndex.map({ case (player, i) => (player, cards(i * 2 to i * 2 + 1)) }).toMap
          val shared = cards(_players.size * 2 to _players.size * 2 + 4)
          val unused = cards((_players.size * 2 + 5) until cards.length)
          PokerState(
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
          )
        }

        case Raise(player, amount) => {
          val diff = currentBet + amount - inFors.get(player).getOrElse(0.0)
          PokerState(
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
          )
        }

        case Call(player) => {
          val diff = currentBet - inFors.get(player).getOrElse(0.0)
          PokerState(
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
          )
        }

        case Fold(player) =>
          PokerState(
            _.betterAfter(player).getOrElse(dealer),
            deck, shared, numShown, hands, pot, currentBet, stillIn - player, inFors - player, piles)

        case Flop() =>
          PokerState(
            _.firstBetter,
            deck, shared, 3, hands, pot, 0, stillIn, Map(), piles)

        case Turn() =>
          PokerState(
            _.firstBetter,
            deck, shared, 4, hands, pot, 0, stillIn, Map(), piles)

        case River() =>
          PokerState(
            _.firstBetter,
            deck, shared, 5, hands, pot, 0, stillIn, Map(), piles)

      }
    }

  }

  case class PokerOutcome(winner: PokerPlayer, hand: Option[PokerHand]) extends Outcome(Some(winner))

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
        if (state.inFors.size == 0)
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
        
  check
  raise 1.0
  call
  fold
        
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
      println()
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

    def parseMove(player: PokerPlayer, moveStr: String): Option[PokerMove] = {
      val tokens = moveStr.split("\\s+")
      if (tokens.length > 0) {
        tokens(0) match {
          // TODO 'check' is a 'call' when currentBet == 0.  Might want to model this.
          case "c" | "check" | "call" => Some(Call(player))
          case "r" | "raise" => {
            if (tokens.length == 2)
              try {
                Some(Raise(player, tokens(1).toDouble))
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

    def isValidMove(state: PokerState, move: PokerMove): Boolean = {
      // TODO: is there a limit to the number of raises that can occur?
      // TODO: maximum bet
      true // TODO
    }

    def chooseMove(state: PokerState): PokerMove = {
      displayEvents()
      println(state)
      userInputStream().flatMap(parseMove(state.player, _)).find(move => isValidMove(state, move)).get
    }

  }

}

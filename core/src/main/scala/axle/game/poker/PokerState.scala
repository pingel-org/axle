package axle.game.poker

import axle._
import axle.game.cards._
import axle.game._

case class PokerState(
  playerFn: PokerState => PokerPlayer,
  deck: Deck,
  shared: IndexedSeq[Card], // flop, turn, river
  numShown: Int,
  hands: Map[PokerPlayer, Seq[Card]],
  pot: Int,
  currentBet: Int,
  stillIn: Set[PokerPlayer],
  inFors: Map[PokerPlayer, Int],
  piles: Map[PokerPlayer, Int],
  _outcome: Option[PokerOutcome] = None)(implicit game: Poker)
  extends State[Poker]() {

  implicit val pokerHandOrdering = new PokerHandOrdering()
  implicit val pokerHandCategoryOrdering = new PokerHandCategoryOrdering()

  val bigBlind = 2 // the "minimum bet"
  val smallBlind = bigBlind / 2

  lazy val _player = playerFn(this)

  def player() = _player

  def firstBetter() = game._players.find(stillIn.contains(_)).get

  def betterAfter(before: PokerPlayer): Option[PokerPlayer] = {
    if (stillIn.forall(p => inFors.get(p).map(_ == currentBet).getOrElse(false))) {
      None
    } else {
      // 'psi' !stillIn.contains(p) after a fold
      val psi = game._players.filter(p => stillIn.contains(p) || p == before)
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
      game.players.map(p => {
        p.id + ": " +
          " hand " + (
            hands.get(p).map(_.map(c =>
              if (viewer == p || (_outcome.isDefined && stillIn.size > 1))
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

  def outcome(): Option[PokerOutcome] = _outcome

  // TODO: is there a limit to the number of raises that can occur?
  // TODO: how to handle player exhausting pile during game?

  def apply(move: PokerMove): Option[PokerState] = move match {

    case Deal() => {
      // TODO clean up these range calculations
      val cards = Vector() ++ deck.cards
      val hands = game._players.zipWithIndex.map({ case (player, i) => (player, cards(i * 2 to i * 2 + 1)) }).toMap
      val shared = cards(game._players.size * 2 to game._players.size * 2 + 4)
      val unused = cards((game._players.size * 2 + 5) until cards.length)

      // TODO: should blinds be a part of the "deal" or are they minimums during first round of betting?
      val orderedStillIn = game._players.filter(stillIn.contains(_))
      val smallBlindPlayer = orderedStillIn(0)
      val bigBlindPlayer = orderedStillIn(1) // list should be at least this long

      val nextBetter = orderedStillIn(2 % orderedStillIn.size)

      // TODO: some kind of "transfer" method that handles money flow from better
      // to pot would simplify the code and make it less error prone

      Some(PokerState(
        s => nextBetter,
        Deck(unused),
        shared,
        numShown,
        hands,
        pot + smallBlind + bigBlind,
        bigBlind,
        stillIn,
        Map(smallBlindPlayer -> smallBlind, bigBlindPlayer -> bigBlind),
        piles + (smallBlindPlayer -> (piles(smallBlindPlayer) - smallBlind)) + (bigBlindPlayer -> (piles(bigBlindPlayer) - bigBlind))
      ))
    }

    case Raise(player, amount) => {
      val diff = currentBet + amount - inFors.get(player).getOrElse(0)
      if (piles(player) - diff >= 0) {
        Some(PokerState(
          _.betterAfter(player).getOrElse(game.dealer),
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
          _.betterAfter(player).getOrElse(game.dealer),
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
        _.betterAfter(player).getOrElse(game.dealer),
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

    case Payout() => {

      val (winner, handOpt) =
        if (stillIn.size == 1) {
          (stillIn.toIndexedSeq.head, None)
        } else {
          // TODO: handle tie
          val (winner, hand) = hands
            .filter({ case (p, cards) => stillIn.contains(p) }).toList
            .map({ case (p, cards) => (p, (shared ++ cards).combinations(5).map(PokerHand(_)).toList.max) })
            .maxBy(_._2)
          (winner, Some(hand))
        }

      val newPiles = piles + (winner -> (piles(winner) + pot))

      val newStillIn = game._players.filter(newPiles(_) >= bigBlind).toSet

      Some(PokerState(
        s => game.dealer,
        deck, shared, 5, hands, 0, 0, newStillIn, Map(), newPiles, Some(PokerOutcome(winner, handOpt))
      ))
    }

  }
}

package axle.game.poker

import axle._
import axle.game.cards._
import axle.game._
import spire.implicits._
import spire.compat.ordering

case class PokerState(
    moverFn: PokerState => Player,
    deck: Deck,
    shared: IndexedSeq[Card], // flop, turn, river
    numShown: Int,
    hands: Map[Player, Seq[Card]],
    pot: Int,
    currentBet: Int,
    stillIn: Set[Player],
    inFors: Map[Player, Int],
    piles: Map[Player, Int],
    _outcome: Option[PokerOutcome],
    _eventQueues: Map[Player, List[Either[PokerOutcome, PokerMove]]]) {

  val bigBlind = 2 // the "minimum bet"
  val smallBlind = bigBlind / 2

  lazy val _mover = moverFn(this)

  def mover: Player = _mover

  def firstBetter(game: Poker): Player = game.players.find(stillIn.contains).get

  def betterAfter(before: Player, game: Poker): Option[Player] = {
    if (stillIn.forall(p => inFors.get(p).map(_ === currentBet).getOrElse(false))) {
      None
    } else {
      // 'psi' !stillIn.contains(p) after a fold
      val psi = game.players.filter(p => stillIn.contains(p) || p === before)
      Some(psi((psi.indexOf(before) + 1) % psi.length))
    }
  }

  // TODO: displayTo could be phrased in terms of Show
  def displayTo(viewer: Player, game: Poker): String =
    "To: " + mover.referenceFor(viewer) + "\n" +
      "Current bet: " + currentBet + "\n" +
      "Pot: " + pot + "\n" +
      "Shared: " + shared.zipWithIndex.map({
        case (card, i) => if (i < numShown) string(card) else "??"
      }).mkString(" ") + "\n" +
      "\n" +
      game.players.map(p => {
        p.id + ": " +
          " hand " + (
            hands.get(p).map(_.map(c =>
              if (viewer === p || (_outcome.isDefined && stillIn.size > 1)) {
                string(c)
              } else {
                "??"
              }).mkString(" ")).getOrElse("--")) + " " +
            (if (stillIn.contains(p)) {
              "in for $" + inFors.get(p).map(amt => string(amt)).getOrElse("--")
            } else {
              "out"
            }) +
            ", $" + piles.get(p).map(amt => string(amt)).getOrElse("--") + " remaining"
      }).mkString("\n")

  def moves(game: Poker): Seq[PokerMove] = List()

  def outcome(game: Poker): Option[PokerOutcome] = _outcome

  // TODO: is there a limit to the number of raises that can occur?
  // TODO: how to handle player exhausting pile during game?

  def apply(move: PokerMove, game: Poker): PokerState = move match {

    case Deal(dealer) => {
      // TODO clean up these range calculations
      val cards = Vector() ++ deck.cards
      val hands = game.players.zipWithIndex.map({ case (player, i) => (player, cards(i * 2 to i * 2 + 1)) }).toMap
      val shared = cards(game.players.size * 2 to game.players.size * 2 + 4)
      val unused = cards((game.players.size * 2 + 5) until cards.length)

      // TODO: should blinds be a part of the "deal" or are they minimums during first round of betting?
      val orderedStillIn = game.players.filter(stillIn.contains)
      val smallBlindPlayer = orderedStillIn(0)
      val bigBlindPlayer = orderedStillIn(1) // list should be at least this long

      val nextBetter = orderedStillIn(2 % orderedStillIn.size)

      // TODO: some kind of "transfer" method that handles money flow from better
      // to pot would simplify the code and make it less error prone

      PokerState(
        s => nextBetter,
        Deck(unused),
        shared,
        numShown,
        hands,
        pot + smallBlind + bigBlind,
        bigBlind,
        stillIn,
        Map(smallBlindPlayer -> smallBlind, bigBlindPlayer -> bigBlind),
        piles + (smallBlindPlayer -> (piles(smallBlindPlayer) - smallBlind)) + (bigBlindPlayer -> (piles(bigBlindPlayer) - bigBlind)),
        None,
        _eventQueues)
    }

    case Raise(player, amount) => {
      val diff = currentBet + amount - inFors.get(player).getOrElse(0)
      assert(piles(player) - diff >= 0)
      PokerState(
        _.betterAfter(player, game).getOrElse(game.dealer),
        deck,
        shared,
        numShown,
        hands,
        pot + diff,
        currentBet + amount,
        stillIn,
        inFors + (player -> (currentBet + amount)),
        piles + (player -> (piles(player) - diff)),
        None,
        _eventQueues)
    }

    case Call(player) => {
      val diff = currentBet - inFors.get(player).getOrElse(0)
      assert(piles(player) - diff >= 0)
      PokerState(
        _.betterAfter(player, game).getOrElse(game.dealer),
        deck,
        shared,
        numShown,
        hands,
        pot + diff,
        currentBet,
        stillIn,
        inFors + (player -> currentBet),
        piles + (player -> (piles(player) - diff)),
        None,
        _eventQueues)
    }

    case Fold(player) =>
      PokerState(
        _.betterAfter(player, game).getOrElse(game.dealer),
        deck, shared, numShown, hands, pot, currentBet, stillIn - player, inFors - player, piles,
        None,
        _eventQueues)

    case Flop(dealer) =>
      PokerState(
        _.firstBetter(game),
        deck, shared, 3, hands, pot, 0, stillIn, Map(), piles,
        None,
        _eventQueues)

    case Turn(dealer) =>
      PokerState(
        _.firstBetter(game),
        deck, shared, 4, hands, pot, 0, stillIn, Map(), piles,
        None,
        _eventQueues)

    case River(dealer) =>
      PokerState(
        _.firstBetter(game),
        deck, shared, 5, hands, pot, 0, stillIn, Map(), piles,
        None,
        _eventQueues)

    case Payout(dealer) => {

      val (winner, handOpt) =
        if (stillIn.size === 1) {
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

      val newStillIn = game.players.filter(newPiles(_) >= bigBlind).toSet

      PokerState(
        s => game.dealer,
        deck,
        shared,
        5,
        hands,
        0,
        0,
        newStillIn,
        Map(),
        newPiles,
        Some(PokerOutcome(Some(winner), handOpt)),
        _eventQueues)
    }

  }

  def eventQueues: Map[Player, List[Either[PokerOutcome, PokerMove]]] = _eventQueues

  def setEventQueues(qs: Map[Player, List[Either[PokerOutcome, PokerMove]]]): PokerState = PokerState(
    moverFn,
    deck,
    shared,
    numShown,
    hands,
    pot,
    currentBet,
    stillIn,
    inFors,
    piles,
    _outcome,
    qs)

}

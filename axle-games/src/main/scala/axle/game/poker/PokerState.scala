package axle.game.poker

import axle._
import axle.game.cards._
import axle.game._
import spire.implicits._
import spire.compat.ordering

case class PokerState(
    moverFn: PokerState => Option[Player],
    deck: Deck,
    shared: IndexedSeq[Card], // flop, turn, river
    numShown: Int,
    hands: Map[Player, Seq[Card]],
    pot: Int,
    currentBet: Int,
    stillIn: Set[Player],
    inFors: Map[Player, Int],
    piles: Map[Player, Int],
    _outcome: Option[PokerOutcome]) {

  val bigBlind = 2 // the "minimum bet"
  val smallBlind = bigBlind / 2

  lazy val _mover = moverFn(this)

  def moverOpt: Option[Player] = _mover

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
    moverOpt.map(mover => "To: " + mover.referenceFor(viewer) + "\n").getOrElse("") +
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

  def moves(game: Poker): Seq[PokerMove] =
    moverOpt map { mover =>

      if (mover === game.dealer) {
        numShown match {
          case 0 =>
            if (inFors.size === 0) {
              Deal() :: Nil
            } else {
              Flop() :: Nil
            }
          case 3 => Turn() :: Nil
          case 4 => River() :: Nil
          case 5 => Payout() :: Nil
        }
      } else {
        val maxPersonalRaise = piles(mover) + inFors.get(mover).getOrElse(0) - currentBet

        val maxTableRaise = game.players.map(p => piles(p) + inFors.get(p).getOrElse(0)).min - currentBet

        assert(maxTableRaise <= maxPersonalRaise)
        // This policy is a workaround for not supporting multiple pots, which are required when
        // a player with a small "pile" "goes all in", but other still-in players have "piles" large
        // enough that allow them to raise the bet beyond that level.
        val maxRaise = maxTableRaise

        // given the above policy, this should always be true:
        val canCall = currentBet - inFors.get(mover).getOrElse(0) <= piles(mover)
        assert(canCall)

        Fold() :: (if (canCall) (Call() :: Nil) else Nil) ++ (1 to maxRaise).map(Raise.apply).toList
      }
    } getOrElse (List.empty)

  def outcome(game: Poker): Option[PokerOutcome] = _outcome

  // TODO: is there a limit to the number of raises that can occur?
  // TODO: how to handle player exhausting pile during game?

  def apply(game: Poker, move: PokerMove): PokerState = {

    val mover = moverOpt.get // TODO .get

    move match {

      case Deal() => {
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
          s => Some(nextBetter),
          Deck(unused),
          shared,
          numShown,
          hands,
          pot + smallBlind + bigBlind,
          bigBlind,
          stillIn,
          Map(smallBlindPlayer -> smallBlind, bigBlindPlayer -> bigBlind),
          piles + (smallBlindPlayer -> (piles(smallBlindPlayer) - smallBlind)) + (bigBlindPlayer -> (piles(bigBlindPlayer) - bigBlind)),
          None)
      }

      case Raise(amount) => {
        val diff = currentBet + amount - inFors.get(mover).getOrElse(0)
        assert(piles(moverOpt.get) - diff >= 0)
        PokerState(
          s => Some(s.betterAfter(mover, game).getOrElse(game.dealer)),
          deck,
          shared,
          numShown,
          hands,
          pot + diff,
          currentBet + amount,
          stillIn,
          inFors + (mover -> (currentBet + amount)),
          piles + (mover -> (piles(mover) - diff)),
          None)
      }

      case Call() => {
        val diff = currentBet - inFors.get(mover).getOrElse(0)
        assert(piles(mover) - diff >= 0)
        PokerState(
          s => Some(s.betterAfter(mover, game).getOrElse(game.dealer)),
          deck,
          shared,
          numShown,
          hands,
          pot + diff,
          currentBet,
          stillIn,
          inFors + (mover -> currentBet),
          piles + (mover -> (piles(mover) - diff)),
          None)
      }

      case Fold() =>
        PokerState(
          s => Some(s.betterAfter(mover, game).getOrElse(game.dealer)),
          deck, shared, numShown, hands, pot, currentBet, stillIn - mover, inFors - mover, piles,
          None)

      case Flop() =>
        PokerState(
          s => Some(s.firstBetter(game)),
          deck, shared, 3, hands, pot, 0, stillIn, Map(), piles,
          None)

      case Turn() =>
        PokerState(
          s => Some(s.firstBetter(game)),
          deck, shared, 4, hands, pot, 0, stillIn, Map(), piles,
          None)

      case River() =>
        PokerState(
          s => Some(s.firstBetter(game)),
          deck, shared, 5, hands, pot, 0, stillIn, Map(), piles,
          None)

      case Payout() => {

        val (winner, handOpt) =
          if (stillIn.size === 1) {
            (stillIn.toIndexedSeq.head, None)
          } else {
            // TODO: handle tie
            val (winner, hand) = hands
              .filter({ case (p, cards) => stillIn.contains(p) }).toList
              .map({ case (p, cards) => (p, (shared ++ cards).combinations(5).map(PokerHand.apply).toList.max) })
              .maxBy(_._2)
            (winner, Some(hand))
          }

        val newPiles = piles + (winner -> (piles(winner) + pot))

        val newStillIn = game.players.filter(newPiles(_) >= bigBlind).toSet

        PokerState(
          s => None,
          deck,
          shared,
          5,
          hands,
          0,
          0,
          newStillIn,
          Map(),
          newPiles,
          Some(PokerOutcome(Some(winner), handOpt)))
      }

    }
  }

}

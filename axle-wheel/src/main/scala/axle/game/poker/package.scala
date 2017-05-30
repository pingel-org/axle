package axle.game

import axle.string
import axle.game.cards._
import axle.stats.Distribution0
import spire.math.Rational
import cats.Order.catsKernelOrderingForOrder
import cats.implicits._

package object poker {

  lazy val moveParser = MoveParser()

  // TODO: is there a limit to the number of raises that can occur?
  // TODO: how to handle player exhausting pile during game?

  implicit val evGame: Game[Poker, PokerState, PokerOutcome, PokerMove, PokerStateMasked, PokerMove] =
    new Game[Poker, PokerState, PokerOutcome, PokerMove, PokerStateMasked, PokerMove] {

      def startState(g: Poker): PokerState =
        PokerState(
          state => Some(g.dealer),
          Deck(),
          Vector(),
          0, // # of shared cards showing
          Map[Player, Seq[Card]](),
          0, // pot
          0, // current bet
          g.players.toSet, // stillIn
          Map(), // inFors
          g.players.map(player => (player, 100)).toMap, // piles
          None)

      def startFrom(g: Poker, s: PokerState): Option[PokerState] = {

        if (s.stillIn.size > 1) {
          Some(PokerState(
            state => Some(g.dealer),
            Deck(),
            Vector(),
            0,
            Map(),
            0,
            0,
            s.stillIn,
            Map(),
            s.piles,
            None))
        } else {
          None
        }
      }

      def players(g: Poker): IndexedSeq[Player] =
        g.players

      def strategyFor(g: Poker, player: Player): (Poker, PokerStateMasked) => Distribution0[PokerMove, Rational] =
        g.playerToStrategy(player)

      // TODO: this implementation works, but ideally there is more information in the error
      // string about why the move is invalid (eg player raised more than he had)
      def isValid(game: Poker, state: PokerStateMasked, move: PokerMove): Either[String, PokerMove] = {
        if (moves(game, state).contains(move)) {
          Right(move)
        } else {
          Left("invalid move")
        }
      }

      def applyMove(game: Poker, state: PokerState, move: PokerMove): PokerState = {

        import state._

        val mover = _mover.get // TODO .get

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
            assert(piles(_mover.get) - diff >= 0)
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
              s => Some(if (stillIn.size == 2) game.dealer else s.betterAfter(mover, game).getOrElse(game.dealer)),
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
              numShown,
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

      def mover(game: Poker, s: PokerState): Option[Player] = s._mover

      def moverM(game: Poker, s: PokerStateMasked): Option[Player] = s.mover

      def moves(game: Poker, s: PokerStateMasked): Seq[PokerMove] = {

        s.mover map { mvr =>

          if (mvr === game.dealer) {
            ((s.stillIn.size, s.shownShared.length) match {
              case (1, _) => Payout()
              case (_, 0) => if (s.inFors.size === 0) Deal() else Flop()
              case (_, 3) => Turn()
              case (_, 4) => River()
              case (_, 5) => Payout()
            }) :: Nil
          } else {
            val maxPersonalRaise = s.piles(mvr) + s.inFors.get(mvr).getOrElse(0) - s.currentBet

            val maxTableRaise = game.players.map(p => s.piles(p) + s.inFors.get(p).getOrElse(0)).min - s.currentBet

            assert(maxTableRaise <= maxPersonalRaise)
            // This policy is a workaround for not supporting multiple pots, which are required when
            // a player with a small "pile" "goes all in", but other still-in players have "piles" large
            // enough that allow them to raise the bet beyond that level.
            val maxRaise = maxTableRaise

            // given the above policy, this should always be true:
            val canCall = s.currentBet - s.inFors.get(mvr).getOrElse(0) <= s.piles(mvr)
            assert(canCall)

            Fold() :: (if (canCall) (Call() :: Nil) else Nil) ++ (1 to maxRaise).map(Raise.apply).toList
          }
        } getOrElse (List.empty)
      }

      def maskState(game: Poker, state: PokerState, observer: Player): PokerStateMasked = {
        PokerStateMasked(
          mover = state._mover,
          shownShared = state.shared.take(state.numShown),
          hands =
            if (state._outcome.isDefined && state.stillIn.size > 1) {
              state.hands.filterKeys { state.stillIn.contains }
            } else {
              state.hands.filterKeys { _ === observer }
            },
          pot = state.pot,
          currentBet = state.currentBet,
          stillIn = state.stillIn,
          inFors = state.inFors,
          piles = state.piles)
      }

      def maskMove(game: Poker, move: PokerMove, mover: Player, observer: Player): PokerMove =
        move

      def outcome(game: Poker, state: PokerState): Option[PokerOutcome] = state._outcome

    }

  implicit val evGameIO: GameIO[Poker, PokerOutcome, PokerMove, PokerStateMasked, PokerMove] =
    new GameIO[Poker, PokerOutcome, PokerMove, PokerStateMasked, PokerMove] {

      def displayerFor(g: Poker, player: Player): String => Unit =
        g.playerToDisplayer(player)

      def parseMove(g: Poker, input: String): Either[String, PokerMove] =
        moveParser.parse(input)

      def introMessage(g: Poker) = """
Texas Hold Em Poker

Example moves:

  check
  raise 1
  call
  fold

"""

      def displayStateTo(game: Poker, s: PokerStateMasked, observer: Player): String = {
        s.mover.map(mover => "To: " + mover.referenceFor(observer) + "\n").getOrElse("") +
          "Current bet: " + s.currentBet + "\n" +
          "Pot: " + s.pot + "\n" +
          "Shared: " + s.shownShared.map({
            card => string(card)
          }).mkString(" ") + "\n" +
          "\n" +
          game.players.map(p => {
            p.id + ": " +
              " hand " + (
                s.hands.get(p).map(h => h.map(c => string(c)).mkString(" ")).getOrElse("--")) + " " +
                (if (s.stillIn.contains(p)) {
                  "in for $" + s.inFors.get(p).map(amt => string(amt)).getOrElse("--")
                } else {
                  "out"
                }) +
                ", $" + s.piles.get(p).map(amt => string(amt)).getOrElse("--") + " remaining"
          }).mkString("\n")
      }

      def displayOutcomeTo(
        game: Poker,
        outcome: PokerOutcome,
        observer: Player): String = {
        "Winner: " + outcome.winner.get.description + "\n" +
          "Hand  : " + outcome.hand.map(h => string(h) + " " + h.description).getOrElse("not shown") + "\n"
      }

      def displayMoveTo(game: Poker, move: PokerMove, mover: Player, observer: Player): String =
        mover.referenceFor(observer) + " " + move.description + "."

    }

}

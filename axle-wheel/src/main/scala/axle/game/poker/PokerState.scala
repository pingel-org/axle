package axle.game.poker

import axle.game._
import axle.game.cards._
import cats.implicits._

case class PokerStateMasked(
  mover: Option[Player],
  shownShared: IndexedSeq[Card], // length = numShown
  hands: Map[Player, Seq[Card]],
  pot: Int,
  currentBet: Int,
  stillIn: Set[Player],
  inFors: Map[Player, Int],
  piles: Map[Player, Int])

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

}

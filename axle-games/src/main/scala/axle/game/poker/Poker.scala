package axle.game.poker

import axle.game._
import axle.game.cards._

// (1 to numPlayers).map(i => player("P" + i, "Player " + i, "human"))
// def players: IndexedSeq[PokerPlayer] = _players

case class Poker(players: IndexedSeq[PokerPlayer]) extends Game[Poker] {

  val numPlayers = players.length

  type PLAYER = PokerPlayer
  type MOVE = PokerMove
  type STATE = PokerState
  type OUTCOME = PokerOutcome

  val dealer = PokerPlayerDealer("D", "Dealer")

  def startState: PokerState =
    PokerState(
      state => dealer,
      Deck(),
      Vector(),
      0, // # of shared cards showing
      Map[PokerPlayer, Seq[Card]](),
      0, // pot
      0, // current bet
      players.toSet, // stillIn
      Map(), // inFors
      players.map(player => (player, 100)).toMap, // piles
      None,
      Map())

  def startFrom(s: PokerState): Option[PokerState] = {

    if (s.stillIn.size > 0) {
      Some(PokerState(
        state => dealer,
        Deck(),
        Vector(),
        0,
        Map(),
        0,
        0,
        s.stillIn,
        Map(),
        s.piles,
        None,
        Map()))
    } else {
      None
    }
  }

  def introMessage: String = "Welcome to Axle Texas Hold Em Poker"

}

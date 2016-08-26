package axle.game.poker

import axle.game._
import axle.game.cards._

case class Poker(numPlayers: Int) extends Game[Poker] {

  implicit val poker = this

  type PLAYER = PokerPlayer
  type MOVE = PokerMove
  type STATE = PokerState
  type OUTCOME = PokerOutcome

  val dealer = player("D", "Dealer", "dealer")

  val _players = (1 to numPlayers).map(i => player("P" + i, "Player " + i, "human"))

  def player(id: String, description: String, which: String): PokerPlayer = which match {
    case "random" => RandomPokerPlayer(id, description)
    case "ai"     => PokerPlayerAI(id, description)
    case "dealer" => PokerPlayerDealer(id, description)
    case _        => PokerPlayerInteractive(id, description)
  }

  def startState: PokerState =
    PokerState(
      state => dealer,
      Deck(),
      Vector(),
      0, // # of shared cards showing
      Map[PokerPlayer, Seq[Card]](),
      0, // pot
      0, // current bet
      _players.toSet, // stillIn
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

  def players: Set[PokerPlayer] = _players.toSet

}

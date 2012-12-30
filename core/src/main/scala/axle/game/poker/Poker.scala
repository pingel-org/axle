package axle.game.poker

import axle.game._

import axle._
import util.Random.nextInt
import collection._
import axle.game.cards._
import Stream.cons

class Poker(numPlayers: Int) extends Game[Poker] {

  implicit val poker = this

  type PLAYER = PokerPlayer
  type MOVE = PokerMove
  type STATE = PokerState
  type OUTCOME = PokerOutcome

  val dealer = player("D", "Dealer", "dealer")

  val _players = (1 to numPlayers).map(i => player("P" + i, "Player " + i, "human"))

  def player(id: String, description: String, which: String) = which match {
    case "random" => new RandomPokerPlayer(id, description)
    case "ai" => new PokerPlayerAI(id, description)
    case "dealer" => new PokerPlayerDealer(id, description)
    case _ => new PokerPlayerInteractive(id, description)
  }

  def startState() =
    PokerState(
      state => dealer,
      Deck(),
      Vector(),
      0, // # of shared cards showing
      immutable.Map[PokerPlayer, Seq[Card]](),
      0, // pot
      0, // current bet
      _players.toSet, // stillIn
      immutable.Map(), // inFors
      players.map(player => (player, 100)).toMap // piles
    )

  def startFrom(s: PokerState) = {

    val newPiles = s.outcome.map(o => {
      s.piles + (o.winner -> (s.piles(o.winner) + s.pot))
    }).getOrElse(s.piles)

    val newStillIn = _players.filter(newPiles(_) > 0).toSet

    if (newStillIn.size > 0)
      Some(PokerState(
        state => dealer,
        Deck(),
        Vector(),
        0,
        immutable.Map(),
        0,
        0,
        newStillIn,
        immutable.Map(),
        newPiles
      ))
    else
      None
  }

  def introMessage() = "Welcome to Axle Texas Hold Em Poker"

  def players() = _players.toSet

}

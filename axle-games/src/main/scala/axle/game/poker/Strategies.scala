package axle.game.poker

import axle.game._
import util.Random.nextInt
import spire.implicits._
import spire.math.Real

object Strategies {

  def dealerMove(state: PokerState, game: Poker): PokerMove = {
    val dealer = state.mover
    state.numShown match {
      case 0 =>
        if (state.inFors.size === 0) {
          Deal(dealer)
        } else {
          Flop(dealer)
        }
      case 3 => Turn(dealer)
      case 4 => River(dealer)
      case 5 => Payout(dealer)
    }
  }

  def heuristic(game: Poker): PokerState => Map[Player, Real] =
    (state: PokerState) => game.players.map(p => {
      (p, state.outcome(game).map(out => if (out.winner.get === p) Real(1) else Real(-1)).getOrElse(Real(0)))
    }).toMap

  def aiMover(lookahead: Int)(
    implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove],
    evState: State[Poker, PokerState, PokerOutcome, PokerMove]) =
    (state: PokerState, poker: Poker) => {
      val (move, newState, values) = minimax(poker, state, lookahead, heuristic(poker))
      move
    }

  val moveParser = MoveParser()

  def interactiveMove(state: PokerState, game: Poker): PokerMove = {
    // displayEvents()
    val display = game.playerToDisplayer(state.mover)
    display(state.displayTo(state.mover, game))
    userInputStream(display, axle.getLine)
      .flatMap(moveParser.parse(_)(state.mover))
      .find(move => state(move, game).isDefined).get
  }

  def randomMove(state: PokerState, game: Poker): PokerMove = {
    val opens = state.moves(game)
    opens(nextInt(opens.length))
  }

}
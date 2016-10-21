package axle.game

import axle.Show
import spire.algebra.Eq
import spire.implicits.eqOps

trait Outcome[O] {

  def winner(outcome: O): Option[Player]

  // TODO: merge/unify with displayTo of Move
  def displayTo[G, S, M](
    game: G,
    outcome: O,
    player: Player)(
      implicit evGame: Game[G, S, O, M],
      eqp: Eq[Player],
      sp: Show[Player]): String =
    winner(outcome) map { wp =>
      s"${wp.referenceFor(player)} beat " + evGame.players(game).filterNot(_ === wp).map(_.referenceFor(player)).toList.mkString(" and ") + "!"
    } getOrElse ("The game was a draw.")

}

package axle.game

/**
 * Axioms:
 *
 * For all move in moves(s, g), isValid(g, s, move).isRight
 *
 * for all observers, moves(game, maksState(game, state,observer)) should ...
 */

import spire.random.Dist

import axle.stats.ConditionalProbabilityTable0

trait Game[G, S, O, M, MS, MM, V] {

  def probabilityDist: Dist[V]

  def startState(game: G): S

  def startFrom(game: G, state: S): Option[S]

  def players(game: G): IndexedSeq[Player]

  def mover(game: G, state: S): Option[Player]

  // TODO rename to moverMasked?
  def moverM(game: G, state: MS): Option[Player]

  def moves(game: G, state: MS): Seq[M]

  def maskState(game: G, state: S, observer: Player): MS

  def maskMove(game: G, move: M, mover: Player, observer: Player): MM

  def strategyFor(game: G, player: Player): (G, MS) => ConditionalProbabilityTable0[M, V]

  def isValid(game: G, state: MS, move: M): Either[String, M]

  def applyMove(game: G, state: S, move: M): S

  def outcome(game: G, state: S): Option[O]

}


package axle.game

/**
 * Axioms:
 * 
 * For all move in moves(s, g), isValid(g, s, move).isRight
 */

trait Game[G, S, O, M] {

  def startState(game: G): S

  def startFrom(game: G, state: S): Option[S]

  def players(game: G): IndexedSeq[Player]

  def mover(state: S): Option[Player]

  def moves(game: G, state: S): Seq[M]

  def strategyFor(game: G, player: Player): (G, S) => M

  def isValid(game: G, state: S, move: M): Either[String, M]

  def applyMove(game: G, state: S, move: M): S

  def outcome(game: G, state: S): Option[O]

}

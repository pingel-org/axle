
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

  def moves(state: S, game: G): Seq[M]

  def strategyFor(game: G, player: Player): (S, G) => M

  def isValid(game: G, state: S, move: M): Either[String, M]

  def applyMove(state: S, game: G, move: M): S

  def outcome(state: S, game: G): Option[O]

  /**
   * IO related
   */

  def parseMove(game: G, input: String): Either[String, M]

  def displayerFor(game: G, player: Player): String => Unit

  def introMessage(game: G): String

  def displayMoveTo(game: G, mover: Player, move: M, observer: Player): String

  def displayOutcomeTo(game: G, outcome: O, observer: Player): String

  def displayStateTo(state: S, observer: Player, game: G): String

}

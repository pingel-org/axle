
package axle.game

trait Game[G, S, O, M] {

  def introMessage(g: G): String

  def startState(g: G): S

  def startFrom(g: G, s: S): Option[S]

  def players(g: G): IndexedSeq[Player]

  def strategyFor(g: G, player: Player): (S, G) => M

  /**
   *
   * default should be no-op:
   *
   *    (s: String) => {}
   */

  def displayerFor(g: G, player: Player): String => Unit

  def parseMove(g: G, input: String): Either[String, M]

  def isValid(g: G, state: S, move: M): Either[String, M]

  def displayMoveTo(
    game: G,
    mover: Player,
    move: M,
    observer: Player)(
      implicit evGame: Game[G, S, O, M]): String

  def displayOutcomeTo(
    game: G,
    outcome: O,
    observer: Player)(
      implicit evGame: Game[G, S, O, M]): String

  def mover(s: S): Option[Player]

  def applyMove(
    state: S,
    game: G,
    move: M)(
      implicit evGame: Game[G, S, O, M]): S

  def displayTo(state: S, observer: Player, game: G)(
    implicit evGame: Game[G, S, O, M]): String

  def outcome(s: S, game: G): Option[O]

  def moves(s: S, game: G): Seq[M]

}

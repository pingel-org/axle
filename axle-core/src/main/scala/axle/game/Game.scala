
package axle.game

trait Game[G, S, O, M] {

  def introMessage(g: G): String

  def startState(g: G): S

  def startFrom(g: G, s: S): Option[S]

  def players(g: G): IndexedSeq[Player]

  def strategyFor(g: G, player: Player): (S, G) => M

  /**
   *
   * default shoud be no-op:
   *
   *    (s: String) => {}
   */

  def displayerFor(g: G, player: Player): String => Unit

}

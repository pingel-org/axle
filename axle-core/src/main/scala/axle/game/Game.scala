
package axle.game

trait Game[G, S, O, M] {

  def introMessage(g: G): String

  def startState(g: G): S

  def startFrom(g: G, s: S): Option[S]

  def strategyFor(player: Player): (S, Game[G, S, O, M]) => (M, S)

  /**
   *
   * default shoud be no-op:
   *
   *    (s: String) => {}
   */

  def displayerFor(player: Player): String => Unit

}


package axle.game

trait Game[G <: Game[G]] {

  type PLAYER <: Player[G]
  type STATE <: State[G]
  type EVENT <: Event[G]
  type MOVE <: Move[G]
  type OUTCOME <: Outcome[G]

  def players: IndexedSeq[G#PLAYER]

  def introMessage: String

  def startState: G#STATE

  def startFrom(s: G#STATE): Option[G#STATE]
}

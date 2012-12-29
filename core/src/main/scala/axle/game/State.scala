package axle.game

trait State[G <: Game[G]] {

  def player(): G#PLAYER

  def apply(move: G#MOVE): Option[G#STATE]

  def outcome(): Option[G#OUTCOME]

  def moves(): Seq[G#MOVE]

  def displayTo(viewer: G#PLAYER): String

}

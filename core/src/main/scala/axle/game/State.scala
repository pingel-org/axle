package axle.game

trait State[GAME <: Game] {

  def player(): GAME#PLAYER

  def apply(move: GAME#MOVE): Option[GAME#STATE]

  def outcome(): Option[GAME#OUTCOME]

  def moves(): Seq[GAME#MOVE]

  def displayTo(viewer: GAME#PLAYER): String

}

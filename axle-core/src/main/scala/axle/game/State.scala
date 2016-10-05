package axle.game

trait State[G <: Game[G]] {

  def player: G#PLAYER

  def apply(move: G#MOVE, game: G): Option[G#STATE]

  def outcome(game: G): Option[G#OUTCOME]

  def moves(game: G): Seq[G#MOVE]

  def displayTo(viewer: G#PLAYER, game: G): String

  def eventQueues: Map[G#PLAYER, List[Event[G]]]

  def setEventQueues(qs: Map[G#PLAYER, List[Event[G]]]): G#STATE

}

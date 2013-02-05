package axle.game

trait State[G <: Game[G]] {

  def player(): G#PLAYER

  def apply(move: G#MOVE): Option[G#STATE]

  def outcome(): Option[G#OUTCOME]

  def moves(): Seq[G#MOVE]

  def displayTo(viewer: G#PLAYER): String

  def eventQueues: Map[G#PLAYER, List[Event[G]]]

  def setEventQueues(qs: Map[G#PLAYER, List[Event[G]]]): G#STATE

  def displayEvents(): G#STATE = {
    val qs = eventQueues
    player.displayEvents(qs.get(player).getOrElse(Nil))
    setEventQueues(qs + (player -> Nil))
  }

  def broadcast[E <: Event[G]](move: E): G#STATE =
    setEventQueues(eventQueues.map({ case (k, v) => (k, v ++ List(move)) }))

}

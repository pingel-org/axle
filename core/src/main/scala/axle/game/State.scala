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

  def broadcast[E <: Event[G]](players: Set[G#PLAYER], move: E): G#STATE =
    setEventQueues(players.map(p => {
      (p -> (eventQueues.get(p).getOrElse(Nil) ++ List(move)))
    }).toMap)

}

package axle.game

trait State[G <: Game[G]] {

  def player: G#PLAYER

  def apply(move: G#MOVE, game: G): Option[G#STATE]

  def outcome(game: G): Option[G#OUTCOME]

  def moves(game: G): Seq[G#MOVE]

  def displayTo(viewer: G#PLAYER): String

  def eventQueues: Map[G#PLAYER, List[Event[G]]]

  def setEventQueues(qs: Map[G#PLAYER, List[Event[G]]]): G#STATE

  def displayEvents(players: Set[G#PLAYER], game: G): G#STATE = {
    val qs = eventQueues
    players.map(p => p.displayEvents(qs.get(p).getOrElse(Nil), game))
    setEventQueues(qs ++ players.map(p => (p -> Nil)))
  }

  def broadcast[E <: Event[G]](players: Set[G#PLAYER], event: E): G#STATE = {
    val qs = eventQueues
    setEventQueues(players.map(p => {
      (p -> (qs.get(p).getOrElse(Nil) ++ List(event)))
    }).toMap)
  }
}

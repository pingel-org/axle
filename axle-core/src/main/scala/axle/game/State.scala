package axle.game

trait State[G, S, O, M] {

  def mover(s: S): Player

  def applyMove(
    state: S,
    move: M,
    game: G)(
      implicit evGame: Game[G, S, O, M]): Option[S]

  def displayTo(viewer: Player)(
    implicit evGame: Game[G, S, O, M]): String

  // previously had game as argument
  def outcome(s: S): Option[O]

  // previously had game as argument
  def moves(s: S): Seq[M]

  def eventQueues(s: S): Map[Player, List[Either[O, M]]]

  def setEventQueues(s: S, qs: Map[Player, List[Either[O, M]]]): S

}

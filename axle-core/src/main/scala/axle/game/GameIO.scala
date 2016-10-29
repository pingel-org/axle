package axle.game

trait GameIO[G, S, O, M] {

  def parseMove(game: G, input: String): Either[String, M]

  def displayerFor(game: G, player: Player): String => Unit

  def introMessage(game: G): String

  def displayMoveTo(game: G, move: M, mover: Player, observer: Player): String

  def displayOutcomeTo(game: G, outcome: O, observer: Player): String

  def displayStateTo(game: G, state: S, observer: Player): String

}
package axle.game

trait GameIO[G, O, M, MS, MM] {

  def parseMove(game: G, input: String): Either[String, M]

  def introMessage(game: G): String

  def displayMoveTo(game: G, move: MM, mover: Player, observer: Player): String

  def displayOutcomeTo(game: G, outcome: O, observer: Player): String

  def displayStateTo(game: G, state: MS, observer: Player): String

}

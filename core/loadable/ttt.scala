
object tttO {

  import axle.game._
  import ttt._

  val game = new TicTacToe(3)

  // two interactive players:

  val x = game.player("X", "Player X", true)
  val o = game.player("O", "Player O", false)

  val start = game.state(x, game.startBoard())

  game.play(start)

}

object tttO {

  import axle.game.ttt._

  val game = TicTacToe(3)

  val x = game.player("X", "Player X", interactive = true)
  val o = game.player("O", "Player O", interactive = false)

  val start = game.state(x, game.startBoard())

  game.play(start)

}
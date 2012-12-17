object TicTacToeDemo {

  println("Tic Tac Toe Demo")

  import axle.game.ttt._

  val game = TicTacToe(3, "human", "ai")
  import game.{x, o}

  val start = game.state(x, game.startBoard())
  // val start = game.startState()

  game.play(start)
 
}
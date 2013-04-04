object TicTacToeDemo {

  println("Tic Tac Toe Demo")

  import axle.game.ttt._

  val game = TicTacToe(3, "human", "ai")
  val start = game.startState()
  game.play(start)
 
}
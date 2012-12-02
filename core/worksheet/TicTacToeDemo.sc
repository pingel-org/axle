object TicTacToeDemo {

  println("Tic Tac Toe Demo")                     //> Tic Tac Toe Demo

  import axle.game.ttt._

  val game = TicTacToe(3)                         //> game  : axle.game.ttt.TicTacToe = TicTacToe(3)

  val x = game.player("X", "Player X", interactive = false)
                                                  //> x  : TicTacToeDemo.game.TicTacToePlayer = Player X
  val o = game.player("O", "Player O", interactive = false)
                                                  //> o  : TicTacToeDemo.game.TicTacToePlayer = Player O

  val start = game.state(x, game.startBoard())    //> start  : TicTacToeDemo.game.TicTacToeState = Board:         Movement Key:
                                                  //|  | |           1|2|3
                                                  //|  | |           4|5|6
                                                  //|  | |           7|8|9


  game.play(start)                                //> res0: Option[TicTacToeDemo.game.OUTCOME] = ()
  
}
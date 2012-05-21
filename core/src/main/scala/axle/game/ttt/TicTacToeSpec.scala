
package axle.game

object TicTacToeSpec {

  import ttt._
  
  val game = TicTacToe(3, false)
  val x = InteractiveTicTacToePlayer(game, "X", "Player X")
  val o = InteractiveTicTacToePlayer(game, "O", "Player O")

  def testTTT(moves: List[Int], winner: Option[Player]): Unit = {

    // the game wasn't designed to support multiple rounds,
    // but this happens to work because we're not notifying
    // the players and setting game.state is enough to clear the
    // memory

    game.addPlayer(x)
    game.addPlayer(o)
    game.state = TicTacToeState(game, x)

    println
    println("moves: %s\n".format(moves))
    moves.reverse

    val outcomes = moves.map( move => game.state.applyMove(TicTacToeMove(game, game.state.player, move)).isDefined)

    println("game state:\n%s".format(game.state))
    println("winner: %s, expected winner: %s".format(outcome.winner, winner))
    assert(winner == outcome.winner)
  }

  testTTT(List(1, 2, 3, 4, 5, 6, 7), Some(x))
  testTTT(List(2, 3, 4, 5, 6, 7, 8), Some(o))
  testTTT(List(1, 2, 3, 4, 5, 7, 8, 9, 6), None)

  // To run the game interactively:
  // val ttt = TicTacToe(4)
  // ttt.play()

}

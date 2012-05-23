
package axle.game

object TicTacToeSpec {

  import ttt._

  val x = InteractiveTicTacToePlayer("X", "Player X")
  val o = InteractiveTicTacToePlayer("O", "Player O")
  val game = TicTacToe(3, x, o)

  def testTTT(moves: List[Int], winner: Option[Player[TicTacToe]]): Unit = {

    // the game wasn't designed to support multiple rounds,
    // but this happens to work because we're not notifying
    // the players and setting game.state is enough to clear the
    // memory

    val start = new TicTacToeState(x, game.startBoard)

    println
    println("moves: %s\n".format(moves))

    var state = start
    moves.map(move => { state = state.applyMove(TicTacToeMove(state.player, move, game)) })
    val outcomeOpt = state.getOutcome

    println("game state:")
    println(state)
    println("winner: %s, expected winner: %s".format(outcomeOpt.get.winner, winner))
    assert(winner == outcomeOpt.get.winner)
  }

  testTTT(List(1, 2, 3, 4, 5, 6, 7), Some(x))
  testTTT(List(2, 3, 4, 5, 6, 7, 8), Some(o))
  testTTT(List(1, 2, 3, 4, 5, 7, 8, 9, 6), None)

  // To run the game interactively:
  // val ttt = TicTacToe(4)
  // ttt.play()

}

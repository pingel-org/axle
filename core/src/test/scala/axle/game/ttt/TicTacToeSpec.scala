
package axle.game

object TicTacToeSpec {

  import ttt._

  val x = InteractiveTicTacToePlayer("X", "Player X")
  val o = InteractiveTicTacToePlayer("O", "Player O")
  val game = TicTacToe(3, x, o)

  def testTTT(moves: List[(Player[TicTacToe], Int)], winner: Option[Player[TicTacToe]]): Unit = {

    // the game wasn't designed to support multiple rounds,
    // but this happens to work because we're not notifying
    // the players and setting game.state is enough to clear the
    // memory

    val start = new TicTacToeState(x, game.startBoard, game).asInstanceOf[State[TicTacToe#G]]

    println
    println("moves: %s\n".format(moves))

    val lastMoveState = game.scriptedMoveStateStream(start, moves.toStream.map(pp => TicTacToeMove(pp._1, pp._2))).last
    println("game state:")
    println(lastMoveState._2)

    val outcome = lastMoveState._2.getOutcome
    println("winner: %s, expected winner: %s".format(outcome.map(_.winner), winner))
    assert(winner == outcome.map(_.winner))
  }

  testTTT(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 6), (x, 7)), Some(x))
  testTTT(List((x, 2), (o, 3), (x, 4), (o, 5), (x, 6), (o, 7), (x, 8)), Some(o))
  testTTT(List((x, 1), (o, 2), (x, 3), (o, 4), (x, 5), (o, 7), (x, 8), (o, 9), (x, 6)), None)

  // To run the game interactively:
  // val ttt = TicTacToe(4)
  // ttt.play()

}

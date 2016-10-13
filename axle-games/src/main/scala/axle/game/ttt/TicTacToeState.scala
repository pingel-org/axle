package axle.game.ttt

import axle.string
import axle.game._
import spire.implicits._

case class TicTacToeState(
    player: Player,
    board: Array[Option[Player]],
    boardSize: Int,
    _eventQueues: Map[Player, List[Either[TicTacToeOutcome, TicTacToeMove]]] = Map()) {

  val numPositions = board.length

  def row(r: Int) = (0 until boardSize) map { c => playerAt(r, c) }

  def column(c: Int) = (0 until boardSize) map { r => playerAt(r, c) }

  def playerAt(r: Int, c: Int): Option[Player] = board(c + r * boardSize)

  def playerAt(i: Int) = board(i - 1)

  def place(position: Int, player: Option[Player]): Array[Option[Player]] = {
    val updated = board.clone()
    updated.update(position - 1, player)
    updated
  }

  def displayTo(viewer: Player, game: TicTacToe): String = {

    val keyWidth = string(numPositions).length

    "Board:         Movement Key:\n" +
      0.until(boardSize).map(r => {
        row(r).map(playerOpt => playerOpt.map(string(_)).getOrElse(" ")).mkString("|") +
          "          " +
          (1 + r * boardSize).until(1 + (r + 1) * boardSize).mkString("|") // TODO rjust(keyWidth)
      }).mkString("\n")

  }

  def apply(position: Int): Option[Player] = playerAt(position)

  def hasWonRow(player: Player): Boolean =
    (0 until boardSize).exists(row(_).toList.forall(_ == Some(player)))

  def hasWonColumn(player: Player): Boolean =
    (0 until boardSize).exists(column(_).toList.forall(_ == Some(player)))

  def hasWonDiagonal(player: Player): Boolean =
    (0 until boardSize).forall(i => playerAt(i, i) == Some(player)) ||
      (0 until boardSize).forall(i => playerAt(i, (boardSize - 1) - i) == Some(player))

  def hasWon(player: Player): Boolean = hasWonRow(player) || hasWonColumn(player) || hasWonDiagonal(player)

  def openPositions(ttt: TicTacToe): IndexedSeq[Int] = (1 to numPositions).filter(this(_).isEmpty)

  def moves(ttt: TicTacToe): Seq[TicTacToeMove] = openPositions(ttt).map(TicTacToeMove(player, _, boardSize))

  def outcome(ttt: TicTacToe): Option[TicTacToeOutcome] = {
    val winner = ttt.players.find(hasWon)
    if (winner.isDefined) {
      Some(TicTacToeOutcome(winner))
    } else if (openPositions(ttt).length === 0) {
      Some(TicTacToeOutcome(None))
    } else {
      None
    }
  }

  def apply(move: TicTacToeMove, ttt: TicTacToe)(implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): Option[TicTacToeState] =
    ttt.state(
      ttt.playerAfter(move.player),
      place(move.position, Some(player)),
      _eventQueues)

  def eventQueues: Map[Player, List[Either[TicTacToeOutcome, TicTacToeMove]]] = _eventQueues

  def setEventQueues(qs: Map[Player, List[Either[TicTacToeOutcome, TicTacToeMove]]]): TicTacToeState =
    TicTacToeState(player, board, boardSize, qs)

}

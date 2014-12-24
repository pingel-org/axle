package axle.game.ttt

import axle.string
import axle.game._
import axle.algebra._
import spire.implicits._

case class TicTacToeState(
  player: TicTacToePlayer,
  board: Array[Option[TicTacToePlayer]],
  _eventQueues: Map[TicTacToePlayer, List[Event[TicTacToe]]] = Map())(implicit ttt: TicTacToe)
  extends State[TicTacToe]() {

  val numPositions = board.length

  def row(r: Int) = (0 until ttt.boardSize) map { c => playerAt(r, c) }

  def column(c: Int) = (0 until ttt.boardSize) map { r => playerAt(r, c) }

  def playerAt(r: Int, c: Int): Option[TicTacToePlayer] = board(c + r * ttt.boardSize)

  def playerAt(i: Int) = board(i - 1)

  def place(position: Int, player: Option[TicTacToePlayer]): Array[Option[TicTacToePlayer]] = {
    val updated = board.clone()
    updated.update(position - 1, player)
    updated
  }

  def displayTo(viewer: TicTacToePlayer): String = {

    val keyWidth = string(numPositions).length

    "Board:         Movement Key:\n" +
      0.until(ttt.boardSize).map(r => {
        row(r).map(playerOpt => playerOpt.map(string(_)).getOrElse(" ")).mkString("|") +
          "          " +
          (1 + r * ttt.boardSize).until(1 + (r + 1) * ttt.boardSize).mkString("|") // TODO rjust(keyWidth)
      }).mkString("\n")

  }

  def apply(position: Int): Option[TicTacToePlayer] = playerAt(position)

  def hasWonRow(player: TicTacToePlayer): Boolean =
    (0 until ttt.boardSize).exists(row(_).toList.forall(_ === Some(player)))

  def hasWonColumn(player: TicTacToePlayer): Boolean =
    (0 until ttt.boardSize).exists(column(_).toList.forall(_ === Some(player)))

  def hasWonDiagonal(player: TicTacToePlayer): Boolean =
    (0 until ttt.boardSize).forall(i => playerAt(i, i) === Some(player)) ||
      (0 until ttt.boardSize).forall(i => playerAt(i, (ttt.boardSize - 1) - i) === Some(player))

  def hasWon(player: TicTacToePlayer): Boolean = hasWonRow(player) || hasWonColumn(player) || hasWonDiagonal(player)

  def openPositions: IndexedSeq[Int] = (1 to numPositions).filter(this(_).isEmpty)

  def moves: Seq[TicTacToeMove] = openPositions.map(TicTacToeMove(player, _))

  def outcome: Option[TicTacToeOutcome] = {
    val winner = ttt.players.find(hasWon)
    if (winner.isDefined) {
      Some(TicTacToeOutcome(winner))
    } else if (openPositions.length === 0) {
      Some(TicTacToeOutcome(None))
    } else {
      None
    }
  }

  def apply(move: TicTacToeMove): Option[TicTacToeState] =
    ttt.state(
      ttt.playerAfter(move.player),
      place(move.position, Some(player)),
      _eventQueues)

  def eventQueues: Map[TicTacToePlayer, List[Event[TicTacToe]]] = _eventQueues

  def setEventQueues(qs: Map[TicTacToePlayer, List[Event[TicTacToe]]]): TicTacToeState =
    TicTacToeState(player, board, qs)

}


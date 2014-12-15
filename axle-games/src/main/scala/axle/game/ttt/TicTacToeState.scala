package axle.game.ttt

import axle.string
import axle.game._
import axle.algebra._
import spire.implicits._

case class TicTacToeBoard(
  size: Int,
  positions: Array[Option[TicTacToePlayer]]) {

  // assumes r and c are numbered from 0 to size-1

  def indexOf(r: Int, c: Int): Int = (r * c) + r

  def row(r: Int) = (0 until size) map { c => playerAt(r, c) }

  def column(c: Int) = (0 until size) map { r => playerAt(r, c) }

  def playerAt(r: Int, c: Int): Option[TicTacToePlayer] = positions(indexOf(r, c))

  def playerAt(i: Int) = positions(i)

  def place(r: Int, c: Int, player: Option[TicTacToePlayer]): TicTacToeBoard = {
    val i = indexOf(r, c)
    val updated = positions.clone()
    updated.update(i, player)
    TicTacToeBoard(size, updated)
  }

  def length = size * size
}

case class TicTacToeState(
  player: TicTacToePlayer,
  board: TicTacToeBoard,
  _eventQueues: Map[TicTacToePlayer, List[Event[TicTacToe]]] = Map())(implicit ttt: TicTacToe)
  extends State[TicTacToe]() {

  val numPositions = board.length

  def displayTo(viewer: TicTacToePlayer): String = {

    val keyWidth = string(numPositions).length

    "Board:         Movement Key:\n" +
      0.until(board.size).map(r => {
        val rowlist = board.row(r).toList
        rowlist.map(_.getOrElse(" ")).mkString("|") +
          "          " +
          (1 + r * board.size).until(1 + (r + 1) * board.size).mkString("|") // TODO rjust(keyWidth)
      }).mkString("\n")

  }

  def positionToRow(position: Int): Int = (position - 1) / board.size

  def positionToColumn(position: Int): Int = (position - 1) % board.size

  def apply(position: Int): Option[TicTacToePlayer] = board.playerAt(position)

  // The validation in InteractiveTicTacToePlayer.chooseMove might be better placed here
  //    def updat(position: Int, player: TicTacToePlayer) =
  //      board(positionToRow(position), positionToColumn(position)) = Some(player.id)

  def hasWonRow(player: TicTacToePlayer): Boolean =
    (0 until board.size).exists(board.row(_).toList.forall(_ === Some(player)))

  def hasWonColumn(player: TicTacToePlayer): Boolean =
    (0 until board.size).exists(board.column(_).toList.forall(_ === Some(player)))

  def hasWonDiagonal(player: TicTacToePlayer): Boolean =
    (0 until board.size).forall(i => board.playerAt(i, i) === Some(player)) ||
      (0 until board.size).forall(i => board.playerAt(i, (board.size - 1) - i) === Some(player))

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
      ttt.playerAfter(move.tttPlayer),
      board.place(positionToRow(move.position), positionToColumn(move.position), Some(player)),
      _eventQueues)

  def eventQueues: Map[TicTacToePlayer, List[Event[TicTacToe]]] = _eventQueues

  def setEventQueues(qs: Map[TicTacToePlayer, List[Event[TicTacToe]]]): TicTacToeState =
    TicTacToeState(player, board, qs)

}


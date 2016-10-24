package axle.game.ttt

import axle.string
import axle.game._
import spire.implicits._

case class TicTacToeState(
    playerOptFn: (TicTacToeState) => Option[Player],
    board: Array[Option[Player]],
    boardSize: Int) {

  val numPositions = board.length

  val moverOpt = playerOptFn(this)

  def row(r: Int) = (0 until boardSize) map { c => playerAt(r, c) }

  def column(c: Int) = (0 until boardSize) map { r => playerAt(r, c) }

  def playerAt(r: Int, c: Int): Option[Player] = board(c + r * boardSize)

  def playerAt(i: Int) = board(i - 1)

  def place(position: Int, player: Player): Array[Option[Player]] = {
    val updated = board.clone()
    updated.update(position - 1, Some(player))
    updated
  }

  def displayTo(viewer: Player, game: TicTacToe): String = {

    val keyWidth = string(numPositions).length

    "Board:         Movement Key:\n" +
      0.until(boardSize).map(r => {
        row(r).map(playerOpt => playerOpt.map(game.markFor).getOrElse(" ")).mkString("|") +
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

}

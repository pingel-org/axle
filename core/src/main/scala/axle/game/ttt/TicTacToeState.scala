
package axle.game.ttt

import axle.game._

import axle.matrix.ArrayMatrixFactory._

case class TicTacToeState(player: Player[TicTacToe], board: ArrayMatrix[Option[String]], game: TicTacToe)
  extends State[TicTacToe] {

  val boardSize = board.columns
  val numPositions = board.length

  override def toString(): String = {

    val keyWidth = numPositions.toString().length

    "Board:         Movement Key:\n" +
      0.until(boardSize).map(r => {
        val rowlist = board.row(r).toList
        rowlist.map(_.getOrElse(" ")).mkString("|") +
          "          " +
          (1 + r * boardSize).until(1 + (r + 1) * boardSize).mkString("|") // TODO rjust(keyWidth)
      }).mkString("\n")

  }

  def positionToRow(position: Int) = (position - 1) / boardSize

  def positionToColumn(position: Int) = (position - 1) % boardSize

  def getBoardAt(position: Int) = board(positionToRow(position), positionToColumn(position))

  // The validation in InteractiveTicTacToePlayer.chooseMove might be better placed here
  def setBoardAt(position: Int, player: Player[TicTacToe]) =
    board(positionToRow(position), positionToColumn(position)) = Some(player.id)

  def hasWonRow(player: Player[TicTacToe]) = 0.until(boardSize).exists(board.row(_).toList.forall(_ == Some(player.id)))

  def hasWonColumn(player: Player[TicTacToe]) = 0.until(boardSize).exists(board.column(_).toList.forall(_ == Some(player.id)))

  def hasWonDiagonal(player: Player[TicTacToe]) =
    (0 until boardSize).forall(i => board(i, i) == Some(player.id)) ||
      (0 until boardSize).forall(i => board(i, (boardSize - 1) - i) == Some(player.id))

  def hasWon(player: Player[TicTacToe]) = hasWonRow(player) || hasWonColumn(player) || hasWonDiagonal(player)

  def openPositions() = 1.to(numPositions).filter(getBoardAt(_).isEmpty)

  def isTerminal(): Boolean = openPositions().length == 0

  def getOutcome(): Option[Outcome[TicTacToe]] = {
    for (player <- game.players.values) {
      val tttp = player.asInstanceOf[Player[TicTacToe]] // TODO remove cast
      if (hasWon(tttp)) {
        return Some(Outcome[TicTacToe](game, Some(tttp)))
      }
    }

    if (isTerminal) {
      return Some(Outcome[TicTacToe](game, None))
    }

    None
  }

  def applyMove(move: Move[TicTacToe]): TicTacToeState = {
    val tttMove = move.asInstanceOf[TicTacToeMove] // TODO: remove cast
    val resultBoard = board.dup
    resultBoard(positionToRow(tttMove.position), positionToColumn(tttMove.position)) = Some(player.id)
    TicTacToeState(game.playerAfter(tttMove.player), resultBoard, game)
  }

}

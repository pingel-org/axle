
package axle.game.ttt

import axle.game._

import axle.matrix.ArrayMatrixFactory._

case class TicTacToeState(player: Player[TicTacToe], board: ArrayMatrix[Option[String]])
  extends State[TicTacToe] {

  val game: TicTacToe = null // TODO

  override def toString(): String = {

    // def none2space(c: Option[String]) = c.getOrElse(" ")

    val keyWidth = game.numPositions.toString().length

    "Board:         Movement Key:\n" +
      0.until(game.boardSize).map(r => {
        board.row(r).toList.mkString("|") +
          "          " +
          (1 + r * game.boardSize).to(1 + (r + 1) * game.boardSize).mkString("|") // TODO rjust(keyWidth)
      }).mkString("\n")

  }

  def positionToRow(position: Int) = (position - 1) / game.boardSize

  def positionToColumn(position: Int) = (position - 1) % game.boardSize

  def getBoardAt(position: Int) = board(positionToRow(position), positionToColumn(position))

  // The validation in InteractiveTicTacToePlayer.chooseMove might be better placed here
  def setBoardAt(position: Int, player: Player[TicTacToe]) =
    board(positionToRow(position), positionToColumn(position)) = Some(player.id)

  def hasWonRow(player: Player[TicTacToe]) = 0.until(game.boardSize).exists(board.row(_).toList.forall(_ == player.id))

  def hasWonColumn(player: Player[TicTacToe]) = 0.until(game.boardSize).exists(board.column(_).toList.forall(_ == player.id))

  def hasWonDiagonal(player: Player[TicTacToe]) = {
    val indexes = 0 until game.boardSize
    indexes.forall(i => board(i, i) == player.id) || indexes.forall(i => board(i, (game.boardSize - 1) - i) == player.id)
  }

  def hasWon(player: Player[TicTacToe]) = hasWonRow(player) || hasWonColumn(player) || hasWonDiagonal(player)

  def openPositions() = 1.to(game.numPositions).filter(getBoardAt(_).isEmpty)

  def getOutcome(): Option[Outcome[TicTacToe]] = {
    for (player <- game.players.values) {
      if (hasWon(player)) {
        return Some(Outcome[TicTacToe](game, Some(player)))
      }
    }

    if (openPositions().length == 0) {
      return Some(Outcome[TicTacToe](game, None))
    }

    None
  }

  def applyMove(move: TicTacToeMove): TicTacToeState = {
    val resultBoard = board.dup
    resultBoard(positionToRow(move.position), positionToColumn(move.position)) = Some(player.id)
    TicTacToeState(game.playerAfter(move.player), resultBoard)
  }

}

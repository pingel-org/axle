
package axle.game.ttt

import axle.game._

import axle.matrix.ArrayMatrixFactory._

case class TicTacToeState(game: TicTacToe, var player: TicTacToePlayer)
  extends State(game, player) {

  val board = matrix[String](game.boardSize, game.boardSize, " ")

  override def toString(): String = {

    def none2space(c: Option[String]) = c.getOrElse(" ")

    val keyWidth = game.numPositions.toString().length

    "Board:         Movement Key:\n" +
      0.until(game.boardSize).map(r => {
        board.row(r).map(none2space(_)).mkString("|") +
          "          " +
          (1 + r * game.boardSize).to(1 + (r + 1) * game.boardSize).mkString("|") // TODO rjust(keyWidth)
      }).mkString("\n")

  }

  def positionToRow(position: Int) = (position - 1) / game.boardSize

  def positionToColumn(position: Int) = (position - 1) % game.boardSize

  def getBoardAt(position: Int) = board(positionToRow(position), positionToColumn(position))

  // The validation in InteractiveTicTacToePlayer.chooseMove might be better placed here
  def setBoardAt(position: Int, player: Player) =
    board(positionToRow(position), positionToColumn(position)) = player.id

  def hasWonRow(player: TicTacToePlayer) = boardRows.exists(_.forall(_ == player.id))

  def hasWonColumn(player: TicTacToePlayer) = boardColumns.exists(_.forall(_ == player.id))

  def hasWonDiagonal(player: TicTacToePlayer) = {
    val indexes = 0 until game.boardSize
    indexes.forall(i => board(i, i) == player.id) || indexes.forall(i => board(i, (game.boardSize - 1) - i) == player.id)
  }

  def hasWon(player: TicTacToePlayer) = hasWonRow(player) || hasWonColumn(player) || hasWonDiagonal(player)

  def openPositions() = 1.to(game.numPositions).flatMap(getBoardAt(_))

  def applyMove(move: TicTacToeMove): Option[Outcome] = {

    setBoardAt(move.position, move.tttPlayer)
    player = game.playerAfter(move.tttPlayer)

    for (player <- game.players.values) {
      if (hasWon(player)) {
        return Some(Outcome(game, Some(player)))
      }
    }

    if (openPositions().length == 0) {
      return Some(Outcome(game, None))
    }

    None
  }

}

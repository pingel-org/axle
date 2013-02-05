package axle.game.ttt

import axle.game._
import axle.matrix.ArrayMatrixFactory._

case class TicTacToeState(
  player: TicTacToePlayer,
  board: Matrix[Option[String]],
  _eventQueues: Map[TicTacToePlayer, List[Event[TicTacToe]]] = Map())(implicit ttt: TicTacToe)
  extends State[TicTacToe]() {

  val boardSize = board.columns
  val numPositions = board.length

  def displayTo(viewer: TicTacToePlayer): String = {

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

  def apply(position: Int) = board(positionToRow(position), positionToColumn(position))

  // The validation in InteractiveTicTacToePlayer.chooseMove might be better placed here
  //    def updat(position: Int, player: TicTacToePlayer) =
  //      board(positionToRow(position), positionToColumn(position)) = Some(player.id)

  def hasWonRow(player: TicTacToePlayer) =
    (0 until boardSize).exists(board.row(_).toList.forall(_ equals Some(player.id)))

  def hasWonColumn(player: TicTacToePlayer) =
    (0 until boardSize).exists(board.column(_).toList.forall(_ equals Some(player.id)))

  def hasWonDiagonal(player: TicTacToePlayer) =
    (0 until boardSize).forall(i => board(i, i) equals Some(player.id)) ||
      (0 until boardSize).forall(i => board(i, (boardSize - 1) - i) equals Some(player.id))

  def hasWon(player: TicTacToePlayer) = hasWonRow(player) || hasWonColumn(player) || hasWonDiagonal(player)

  def openPositions() = (1 to numPositions).filter(this(_).isEmpty)

  def moves(): Seq[TicTacToeMove] = openPositions().map(TicTacToeMove(player, _))

  def outcome(): Option[TicTacToeOutcome] = {
    val winner = ttt.players.find(hasWon(_))
    if (winner.isDefined) {
      Some(TicTacToeOutcome(winner))
    } else if (openPositions().length == 0) {
      Some(TicTacToeOutcome(None))
    } else {
      None
    }
  }

  def apply(move: TicTacToeMove): Option[TicTacToeState] =
    ttt.state(ttt.playerAfter(move.tttPlayer), board.addAssignment(positionToRow(move.position), positionToColumn(move.position), Some(player.id)))

  def eventQueues() = _eventQueues

  def setEventQueues(qs: Map[TicTacToePlayer, List[Event[TicTacToe]]]): TicTacToeState =
    TicTacToeState(player, board, qs)

}

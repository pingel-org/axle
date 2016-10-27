
package axle.game.ttt

import axle.game._
import spire.implicits._

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(
    boardSize: Int = 3,
    x: Player,
    xStrategy: (TicTacToeState, TicTacToe) => TicTacToeMove,
    xDisplayer: String => Unit,
    o: Player,
    oStrategy: (TicTacToeState, TicTacToe) => TicTacToeMove,
    oDisplayer: String => Unit) {

  val players = Vector(x, o)

  val playerToDisplayer = Map(x -> xDisplayer, o -> oDisplayer)

  val playerToStrategy = Map(x -> xStrategy, o -> oStrategy)

  def numPositions: Int = boardSize * boardSize

  def startBoard: Array[Option[Player]] =
    (0 until (boardSize * boardSize)).map(i => None).toArray

  def playerAfter(player: Player): Player =
    if (player === x) o else x

  def markFor(player: Player): Char =
    if (player === x) 'X' else 'O'

}

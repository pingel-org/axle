
package axle.game.ttt

import cats.implicits._

import axle.game._

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(
  boardSize:  Int = 3,
  x:          Player,
  o:          Player) {

  val players = Vector(x, o)

  def numPositions: Int = boardSize * boardSize

  def startBoard: Array[Option[Player]] =
    (0 until (boardSize * boardSize)).map(i => None).toArray

  def playerAfter(player: Player): Player =
    if (player === x) o else x

  def markFor(player: Player): Char =
    if (player === x) 'X' else 'O'

}

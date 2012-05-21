
package axle.game.ttt

import axle.game._

case class TicTacToePlayer(game: TicTacToe, playerId: String, description: String)
  extends Player(game, playerId, description) {

}


package axle.game

abstract class Move[GAME <: Game](player: Player[GAME], game: GAME)
  extends Event {

}

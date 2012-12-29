package axle.game

abstract class Move[GAME <: Game](player: GAME#PLAYER)
  extends Event[GAME] {

}

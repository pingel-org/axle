package axle.game

abstract class Move[G <: Game[G]](player: G#PLAYER)
  extends Event[G]

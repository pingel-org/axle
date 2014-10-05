package axle.game

abstract class Move[G <: Game[G]](val player: G#PLAYER)
  extends Event[G]

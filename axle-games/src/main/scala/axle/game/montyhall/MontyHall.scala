package axle.game.montyhall

import axle.game._

case class MontyHall(
  contestant: Player,
  contestantStrategy: (MontyHall, MontyHallState) => MontyHallMove,
  contestantDisplayer: String => Unit,
  monty: Player,
  montyStrategy: (MontyHall, MontyHallState) => MontyHallMove,
  montyDisplayer: String => Unit)

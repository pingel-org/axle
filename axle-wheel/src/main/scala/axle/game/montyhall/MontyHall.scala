package axle.game.montyhall

import axle.game._
import axle.probability.ConditionalProbabilityTable
import spire.math.Rational

case class MontyHall(
  contestant:          Player,
  contestantStrategy:  (MontyHall, MontyHallState) => ConditionalProbabilityTable[MontyHallMove, Rational],
  contestantDisplayer: String => Unit,
  monty:               Player,
  montyStrategy:       (MontyHall, MontyHallState) => ConditionalProbabilityTable[MontyHallMove, Rational],
  montyDisplayer:      String => Unit)

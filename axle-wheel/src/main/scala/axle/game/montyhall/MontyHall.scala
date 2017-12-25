package axle.game.montyhall

import axle.game._
import axle.stats.ConditionalProbabilityTable0
import spire.math.Rational

case class MontyHall(
  contestant:          Player,
  contestantStrategy:  (MontyHall, MontyHallState) => ConditionalProbabilityTable0[MontyHallMove, Rational],
  contestantDisplayer: String => Unit,
  monty:               Player,
  montyStrategy:       (MontyHall, MontyHallState) => ConditionalProbabilityTable0[MontyHallMove, Rational],
  montyDisplayer:      String => Unit)

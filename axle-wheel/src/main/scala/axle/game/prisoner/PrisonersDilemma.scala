package axle.game.prisoner

import axle.game._
import axle.stats.ConditionalProbabilityTable0
import spire.math.Rational

case class PrisonersDilemma(
  p1: Player,
  p1Strategy: (PrisonersDilemma, PrisonersDilemmaState) => ConditionalProbabilityTable0[PrisonersDilemmaMove, Rational],
  p1Displayer: String => Unit,
  p2: Player,
  p2Strategy: (PrisonersDilemma, PrisonersDilemmaState) => ConditionalProbabilityTable0[PrisonersDilemmaMove, Rational],
  p2Displayer: String => Unit)

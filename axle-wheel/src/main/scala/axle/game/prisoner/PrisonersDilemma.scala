package axle.game.prisoner

import spire.math.Rational
import axle.game._
import axle.probability.ConditionalProbabilityTable

case class PrisonersDilemma(
  p1:          Player,
  p1Strategy:  (PrisonersDilemma, PrisonersDilemmaState) => ConditionalProbabilityTable[PrisonersDilemmaMove, Rational],
  p1Displayer: String => Unit,
  p2:          Player,
  p2Strategy:  (PrisonersDilemma, PrisonersDilemmaState) => ConditionalProbabilityTable[PrisonersDilemmaMove, Rational],
  p2Displayer: String => Unit)

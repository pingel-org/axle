package axle.game.prisoner

import axle.game._

case class PrisonersDilemma(
  p1: Player,
  p1Strategy: (PrisonersDilemma, PrisonersDilemmaState) => PrisonersDilemmaMove,
  p1Displayer: String => Unit,
  p2: Player,
  p2Strategy: (PrisonersDilemma, PrisonersDilemmaState) => PrisonersDilemmaMove,
  p2Displayer: String => Unit)

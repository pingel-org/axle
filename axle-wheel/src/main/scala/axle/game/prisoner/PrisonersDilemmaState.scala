package axle.game.prisoner

case class PrisonersDilemmaState(
  p1Move:  Option[PrisonersDilemmaMove],
  p1Moved: Boolean,
  p2Move:  Option[PrisonersDilemmaMove])

package axle.game.prisoner

trait PrisonersDilemmaMove {

  def description: String
}

case class Silence() extends PrisonersDilemmaMove {

  def description = "silence"
}

case class Betrayal() extends PrisonersDilemmaMove {

  def description = "betrayal"
}



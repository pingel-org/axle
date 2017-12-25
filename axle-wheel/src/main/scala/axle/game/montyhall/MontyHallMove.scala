package axle.game.montyhall

sealed trait MontyHallMove {

  def description: String
}

case class PlaceCar(door: Int) extends MontyHallMove {

  def description = s"car placed"
}

case class FirstChoice(door: Int) extends MontyHallMove {

  def description = s"door #${door}"
}

case class Reveal(door: Int) extends MontyHallMove {

  def description = s"reveal goat behind door #${door}"
}

case class Change() extends MontyHallMove {

  def description = "change doors"
}

case class Stay() extends MontyHallMove {

  def description = "stay with original choice"
}


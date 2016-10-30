package axle.game.montyhall

case class MontyHallState(
  placement: Option[PlaceCar],
  firstChoice: Option[FirstChoice],
  reveal: Option[Reveal],
  secondChoice: Option[Either[Change, Stay]])

package axle.game.montyhall

case class MontyHallState(
  placement:    Option[PlaceCar],
  carPlaced:    Boolean,
  firstChoice:  Option[FirstChoice],
  reveal:       Option[Reveal],
  secondChoice: Option[Either[Change, Stay]])

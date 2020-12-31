package axle.game.montyhall

case class MontyHallState(
  placement:    Option[PlaceCar],
  placed:       Boolean, // allows this class to be reused for 'Masked' version
  firstChoice:  Option[FirstChoice],
  reveal:       Option[Reveal],
  secondChoice: Option[Either[Change, Stay]])

package axle.game.guessriffle

import axle.game.cards._

trait GuessRiffleMove

// Player move

case class GuessCard(card: Card) extends GuessRiffleMove

// Two Dealer moves

case class Riffle() extends GuessRiffleMove

case class RevealAndScore() extends GuessRiffleMove

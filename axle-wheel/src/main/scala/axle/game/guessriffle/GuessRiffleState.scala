package axle.game.guessriffle

import axle.game.cards._

case class GuessRiffleState(
  initialDeck: Deck,
  riffledDeck: Option[Deck],
  remaining: List[Card],
  numCorrect: Int,
  currentGuess: Option[Card]
)

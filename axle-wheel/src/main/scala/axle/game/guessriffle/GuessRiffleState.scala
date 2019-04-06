package axle.game.guessriffle

import axle.game.cards._

case class GuessRiffleState(
  initialDeck: Deck,
  riffledDeck: Option[Deck],
  guess: Option[Card],
  remaining: List[Card],
  numCorrect: Int,
  currentGuess: Option[Card]
)

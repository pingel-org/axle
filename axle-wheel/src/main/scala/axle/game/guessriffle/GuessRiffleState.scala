package axle.game.guessriffle

import axle.game.cards._

case class GuessRiffleState(
  initialDeck: Deck,
  riffledDeck: Option[Deck],
  guess: Option[Card],
  history: List[Card], // history of revealed cards in reverse order
  remaining: List[Card],
  numCorrect: Int,
  currentGuess: Option[Card]
)

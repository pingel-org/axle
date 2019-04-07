package axle.game.guessriffle

import axle.game.cards._

case class GuessRiffleState(
  initialDeck: Deck,
  riffledDeck: Option[Deck],
  guess: Option[Card],
  revealed: List[Card], // will store in reverse order
  remaining: List[Card],
  numCorrect: Int,
  currentGuess: Option[Card]
)

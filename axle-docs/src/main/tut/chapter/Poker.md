Poker
=====

An N-Player, Imperfect Information, Zero-sum game

Example
-------

The `axle.game.cards` package models decks, cards, ranks, suits, and ordering.

Define a function that takes the hand size and returns the best 5-card hand

```tut:book
import axle.game.cards.Deck
import axle.game.poker.PokerHand
import axle.game.poker.PokerHandCategory
import spire.compat.ordering

def winnerFromHandSize(handSize: Int) =
  Deck().cards.take(handSize).combinations(5).map(PokerHand(_)).toList.max

import axle.string

string(winnerFromHandSize(7))
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```tut:book
val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted

hands.map({ hand => string(hand) + "  " + hand.description }).mkString("\n")
```

Record 1000 simulated hands for each drawn hand size from 5 to 9

```tut:book
val data: IndexedSeq[(PokerHandCategory, Int)] =
  for {
    handSize <- 5 to 9
    trial <- 1 to 1000
  } yield (winnerFromHandSize(handSize).category, handSize)
```

BarChartGrouped to visualize the results

```tut:book
import spire.implicits.IntAlgebra
import axle.visualize.BarChartGrouped

val chart = BarChartGrouped[PokerHandCategory, Int, Int, Map[(PokerHandCategory, Int), Int]](
  data.tally.withDefaultValue(0),
  title = Some("Poker Hands"),
  keyTitle = Some("Hand Size"),
  yAxisLabel = Some("instances of category by hand size (1000 trials each)")
  )

import axle.web._
svg(chart, "pokerhands.svg")
```

![poker hands](../images/pokerhands.svg)

### Texas Hold 'Em Poker

As a game of "imperfect information", poker introduces the concept of Information Set.

```
import axle.game.poker._

val game = new Poker(3)
game.play()
```

An example of the first few lines of output:

```
Texas Hold Em Poker

Example moves:

  check
  raise 1.0
  call
  fold

Dealer initial deal.

To: Player 3
Current bet: 2
Pot: 3
Shared: ?? ?? ?? ?? ??

P1:  hand ?? ?? in for $1, $99 remaining
P2:  hand ?? ?? in for $2, $98 remaining
P3:  hand 7♢ Q♢ in for $--, $100 remaining
```

Note that `game.play()` will play a single round.
`game.playContinuously()` will play rounds until there is only a single player with money remaining.

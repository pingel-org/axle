Poker
=====

An N-Player, Imperfect Information, Zero-sum game

Example
-------

The `axle.game.cards` package models decks, cards, ranks, suits, and ordering.

Define a function that takes the hand size and returns the best 5-card hand

```scala
scala> import axle.game.cards.Deck
import axle.game.cards.Deck

scala> import axle.game.poker.PokerHand
import axle.game.poker.PokerHand

scala> import axle.game.poker.PokerHandCategory
import axle.game.poker.PokerHandCategory

scala> import spire.compat.ordering
import spire.compat.ordering

scala> def winnerFromHandSize(handSize: Int) =
     |   Deck().cards.take(handSize).combinations(5).map(PokerHand(_)).toList.max
winnerFromHandSize: (handSize: Int)axle.game.poker.PokerHand

scala> import axle.string
import axle.string

scala> string(winnerFromHandSize(7))
res0: String = 4♣ 4♢ 9♠ Q♠ A♣
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala
scala> val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted
hands: scala.collection.immutable.IndexedSeq[axle.game.poker.PokerHand] = Vector(PokerHand(Vector(Card(axle.game.cards.R9$@5ce366c7,axle.game.cards.Hearts$@6891c926), Card(axle.game.cards.R10$@277a6558,axle.game.cards.Clubs$@686ef97a), Card(axle.game.cards.R7$@2ff16f74,axle.game.cards.Spades$@7adf2509), Card(axle.game.cards.Jack$@1cfef222,axle.game.cards.Hearts$@6891c926), Card(axle.game.cards.R6$@51e81170,axle.game.cards.Clubs$@686ef97a))), PokerHand(Vector(Card(axle.game.cards.R10$@277a6558,axle.game.cards.Clubs$@686ef97a), Card(axle.game.cards.R8$@84aa5af,axle.game.cards.Clubs$@686ef97a), Card(axle.game.cards.R7$@2ff16f74,axle.game.cards.Hearts$@6891c926), Card(axle.game.cards.R5$@5418fb8e,axle.game.cards.Spades$@7adf2509), Card(axle.game.cards.Queen$@4fc1133b,axle.game.cards.Diamond...

scala> hands.map({ hand => string(hand) + "  " + hand.description }).mkString("\n")
res1: String =
6♣ 7♠ 9♡ T♣ J♡  J high
5♠ 7♡ 8♣ T♣ Q♢  Q high
7♡ 9♣ T♠ J♣ K♢  K high
6♠ 8♢ T♣ Q♠ K♢  K high
8♠ T♠ J♣ Q♢ K♠  K high
4♡ 4♠ 9♠ T♡ Q♢  pair of 4
8♣ 9♠ J♢ J♠ A♠  pair of J
7♣ 9♣ J♢ K♡ K♠  pair of K
4♣ 5♢ Q♡ A♡ A♢  pair of A
9♢ T♢ Q♠ A♡ A♠  pair of A
2♡ 2♠ 3♣ 3♢ K♡  two pair 3 and 2
3♡ 3♠ 4♣ 4♢ 6♡  two pair 4 and 3
6♣ 6♡ 7♠ 7♡ K♣  two pair 7 and 6
2♢ 2♠ 9♢ 9♣ Q♠  two pair 9 and 2
7♠ 7♣ 9♣ K♡ K♢  two pair K and 7
9♣ T♠ T♣ K♠ K♢  two pair K and T
8♢ Q♡ A♣ A♢ A♡  three of a kind of A
T♡ J♠ Q♢ K♡ A♡  straight to A
2♣ 2♠ 2♢ K♠ K♡  full house 2 over K
5♢ 5♣ 8♣ 8♢ 8♠  full house 8 over 5
```

Record 1000 simulated hands for each drawn hand size from 5 to 9

```scala
scala> val data: IndexedSeq[(PokerHandCategory, Int)] =
     |   for {
     |     handSize <- 5 to 9
     |     trial <- 1 to 1000
     |   } yield (winnerFromHandSize(handSize).category, handSize)
data: IndexedSeq[(axle.game.poker.PokerHandCategory, Int)] = Vector((axle.game.poker.Pair$@b4a390f,5), (axle.game.poker.Pair$@b4a390f,5), (axle.game.poker.TwoPair$@1c49f0d0,5), (axle.game.poker.High$@3a7670c5,5), (axle.game.poker.High$@3a7670c5,5), (axle.game.poker.High$@3a7670c5,5), (axle.game.poker.Pair$@b4a390f,5), (axle.game.poker.High$@3a7670c5,5), (axle.game.poker.High$@3a7670c5,5), (axle.game.poker.Pair$@b4a390f,5), (axle.game.poker.Pair$@b4a390f,5), (axle.game.poker.Pair$@b4a390f,5), (axle.game.poker.TwoPair$@1c49f0d0,5), (axle.game.poker.High$@3a7670c5,5), (axle.game.poker.High$@3a7670c5,5), (axle.game.poker.Pair$@b4a390f,5), (axle.game.poker.High$@3a7670c5,5), (axle.game.poker.Pair$@b4a390f,5), (axle.game.poker.High$@3a7670c5,5), (axle.game.poker.Pair$@b4a390f,5), (axle.game.p...
```

BarChartGrouped to visualize the results

```scala
scala> import spire.implicits.IntAlgebra
import spire.implicits.IntAlgebra

scala> import axle.visualize.BarChartGrouped
import axle.visualize.BarChartGrouped

scala> val chart = BarChartGrouped[PokerHandCategory, Int, Int, Map[(PokerHandCategory, Int), Int]](
     |   data.tally.withDefaultValue(0),
     |   title = Some("Poker Hands"),
     |   keyTitle = Some("Hand Size"),
     |   yAxisLabel = Some("instances of category by hand size (1000 trials each)")
     |   )
chart: axle.visualize.BarChartGrouped[axle.game.poker.PokerHandCategory,Int,Int,Map[(axle.game.poker.PokerHandCategory, Int),Int]] = BarChartGrouped(Map((axle.game.poker.FullHouse$@5a072a0f,8) -> 63, (axle.game.poker.FourOfAKind$@53aa8385,9) -> 9, (axle.game.poker.High$@3a7670c5,9) -> 11, (axle.game.poker.High$@3a7670c5,7) -> 188, (axle.game.poker.Pair$@b4a390f,9) -> 173, (axle.game.poker.TwoPair$@1c49f0d0,9) -> 409, (axle.game.poker.Straight$@1dbf78a4,6) -> 7, (axle.game.poker.StraightFlush$@6c7ee771,9) -> 1, (axle.game.poker.Flush$@4e23d4b3,6) -> 9, (axle.game.poker.Flush$@4e23d4b3,7) -> 37, (axle.game.poker.Flush$@4e23d4b3,9) -> 113, (axle.game.poker.Pair$@b4a390f,7) -> 423, (axle.game.poker.FullHouse$@5a072a0f,9) -> 123, (axle.game.poker.High$@3a7670c5,5) -> 528, (axle.game.poker.Fo...

scala> import axle.web._
import axle.web._

scala> svg(chart, "pokerhands.svg")
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

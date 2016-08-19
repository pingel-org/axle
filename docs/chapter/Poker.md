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
res0: String = 9♠ T♠ J♣ J♢ A♢
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala
scala> val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted
hands: scala.collection.immutable.IndexedSeq[axle.game.poker.PokerHand] = Vector(PokerHand(Vector(Card(axle.game.cards.R5$@3889d68e,axle.game.cards.Clubs$@368bb86d), Card(axle.game.cards.R8$@43406934,axle.game.cards.Spades$@1ec0811a), Card(axle.game.cards.Jack$@6b47c521,axle.game.cards.Spades$@1ec0811a), Card(axle.game.cards.R10$@75e3071b,axle.game.cards.Clubs$@368bb86d), Card(axle.game.cards.R6$@20d8b0ec,axle.game.cards.Diamonds$@1001a2aa))), PokerHand(Vector(Card(axle.game.cards.R9$@3c9749a,axle.game.cards.Clubs$@368bb86d), Card(axle.game.cards.R10$@75e3071b,axle.game.cards.Diamonds$@1001a2aa), Card(axle.game.cards.R4$@729d6df0,axle.game.cards.Spades$@1ec0811a), Card(axle.game.cards.R5$@3889d68e,axle.game.cards.Spades$@1ec0811a), Card(axle.game.cards.Queen$@e4fceff,axle.game.cards.Spa...

scala> hands.map({ hand => string(hand) + "  " + hand.description }).mkString("\n")
res1: String =
5♣ 6♢ 8♠ T♣ J♠  J high
4♠ 5♠ 9♣ T♢ Q♠  Q high
8♠ 9♠ J♣ Q♣ K♡  K high
4♠ T♠ J♡ K♢ A♣  A high
4♣ T♢ J♡ K♣ A♢  A high
3♠ 3♢ 9♡ J♡ K♡  pair of 3
4♡ 4♣ 7♡ T♣ Q♢  pair of 4
4♣ 4♠ 9♡ T♠ A♡  pair of 4
7♢ 7♡ J♠ Q♣ K♡  pair of 7
8♢ 8♡ 9♠ T♡ K♡  pair of 8
T♣ Q♡ K♣ A♠ A♢  pair of A
2♡ 2♣ 5♡ 5♠ T♡  two pair 5 and 2
2♠ 2♣ 7♡ 7♢ A♢  two pair 7 and 2
5♡ 5♣ 8♣ 8♡ A♡  two pair 8 and 5
9♡ 9♢ J♠ J♣ Q♠  two pair J and 9
9♢ 9♡ J♢ J♡ K♢  two pair J and 9
3♡ 3♢ Q♠ A♡ A♢  two pair A and 3
7♡ 7♣ 7♠ 9♢ A♣  three of a kind of 7
9♡ 9♣ 9♠ T♢ Q♢  three of a kind of 9
4♠ 7♠ J♠ Q♠ K♠  flush in ♠
```

Record 1000 simulated hands for each drawn hand size from 5 to 9

```scala
scala> val data: IndexedSeq[(PokerHandCategory, Int)] =
     |   for {
     |     handSize <- 5 to 9
     |     trial <- 1 to 1000
     |   } yield (winnerFromHandSize(handSize).category, handSize)
data: IndexedSeq[(axle.game.poker.PokerHandCategory, Int)] = Vector((axle.game.poker.Pair$@13e7ceee,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.Pair$@13e7ceee,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.Pair$@13e7ceee,5), (axle.game.poker.Pair$@13e7ceee,5), (axle.game.poker.Pair$@13e7ceee,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.Pair$@13e7ceee,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.Pair$@13e7ceee,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.Pair$@13e7ceee,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.High$@7ff58399,5), (axle.game.poker.Pair$@13e7ceee,5), (axle.gam...
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
chart: axle.visualize.BarChartGrouped[axle.game.poker.PokerHandCategory,Int,Int,Map[(axle.game.poker.PokerHandCategory, Int),Int]] = BarChartGrouped(Map((axle.game.poker.ThreeOfAKind$@e177b20,9) -> 42, (axle.game.poker.Straight$@23aa4105,5) -> 6, (axle.game.poker.RoyalFlush$@8acf51a,7) -> 1, (axle.game.poker.TwoPair$@402a4689,5) -> 64, (axle.game.poker.High$@7ff58399,6) -> 341, (axle.game.poker.FourOfAKind$@7a7696d,8) -> 3, (axle.game.poker.Flush$@5d2dc830,8) -> 77, (axle.game.poker.FourOfAKind$@7a7696d,6) -> 1, (axle.game.poker.Straight$@23aa4105,6) -> 13, (axle.game.poker.High$@7ff58399,5) -> 479, (axle.game.poker.FourOfAKind$@7a7696d,9) -> 3, (axle.game.poker.ThreeOfAKind$@e177b20,5) -> 13, (axle.game.poker.ThreeOfAKind$@e177b20,7) -> 54, (axle.game.poker.TwoPair$@402a4689,9) -> 409,...

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

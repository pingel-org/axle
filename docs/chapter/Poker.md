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
res0: String = 5♠ 9♢ 9♡ K♡ K♢
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala
scala> val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted
hands: scala.collection.immutable.IndexedSeq[axle.game.poker.PokerHand] = Vector(PokerHand(Vector(Card(axle.game.cards.R7$@35280e82,axle.game.cards.Diamonds$@2d1b65b1), Card(axle.game.cards.Jack$@5697b7cb,axle.game.cards.Clubs$@28667a2e), Card(axle.game.cards.Queen$@1bcb17ae,axle.game.cards.Spades$@74943f7c), Card(axle.game.cards.R10$@4d33adac,axle.game.cards.Hearts$@4bd08744), Card(axle.game.cards.R9$@57b45ce1,axle.game.cards.Clubs$@28667a2e))), PokerHand(Vector(Card(axle.game.cards.Ace$@3df373db,axle.game.cards.Spades$@74943f7c), Card(axle.game.cards.Jack$@5697b7cb,axle.game.cards.Clubs$@28667a2e), Card(axle.game.cards.R6$@1b161ed6,axle.game.cards.Clubs$@28667a2e), Card(axle.game.cards.R8$@569f0a5b,axle.game.cards.Hearts$@4bd08744), Card(axle.game.cards.R10$@4d33adac,axle.game.cards.S...

scala> hands.map({ hand => string(hand) + "  " + hand.description }).mkString("\n")
res1: String =
7♢ 9♣ T♡ J♣ Q♠  Q high
6♣ 8♡ T♠ J♣ A♠  A high
7♠ T♣ J♢ K♣ A♡  A high
9♣ T♠ Q♢ K♢ A♠  A high
3♠ 3♢ 7♡ T♣ K♣  pair of 3
6♡ 6♣ 7♢ J♢ A♡  pair of 6
5♢ 7♠ 9♡ 9♢ T♣  pair of 9
J♠ J♢ Q♢ K♣ A♣  pair of J
5♡ 9♠ J♢ A♣ A♢  pair of A
9♠ T♢ Q♡ A♢ A♠  pair of A
8♡ J♣ K♣ A♢ A♡  pair of A
4♣ 4♡ 7♡ 7♣ A♢  two pair 7 and 4
2♠ 2♡ 8♢ 8♣ A♢  two pair 8 and 2
2♢ 2♠ 7♡ J♣ J♠  two pair J and 2
J♣ J♠ Q♡ A♣ A♢  two pair A and J
2♡ 2♣ 2♢ K♡ A♣  three of a kind of 2
4♣ 4♢ 4♠ 9♣ J♠  three of a kind of 4
6♣ 7♣ J♢ J♠ J♣  three of a kind of J
2♢ 3♢ 4♢ 5♢ 6♠  straight to 6
T♢ J♣ Q♢ K♠ A♣  straight to A
```

Record 1000 simulated hands for each drawn hand size from 5 to 9

```scala
scala> val data: IndexedSeq[(PokerHandCategory, Int)] =
     |   for {
     |     handSize <- 5 to 9
     |     trial <- 1 to 1000
     |   } yield (winnerFromHandSize(handSize).category, handSize)
data: IndexedSeq[(axle.game.poker.PokerHandCategory, Int)] = Vector((axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.Pair$@6ab333ca,5), (axle.game.poker.Pair$@6ab333ca,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.Pair$@6ab333ca,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.High$@33644315,5), (axle.game.poker.Pair$@6ab333ca,5), (axle.game.poker.Pair$@6ab333ca,5), (axle.game.poker.Pair$@6ab333ca,5), (axle.gam...
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
chart: axle.visualize.BarChartGrouped[axle.game.poker.PokerHandCategory,Int,Int,Map[(axle.game.poker.PokerHandCategory, Int),Int]] = BarChartGrouped(Map((axle.game.poker.Flush$@5694e120,5) -> 4, (axle.game.poker.Flush$@5694e120,7) -> 31, (axle.game.poker.FullHouse$@7721c73b,7) -> 28, (axle.game.poker.TwoPair$@66cc5c1c,8) -> 343, (axle.game.poker.Flush$@5694e120,9) -> 126, (axle.game.poker.High$@33644315,6) -> 343, (axle.game.poker.ThreeOfAKind$@79c57f86,7) -> 64, (axle.game.poker.High$@33644315,9) -> 21, (axle.game.poker.Straight$@4ff9776,6) -> 13, (axle.game.poker.Pair$@6ab333ca,6) -> 469, (axle.game.poker.Pair$@6ab333ca,7) -> 450, (axle.game.poker.Straight$@4ff9776,5) -> 4, (axle.game.poker.High$@33644315,7) -> 146, (axle.game.poker.Pair$@6ab333ca,8) -> 333, (axle.game.poker.High$@336...

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

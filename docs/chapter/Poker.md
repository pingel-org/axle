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
res0: String = J♠ J♡ Q♣ K♡ A♢
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala
scala> val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted
hands: scala.collection.immutable.IndexedSeq[axle.game.poker.PokerHand] = Vector(PokerHand(Vector(Card(axle.game.cards.R8$@3abf839e,axle.game.cards.Spades$@775fa641), Card(axle.game.cards.Queen$@281b13a4,axle.game.cards.Hearts$@6fda8ebc), Card(axle.game.cards.Jack$@4a45daa5,axle.game.cards.Spades$@775fa641), Card(axle.game.cards.R7$@58ee44cd,axle.game.cards.Diamonds$@4ad2a8e), Card(axle.game.cards.R4$@7655db4c,axle.game.cards.Hearts$@6fda8ebc))), PokerHand(Vector(Card(axle.game.cards.R9$@46c3bc51,axle.game.cards.Hearts$@6fda8ebc), Card(axle.game.cards.Jack$@4a45daa5,axle.game.cards.Spades$@775fa641), Card(axle.game.cards.R4$@7655db4c,axle.game.cards.Clubs$@71c75a1a), Card(axle.game.cards.Queen$@281b13a4,axle.game.cards.Hearts$@6fda8ebc), Card(axle.game.cards.Ace$@60d954c2,axle.game.card...

scala> hands.map({ hand => string(hand) + "  " + hand.description }).mkString("\n")
res1: String =
4♡ 7♢ 8♠ J♠ Q♡  Q high
4♣ 9♡ J♠ Q♡ A♡  A high
2♢ 2♡ J♢ Q♡ K♡  pair of 2
2♠ 2♣ J♢ Q♠ K♣  pair of 2
2♣ 2♢ 6♡ J♣ A♣  pair of 2
3♠ 3♡ 9♠ J♣ Q♣  pair of 3
5♡ 5♠ 7♣ T♠ J♠  pair of 5
5♢ 5♡ T♡ J♣ K♣  pair of 5
5♢ 6♡ 6♢ T♢ J♡  pair of 6
6♢ 6♠ 7♠ 8♠ K♣  pair of 6
8♣ 8♡ T♣ J♢ K♡  pair of 8
8♠ 9♢ 9♡ J♣ K♠  pair of 9
7♠ 8♡ J♠ J♣ Q♠  pair of J
7♣ 8♣ J♠ J♣ K♡  pair of J
5♣ 5♡ 7♠ 7♢ A♡  two pair 7 and 5
4♣ 4♠ 8♡ K♠ K♢  two pair K and 4
3♠ 3♢ K♣ A♠ A♢  two pair A and 3
7♣ 8♠ 9♠ T♣ J♢  straight to J
3♡ 7♡ 9♡ J♡ A♡  flush in ♡
6♢ 6♠ 6♣ 6♡ K♣  four of a kind of 6
```

Record 1000 simulated hands for each drawn hand size from 5 to 9

```scala
scala> val data: IndexedSeq[(PokerHandCategory, Int)] =
     |   for {
     |     handSize <- 5 to 9
     |     trial <- 1 to 1000
     |   } yield (winnerFromHandSize(handSize).category, handSize)
data: IndexedSeq[(axle.game.poker.PokerHandCategory, Int)] = Vector((axle.game.poker.High$@432c69cd,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.TwoPair$@276fa4c0,5), (axle.game.poker.Pair$@2ff7304f,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.Pair$@2ff7304f,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.High$@432c69cd,5), (axle.game.poker.Pair$@2ff7304f,5), (axle.game.poker.Pair$@2ff7304f,5), (axle.game.poker.Pair$@2ff7304f,5), (axle.game.poker.High$@432c69cd,5), (axle....
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
chart: axle.visualize.BarChartGrouped[axle.game.poker.PokerHandCategory,Int,Int,Map[(axle.game.poker.PokerHandCategory, Int),Int]] = BarChartGrouped(Map((axle.game.poker.Flush$@414350b4,5) -> 2, (axle.game.poker.High$@432c69cd,8) -> 75, (axle.game.poker.Straight$@57a0759b,9) -> 114, (axle.game.poker.ThreeOfAKind$@3b92da14,6) -> 32, (axle.game.poker.Straight$@57a0759b,6) -> 20, (axle.game.poker.Pair$@2ff7304f,7) -> 444, (axle.game.poker.Flush$@414350b4,7) -> 29, (axle.game.poker.High$@432c69cd,6) -> 340, (axle.game.poker.High$@432c69cd,9) -> 22, (axle.game.poker.TwoPair$@276fa4c0,9) -> 411, (axle.game.poker.FullHouse$@1bd0beaa,6) -> 6, (axle.game.poker.FourOfAKind$@38afcad8,7) -> 1, (axle.game.poker.Flush$@414350b4,6) -> 6, (axle.game.poker.Pair$@2ff7304f,8) -> 325, (axle.game.poker.TwoP...

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

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
res0: String = 7♢ 7♣ 8♠ 9♡ K♣
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala
scala> val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted
hands: scala.collection.immutable.IndexedSeq[axle.game.poker.PokerHand] = Vector(PokerHand(Vector(Card(axle.game.cards.R5$@b245b0e,axle.game.cards.Spades$@7420b8cc), Card(axle.game.cards.R9$@f3545c1,axle.game.cards.Diamonds$@35528e88), Card(axle.game.cards.Jack$@25f673a8,axle.game.cards.Clubs$@212d570b), Card(axle.game.cards.Queen$@50260e6b,axle.game.cards.Clubs$@212d570b), Card(axle.game.cards.R7$@446bd748,axle.game.cards.Spades$@7420b8cc))), PokerHand(Vector(Card(axle.game.cards.R8$@55275a2e,axle.game.cards.Spades$@7420b8cc), Card(axle.game.cards.R10$@7efa00d8,axle.game.cards.Clubs$@212d570b), Card(axle.game.cards.King$@6e2bca1a,axle.game.cards.Hearts$@60ac8823), Card(axle.game.cards.Jack$@25f673a8,axle.game.cards.Diamonds$@35528e88), Card(axle.game.cards.R6$@4d9e63cd,axle.game.cards....

scala> hands.map({ hand => string(hand) + "  " + hand.description }).mkString("\n")
res1: String =
5♠ 7♠ 9♢ J♣ Q♣  Q high
6♢ 8♠ T♣ J♢ K♡  K high
5♣ 7♣ 9♣ Q♣ K♡  K high
6♠ 7♣ T♡ K♣ A♡  A high
3♠ 3♢ 6♡ J♣ Q♢  pair of 3
3♢ 3♡ 9♠ J♢ A♡  pair of 3
8♢ 8♡ 9♠ Q♢ K♣  pair of 8
8♡ 8♠ Q♡ K♢ A♢  pair of 8
5♡ 6♣ 7♢ Q♡ Q♠  pair of Q
5♡ 7♢ K♢ A♡ A♣  pair of A
4♡ 4♢ 5♡ 5♠ 8♡  two pair 5 and 4
2♠ 2♣ 9♡ 9♣ K♠  two pair 9 and 2
5♢ 5♣ T♠ T♣ A♢  two pair T and 5
3♡ 3♢ 7♢ J♢ J♡  two pair J and 3
9♡ 9♠ J♢ J♣ K♠  two pair J and 9
8♣ T♡ T♠ A♢ A♡  two pair A and T
9♣ 9♠ 9♡ K♢ A♡  three of a kind of 9
6♣ 7♠ 8♢ 9♣ T♣  straight to T
6♡ 7♡ 8♠ 9♠ T♠  straight to T
T♣ J♣ Q♠ K♡ A♡  straight to A
```

Record 1000 simulated hands for each drawn hand size from 5 to 9

```scala
scala> val data: IndexedSeq[(PokerHandCategory, Int)] =
     |   for {
     |     handSize <- 5 to 9
     |     trial <- 1 to 1000
     |   } yield (winnerFromHandSize(handSize).category, handSize)
data: IndexedSeq[(axle.game.poker.PokerHandCategory, Int)] = Vector((axle.game.poker.High$@2a583248,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.High$@2a583248,5), (axle.game.poker.High$@2a583248,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.TwoPair$@70e07e77,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.High$@2a583248,5), (axle.game.poker.High$@2a583248,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.High$@2a583248,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.High$@2a583248,5), (axle.game.poker.Pair$@2bdd9df7,5), (axle.game.poker.High$@2a583248,5), (axle....
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
chart: axle.visualize.BarChartGrouped[axle.game.poker.PokerHandCategory,Int,Int,Map[(axle.game.poker.PokerHandCategory, Int),Int]] = BarChartGrouped(Map((axle.game.poker.TwoPair$@70e07e77,5) -> 56, (axle.game.poker.FullHouse$@21b9369b,6) -> 14, (axle.game.poker.High$@2a583248,8) -> 74, (axle.game.poker.Flush$@664c257e,5) -> 2, (axle.game.poker.FourOfAKind$@262039ba,7) -> 5, (axle.game.poker.High$@2a583248,5) -> 497, (axle.game.poker.Straight$@152cec06,7) -> 40, (axle.game.poker.Pair$@2bdd9df7,6) -> 469, (axle.game.poker.Flush$@664c257e,9) -> 131, (axle.game.poker.ThreeOfAKind$@4a1887e2,8) -> 43, (axle.game.poker.ThreeOfAKind$@4a1887e2,9) -> 42, (axle.game.poker.Flush$@664c257e,6) -> 10, (axle.game.poker.Straight$@152cec06,9) -> 135, (axle.game.poker.Pair$@2bdd9df7,9) -> 143, (axle.game....

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

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
res0: String = 2♠ 2♢ J♡ J♠ K♠
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala
scala> val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted
hands: scala.collection.immutable.IndexedSeq[axle.game.poker.PokerHand] = Vector(PokerHand(Vector(Card(axle.game.cards.R6$@3d7093ae,axle.game.cards.Spades$@5662d940), Card(axle.game.cards.R5$@1b8855fe,axle.game.cards.Clubs$@43ea36b9), Card(axle.game.cards.Jack$@38ead3bf,axle.game.cards.Spades$@5662d940), Card(axle.game.cards.R9$@6756722b,axle.game.cards.Clubs$@43ea36b9), Card(axle.game.cards.R10$@3c5ad1eb,axle.game.cards.Hearts$@234966ec))), PokerHand(Vector(Card(axle.game.cards.Ace$@2a4523c2,axle.game.cards.Clubs$@43ea36b9), Card(axle.game.cards.R6$@3d7093ae,axle.game.cards.Clubs$@43ea36b9), Card(axle.game.cards.R8$@1a405227,axle.game.cards.Spades$@5662d940), Card(axle.game.cards.Queen$@7f9fd695,axle.game.cards.Clubs$@43ea36b9), Card(axle.game.cards.King$@6e72ed06,axle.game.cards.Clubs...

scala> hands.map({ hand => string(hand) + "  " + hand.description }).mkString("\n")
res1: String =
5♣ 6♠ 9♣ T♡ J♠  J high
6♣ 8♠ Q♣ K♣ A♣  A high
3♠ 3♢ Q♡ K♡ A♡  pair of 3
5♢ 5♣ 8♣ T♢ Q♢  pair of 5
5♣ 5♢ 9♠ J♢ A♢  pair of 5
5♢ 5♡ Q♠ K♠ A♢  pair of 5
6♣ 6♡ 9♠ T♢ A♡  pair of 6
9♠ 9♡ Q♠ K♣ A♡  pair of 9
9♠ T♣ T♠ J♡ A♣  pair of T
3♠ 3♡ 7♢ 7♡ T♢  two pair 7 and 3
5♢ 5♠ 8♡ 8♣ K♡  two pair 8 and 5
3♠ 3♡ 9♡ 9♣ Q♣  two pair 9 and 3
9♣ 9♠ J♠ J♣ A♣  two pair J and 9
7♢ T♠ T♣ Q♠ Q♢  two pair Q and T
9♠ J♠ J♡ Q♢ Q♠  two pair Q and J
2♠ 2♡ 2♢ 6♠ Q♣  three of a kind of 2
5♣ 5♠ 5♡ Q♣ K♢  three of a kind of 5
7♣ 8♢ 9♠ T♠ J♢  straight to J
6♡ 8♡ 9♡ T♡ J♡  flush in ♡
2♢ 7♢ 8♢ J♢ Q♢  flush in ♢
```

Record 1000 simulated hands for each drawn hand size from 5 to 9

```scala
scala> val data: IndexedSeq[(PokerHandCategory, Int)] =
     |   for {
     |     handSize <- 5 to 9
     |     trial <- 1 to 1000
     |   } yield (winnerFromHandSize(handSize).category, handSize)
data: IndexedSeq[(axle.game.poker.PokerHandCategory, Int)] = Vector((axle.game.poker.High$@27854d8f,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.Pair$@715501cd,5), (axle.game.poker.Pair$@715501cd,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.TwoPair$@16ed3509,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.Pair$@715501cd,5), (axle.game.poker.High$@27854d8f,5), (axle.game.poker.Pair$@715501cd,5), (axle.game.poker.Pair$@715501cd,5), (axle.game.poker.High$@27854d8f,5), (axle....
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
chart: axle.visualize.BarChartGrouped[axle.game.poker.PokerHandCategory,Int,Int,Map[(axle.game.poker.PokerHandCategory, Int),Int]] = BarChartGrouped(Map((axle.game.poker.TwoPair$@16ed3509,9) -> 408, (axle.game.poker.Straight$@5681d53b,6) -> 16, (axle.game.poker.Pair$@715501cd,5) -> 408, (axle.game.poker.FullHouse$@12b33343,9) -> 122, (axle.game.poker.High$@27854d8f,9) -> 17, (axle.game.poker.FullHouse$@12b33343,5) -> 1, (axle.game.poker.FourOfAKind$@29bbe79f,7) -> 2, (axle.game.poker.ThreeOfAKind$@2e72a89,6) -> 43, (axle.game.poker.High$@27854d8f,5) -> 509, (axle.game.poker.StraightFlush$@6543ebc5,8) -> 2, (axle.game.poker.FourOfAKind$@29bbe79f,9) -> 7, (axle.game.poker.FullHouse$@12b33343,8) -> 55, (axle.game.poker.ThreeOfAKind$@2e72a89,8) -> 57, (axle.game.poker.Straight$@5681d53b,7) ...

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

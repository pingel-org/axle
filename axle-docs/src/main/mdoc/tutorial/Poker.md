---
layout: page
title: Poker
permalink: /tutorial/poker/
---

An N-Player, Imperfect Information, Zero-sum game

## Example

The `axle.game.cards` package models decks, cards, ranks, suits, and ordering.

Define a function that takes the hand size and returns the best 5-card hand

```scala mdoc
import cats.implicits._
import cats.Order.catsKernelOrderingForOrder

import axle.game.cards.Deck
import axle.game.poker.PokerHand

def winnerFromHandSize(handSize: Int) =
  Deck().cards.take(handSize).combinations(5).map(cs => PokerHand(cs.toVector)).toList.max

winnerFromHandSize(7).show
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala mdoc
val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted

hands.map({ hand => hand.show + "  " + hand.description }).mkString("\n")
```

Record 1000 simulated hands for each drawn hand size from 5 to 9

```scala mdoc
import axle.game.poker.PokerHandCategory

val data: IndexedSeq[(PokerHandCategory, Int)] =
  for {
    handSize <- 5 to 9
    trial <- 1 to 1000
  } yield (winnerFromHandSize(handSize).category, handSize)
```

BarChartGrouped to visualize the results

```scala mdoc
import spire.algebra.CRing

import axle.visualize.BarChartGrouped
import axle.visualize.Color._
import axle.syntax.talliable.talliableOps

implicit val ringInt: CRing[Int] = spire.implicits.IntAlgebra

val colors = List(black, red, blue, yellow, green)

val chart = BarChartGrouped[PokerHandCategory, Int, Int, Map[(PokerHandCategory, Int), Int], String](
  () => data.tally.withDefaultValue(0),
  title = Some("Poker Hands"),
  drawKey = false,
  yAxisLabel = Some("instances of category by hand size (1000 trials each)"),
  colorOf = (cat: PokerHandCategory, handSize: Int) => colors( (handSize - 5) % colors.size),
  hoverOf = (cat: PokerHandCategory, handSize: Int) => Some(s"${cat.show} from $handSize")
)
```

Render as SVG file

```scala mdoc
import axle.web._
import cats.effect._

chart.svg[IO]("pokerhands.svg").unsafeRunSync()
```

![poker hands](/tutorial/images/pokerhands.svg)

### Texas Hold 'Em Poker

As a game of "imperfect information", poker introduces the concept of Information Set.

```scala mdoc
import axle._
import axle.IO.prefixedDisplay
import axle.game._
import axle.game.poker._
import axle.game.poker.evGame._
import Strategies._

val p1 = Player("P1", "Player 1")
val p2 = Player("P2", "Player 2")

val game = Poker(Vector(
  (p1, randomMove, prefixedDisplay("1")(println)),
  (p2, randomMove, prefixedDisplay("2")(println))),
  prefixedDisplay("D")(println))
```

Compute the end state from the start state

```scala mdoc
import spire.random.Generator.rng

play(game, startState(game), false, rng)
```

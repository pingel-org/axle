---
layout: page
title: Poker
permalink: /tutorial/poker/
---

An N-Player, Imperfect Information, Zero-sum game

Example
-------

The `axle.game.cards` package models decks, cards, ranks, suits, and ordering.

Imports

```tut:silent
import cats.implicits._
import cats.Order.catsKernelOrderingForOrder

import axle.game.cards.Deck
import axle.game.poker.PokerHand
import axle.game.poker.PokerHandCategory
import axle.string
```

Define a function that takes the hand size and returns the best 5-card hand

```tut:book
def winnerFromHandSize(handSize: Int) =
  Deck().cards.take(handSize).combinations(5).map(PokerHand(_)).toList.max

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

![poker hands](/tutorial/images/pokerhands.svg)

### Texas Hold 'Em Poker

As a game of "imperfect information", poker introduces the concept of Information Set.

```tut
import axle._
import axle.game._
import axle.game.poker._
import Strategies._

val p1 = Player("P1", "Player 1")
val p2 = Player("P2", "Player 2")

val game = Poker(Vector(
  (p1, randomMove, prefixedDisplay("1")(println)),
  (p2, randomMove, prefixedDisplay("2")(println))),
  prefixedDisplay("D")(println))

play(game)
```

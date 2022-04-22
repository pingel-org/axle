---
layout: page
title: Poker
permalink: /tutorial/poker/
---

An N-Player, Imperfect Information, Zero-sum game

## Example

The `axle.game.cards` package models decks, cards, ranks, suits, and ordering.

Define a function that takes the hand size and returns the best 5-card hand

```scala
import cats.implicits._
import cats.Order.catsKernelOrderingForOrder

import axle.game.cards.Deck
import axle.game.poker.PokerHand

def winnerFromHandSize(handSize: Int) =
  Deck().cards.take(handSize).combinations(5).map(cs => PokerHand(cs.toVector)).toList.max

winnerFromHandSize(7).show
// res0: String = "6♡ 6♢ Q♢ K♡ A♢"
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala
val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted
// hands: IndexedSeq[PokerHand] = Vector(
//   PokerHand(
//     cards = Vector(
//       Card(
//         rank = axle.game.cards.R6$@29430553,
//         suit = axle.game.cards.Diamonds$@1b2b6c89
//       ),
//       Card(
//         rank = axle.game.cards.R7$@39256edb,
//         suit = axle.game.cards.Hearts$@5da9b90f
//       ),
//       Card(
//         rank = axle.game.cards.R10$@b39bf3c,
//         suit = axle.game.cards.Diamonds$@1b2b6c89
//       ),
//       Card(
//         rank = axle.game.cards.Queen$@28fa0e1b,
//         suit = axle.game.cards.Diamonds$@1b2b6c89
//       ),
//       Card(
//         rank = axle.game.cards.R5$@2a672e2e,
//         suit = axle.game.cards.Diamonds$@1b2b6c89
//       )
//     )
//   ),
//   PokerHand(
//     cards = Vector(
//       Card(
//         rank = axle.game.cards.Jack$@4ad6f420,
//         suit = axle.game.cards.Spades$@49a85f6c
//       ),
//       Card(
//         rank = axle.game.cards.R10$@b39bf3c,
//         suit = axle.game.cards.Hearts$@5da9b90f
//       ),
//       Card(
//         rank = axle.game.cards.R8$@65cac508,
//         suit = axle.game.cards.Diamonds$@1b2b6c89
//       ),
//       Card(
//         rank = axle.game.cards.R6$@29430553,
//         suit = axle.game.cards.Clubs$@6f3352ab
//       ),
//       Card(
//         rank = axle.game.cards.Queen$@28fa0e1b,
//         suit = axle.game.cards.Diamonds$@1b2b6c89
//       )
//     )
//   ),
// ...

hands.map({ hand => hand.show + "  " + hand.description }).mkString("\n")
// res1: String = """5♢ 6♢ 7♡ T♢ Q♢  high Q high
// 6♣ 8♢ T♡ J♠ Q♢  high Q high
// 8♣ T♢ J♣ Q♠ K♣  high K high
// 5♣ 7♣ 9♡ Q♠ A♣  high A high
// 6♠ 9♢ Q♠ K♡ A♢  high A high
// 6♢ 6♣ 9♢ J♣ A♡  pair of 6
// 6♡ 8♠ 8♣ 9♢ T♠  pair of 8
// 8♠ 8♣ 9♡ J♣ Q♣  pair of 8
// 8♠ 8♣ T♠ J♣ K♣  pair of 8
// 6♡ 7♢ J♡ J♣ A♣  pair of J
// 6♢ J♣ Q♠ Q♣ K♡  pair of Q
// 2♠ 2♢ 7♡ 7♣ A♡  two pair 7 and 2
// 6♢ 6♣ 8♠ 8♢ Q♣  two pair 8 and 6
// 8♠ 8♡ 9♡ 9♢ A♠  two pair 9 and 8
// 4♡ 4♣ T♠ T♢ A♣  two pair T and 4
// 6♠ 6♢ J♡ J♣ A♣  two pair J and 6
// 4♡ 4♣ J♣ A♡ A♢  two pair A and 4
// 4♣ 5♣ 6♠ 7♣ 8♢  straight to 8
// 3♢ 5♢ 6♢ 9♢ A♢  flush in ♢
// J♡ J♢ K♠ K♢ K♣  full house K over J"""
```

Record 1000 simulated hands for each drawn hand size from 5 to 9

```scala
import axle.game.poker.PokerHandCategory

val data: IndexedSeq[(PokerHandCategory, Int)] =
  for {
    handSize <- 5 to 9
    trial <- 1 to 1000
  } yield (winnerFromHandSize(handSize).category, handSize)
// data: IndexedSeq[(PokerHandCategory, Int)] = Vector(
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.TwoPair$@2595a8e5, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.ThreeOfAKind$@1cece3e3, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.TwoPair$@2595a8e5, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.ThreeOfAKind$@1cece3e3, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.Pair$@34422ed8, 5),
//   (axle.game.poker.High$@39731be2, 5),
//   (axle.game.poker.ThreeOfAKind$@1cece3e3, 5),
//   (axle.game.poker.High$@39731be2, 5),
// ...
```

BarChartGrouped to visualize the results

```scala
import spire.algebra.CRing

import axle.visualize.BarChartGrouped
import axle.visualize.Color._
import axle.syntax.talliable.talliableOps

implicit val ringInt: CRing[Int] = spire.implicits.IntAlgebra
// ringInt: CRing[Int] = spire.std.IntAlgebra@4fb1f0d5

val colors = List(black, red, blue, yellow, green)
// colors: List[axle.visualize.Color] = List(
//   Color(r = 0, g = 0, b = 0),
//   Color(r = 255, g = 0, b = 0),
//   Color(r = 0, g = 0, b = 255),
//   Color(r = 255, g = 255, b = 0),
//   Color(r = 0, g = 255, b = 0)
// )

val chart = BarChartGrouped[PokerHandCategory, Int, Int, Map[(PokerHandCategory, Int), Int], String](
  () => data.tally.withDefaultValue(0),
  title = Some("Poker Hands"),
  drawKey = false,
  yAxisLabel = Some("instances of category by hand size (1000 trials each)"),
  colorOf = (cat: PokerHandCategory, handSize: Int) => colors( (handSize - 5) % colors.size),
  hoverOf = (cat: PokerHandCategory, handSize: Int) => Some(s"${cat.show} from $handSize")
)
// chart: BarChartGrouped[PokerHandCategory, Int, Int, Map[(PokerHandCategory, Int), Int], String] = BarChartGrouped(
//   dataFn = <function0>,
//   drawKey = false,
//   width = 700,
//   height = 600,
//   border = 50,
//   barWidthPercent = 0.8,
//   keyLeftPadding = 20,
//   keyTopPadding = 50,
//   keyWidth = 80,
//   title = Some(value = "Poker Hands"),
//   keyTitle = None,
//   normalFontName = "Courier New",
//   normalFontSize = 12,
//   titleFontName = "Palatino",
//   titleFontSize = 20,
//   xAxis = None,
//   xAxisLabel = None,
//   yAxisLabel = Some(
//     value = "instances of category by hand size (1000 trials each)"
//   ),
//   labelAngle = Some(
//     value = UnittedQuantity(
//       magnitude = 36.0,
//       unit = UnitOfMeasurement(
//         name = "degree",
//         symbol = "°",
//         wikipediaUrl = Some(
//           value = "http://en.wikipedia.org/wiki/Degree_(angle)"
//         )
//       )
//     )
//   ),
//   colorOf = <function2>,
//   hoverOf = <function2>,
//   linkOf = axle.visualize.BarChartGrouped$$$Lambda$10416/0x0000000802722a38@33e64f42
// )
```

Render as SVG file

```scala
import axle.web._
import cats.effect._

chart.svg[IO]("poker_hands.svg").unsafeRunSync()
```

![poker hands](/tutorial/images/poker_hands.svg)

### Texas Hold 'Em Poker

As a game of "imperfect information", poker introduces the concept of Information Set.

```scala
import axle.game._
import axle.game.poker._

val p1 = Player("P1", "Player 1")
// p1: Player = Player(id = "P1", description = "Player 1")
val p2 = Player("P2", "Player 2")
// p2: Player = Player(id = "P2", description = "Player 2")

val game = Poker(Vector(p1, p2))
// game: Poker = Poker(
//   betters = Vector(
//     Player(id = "P1", description = "Player 1"),
//     Player(id = "P2", description = "Player 2")
//   )
// )
```

Create a `writer` for each player that prefixes the player id to all output.

```scala
import cats.effect.IO
import axle.IO.printMultiLinePrefixed

val playerToWriter: Map[Player, String => IO[Unit]] =
  evGame.players(game).map { player =>
    player -> (printMultiLinePrefixed[IO](player.id) _)
  } toMap
// playerToWriter: Map[Player, String => IO[Unit]] = Map(
//   Player(id = "D", description = "Dealer") -> <function1>,
//   Player(id = "P1", description = "Player 1") -> <function1>,
//   Player(id = "P2", description = "Player 2") -> <function1>
// )
```

Use a uniform distribution on moves as the demo strategy:

```scala
import axle.probability._
import spire.math.Rational

val randomMove =
  (state: PokerStateMasked) =>
    ConditionalProbabilityTable.uniform[PokerMove, Rational](evGame.moves(game, state))
// randomMove: PokerStateMasked => ConditionalProbabilityTable[PokerMove, Rational] = <function1>
```

Wrap the strategies in the calls to `writer` that log the transitions from state to state.

```scala
val strategies: Player => PokerStateMasked => IO[ConditionalProbabilityTable[PokerMove, Rational]] = 
  (player: Player) =>
    (state: PokerStateMasked) =>
      for {
        _ <- playerToWriter(player)(evGameIO.displayStateTo(game, state, player))
        move <- randomMove.andThen( m => IO { m })(state)
      } yield move
// strategies: Player => PokerStateMasked => IO[ConditionalProbabilityTable[PokerMove, Rational]] = <function1>
```

Play the game -- compute the end state from the start state.

```scala
import spire.random.Generator.rng

val endState = play(game, strategies, evGame.startState(game), rng).unsafeRunSync()
// D> To: You
// D> Current bet: 0
// D> Pot: 0
// D> Shared: 
// D> 
// D> P1:  hand -- in for $--, $100 remaining
// D> P2:  hand -- in for $--, $100 remaining
// P1> To: You
// P1> Current bet: 2
// P1> Pot: 3
// P1> Shared: 
// P1> 
// P1> P1:  hand 7♡ A♠ in for $1, $99 remaining
// P1> P2:  hand -- in for $2, $98 remaining
// P2> To: You
// P2> Current bet: 36
// P2> Pot: 38
// P2> Shared: 
// P2> 
// P2> P1:  hand -- in for $36, $64 remaining
// P2> P2:  hand T♢ 6♣ in for $2, $98 remaining
// P1> To: You
// P1> Current bet: 62
// P1> Pot: 98
// P1> Shared: 
// P1> 
// P1> P1:  hand 7♡ A♠ in for $36, $64 remaining
// P1> P2:  hand -- in for $62, $38 remaining
// P2> To: You
// P2> Current bet: 91
// P2> Pot: 153
// P2> Shared: 
// P2> 
// P2> P1:  hand -- in for $91, $9 remaining
// P2> P2:  hand T♢ 6♣ in for $62, $38 remaining
// P1> To: You
// P1> Current bet: 95
// P1> Pot: 186
// P1> Shared: 
// P1> 
// P1> P1:  hand 7♡ A♠ in for $91, $9 remaining
// P1> P2:  hand -- in for $95, $5 remaining
// P2> To: You
// P2> Current bet: 99
// P2> Pot: 194
// P2> Shared: 
// P2> 
// P2> P1:  hand -- in for $99, $1 remaining
// P2> P2:  hand T♢ 6♣ in for $95, $5 remaining
// D> To: You
// D> Current bet: 99
// D> Pot: 194
// D> Shared: 
// D> 
// D> P1:  hand -- in for $99, $1 remaining
// D> P2:  hand -- out, $5 remaining
// endState: PokerState = PokerState(
//   moverFn = axle.game.poker.package$$anon$1$$Lambda$10728/0x00000008027fbca8@21d1a43d,
//   deck = Deck(
//     cards = List(
//       Card(
//         rank = axle.game.cards.Queen$@28fa0e1b,
//         suit = axle.game.cards.Diamonds$@1b2b6c89
//       ),
//       Card(
//         rank = axle.game.cards.R7$@39256edb,
//         suit = axle.game.cards.Spades$@49a85f6c
//       ),
//       Card(
//         rank = axle.game.cards.Queen$@28fa0e1b,
//         suit = axle.game.cards.Spades$@49a85f6c
//       ),
//       Card(
//         rank = axle.game.cards.R9$@32fd15ac,
//         suit = axle.game.cards.Clubs$@6f3352ab
//       ),
//       Card(
//         rank = axle.game.cards.Ace$@39b2a0b5,
//         suit = axle.game.cards.Diamonds$@1b2b6c89
//       ),
//       Card(
//         rank = axle.game.cards.R2$@717d2fe2,
//         suit = axle.game.cards.Spades$@49a85f6c
//       ),
//       Card(
//         rank = axle.game.cards.R5$@2a672e2e,
//         suit = axle.game.cards.Clubs$@6f3352ab
//       ),
//       Card(
//         rank = axle.game.cards.R5$@2a672e2e,
//         suit = axle.game.cards.Diamonds$@1b2b6c89
//       ),
//       Card(
//         rank = axle.game.cards.Jack$@4ad6f420,
//         suit = axle.game.cards.Hearts$@5da9b90f
//       ),
//       Card(
//         rank = axle.game.cards.R8$@65cac508,
//         suit = axle.game.cards.Spades$@49a85f6c
//       ),
//       Card(
//         rank = axle.game.cards.R10$@b39bf3c,
//         suit = axle.game.cards.Clubs$@6f3352ab
//       ),
// ...
```

Display outcome to each player

```scala
val outcome = evGame.mover(game, endState).swap.toOption.get
// outcome: PokerOutcome = PokerOutcome(
//   winner = Some(value = Player(id = "P1", description = "Player 1")),
//   hand = None
// )

evGame.players(game).foreach { player =>
  playerToWriter(player)(evGameIO.displayOutcomeTo(game, outcome, player)).unsafeRunSync()
}
// D> Winner: Player 1
// D> Hand  : not shown
// P1> Winner: Player 1
// P1> Hand  : not shown
// P2> Winner: Player 1
// P2> Hand  : not shown
```

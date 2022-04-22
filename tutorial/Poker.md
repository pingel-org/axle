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
// res0: String = "9♢ Q♡ K♠ K♡ K♣"
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala
val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted
// hands: IndexedSeq[PokerHand] = Vector(
//   PokerHand(
//     cards = Vector(
//       Card(
//         rank = axle.game.cards.Queen$@d7d419a,
//         suit = axle.game.cards.Spades$@3ede42b7
//       ),
//       Card(
//         rank = axle.game.cards.R8$@18cbd37e,
//         suit = axle.game.cards.Hearts$@7f9bb0c
//       ),
//       Card(
//         rank = axle.game.cards.R10$@66b0587e,
//         suit = axle.game.cards.Clubs$@433e3685
//       ),
//       Card(
//         rank = axle.game.cards.R2$@4d31dc4b,
//         suit = axle.game.cards.Diamonds$@50a4cdeb
//       ),
//       Card(
//         rank = axle.game.cards.R2$@4d31dc4b,
//         suit = axle.game.cards.Spades$@3ede42b7
//       )
//     )
//   ),
//   PokerHand(
//     cards = Vector(
//       Card(
//         rank = axle.game.cards.R8$@18cbd37e,
//         suit = axle.game.cards.Spades$@3ede42b7
//       ),
//       Card(
//         rank = axle.game.cards.R10$@66b0587e,
//         suit = axle.game.cards.Spades$@3ede42b7
//       ),
//       Card(
//         rank = axle.game.cards.King$@48e305d5,
//         suit = axle.game.cards.Clubs$@433e3685
//       ),
//       Card(
//         rank = axle.game.cards.R4$@b27a6b0,
//         suit = axle.game.cards.Diamonds$@50a4cdeb
//       ),
//       Card(
//         rank = axle.game.cards.R4$@b27a6b0,
//         suit = axle.game.cards.Clubs$@433e3685
//       )
//     )
//   ),
// ...

hands.map({ hand => hand.show + "  " + hand.description }).mkString("\n")
// res1: String = """2♠ 2♢ 8♡ T♣ Q♠  pair of 2
// 4♢ 4♣ 8♠ T♠ K♣  pair of 4
// 5♠ 5♡ 7♣ 9♡ T♣  pair of 5
// 6♠ 6♣ Q♠ K♠ A♢  pair of 6
// 6♡ 7♠ 7♣ 8♡ T♢  pair of 7
// T♠ T♣ J♢ Q♠ K♢  pair of T
// 5♣ T♢ T♣ K♠ A♠  pair of T
// 9♠ J♡ J♢ K♢ A♡  pair of J
// 9♣ J♠ J♣ K♡ A♡  pair of J
// T♡ J♡ J♣ K♠ A♠  pair of J
// 8♠ 9♣ Q♠ Q♡ K♢  pair of Q
// 9♠ J♢ Q♠ Q♡ A♣  pair of Q
// 7♢ 8♢ J♢ K♡ K♣  pair of K
// 2♡ 2♣ 3♠ 3♣ A♣  two pair 3 and 2
// 5♠ 5♡ 6♠ 6♡ Q♣  two pair 6 and 5
// 4♠ 4♣ 8♡ 8♢ J♡  two pair 8 and 4
// Q♡ Q♣ K♠ K♢ A♣  two pair K and Q
// 9♠ 9♢ Q♡ A♠ A♡  two pair A and 9
// J♢ Q♠ K♡ K♢ K♣  three of a kind of K
// 2♣ 3♣ 5♣ T♣ K♣  flush in ♣"""
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
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.ThreeOfAKind$@1a11ef92, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.TwoPair$@25e8c4eb, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.Pair$@a1e872, 5),
//   (axle.game.poker.High$@2a537300, 5),
//   (axle.game.poker.High$@2a537300, 5),
// ...
```

BarChartGrouped to visualize the results

```scala
import spire.algebra.CRing

import axle.visualize.BarChartGrouped
import axle.visualize.Color._
import axle.syntax.talliable.talliableOps

implicit val ringInt: CRing[Int] = spire.implicits.IntAlgebra
// ringInt: CRing[Int] = spire.std.IntAlgebra@23640a5b

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
//   linkOf = axle.visualize.BarChartGrouped$$$Lambda$10434/0x000000080270e5a8@2b2049d8
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
// P1> P1:  hand Q♡ 2♡ in for $1, $99 remaining
// P1> P2:  hand -- in for $2, $98 remaining
// P2> To: You
// P2> Current bet: 62
// P2> Pot: 64
// P2> Shared: 
// P2> 
// P2> P1:  hand -- in for $62, $38 remaining
// P2> P2:  hand K♣ J♢ in for $2, $98 remaining
// P1> To: You
// P1> Current bet: 67
// P1> Pot: 129
// P1> Shared: 
// P1> 
// P1> P1:  hand Q♡ 2♡ in for $62, $38 remaining
// P1> P2:  hand -- in for $67, $33 remaining
// P2> To: You
// P2> Current bet: 79
// P2> Pot: 146
// P2> Shared: 
// P2> 
// P2> P1:  hand -- in for $79, $21 remaining
// P2> P2:  hand K♣ J♢ in for $67, $33 remaining
// P1> To: You
// P1> Current bet: 100
// P1> Pot: 179
// P1> Shared: 
// P1> 
// P1> P1:  hand Q♡ 2♡ in for $79, $21 remaining
// P1> P2:  hand -- in for $100, $0 remaining
// D> To: You
// D> Current bet: 100
// D> Pot: 200
// D> Shared: 
// D> 
// D> P1:  hand -- in for $100, $0 remaining
// D> P2:  hand -- in for $100, $0 remaining
// P1> To: You
// P1> Current bet: 0
// P1> Pot: 200
// P1> Shared: A♡ 3♡ J♡
// P1> 
// P1> P1:  hand Q♡ 2♡ in for $--, $0 remaining
// P1> P2:  hand -- in for $--, $0 remaining
// D> To: You
// D> Current bet: 0
// D> Pot: 200
// D> Shared: A♡ 3♡ J♡
// D> 
// D> P1:  hand -- out, $0 remaining
// D> P2:  hand -- in for $--, $0 remaining
// endState: PokerState = PokerState(
//   moverFn = axle.game.poker.package$$anon$1$$Lambda$10751/0x0000000802807c10@5a7436e2,
//   deck = Deck(
//     cards = List(
//       Card(
//         rank = axle.game.cards.R8$@18cbd37e,
//         suit = axle.game.cards.Spades$@3ede42b7
//       ),
//       Card(
//         rank = axle.game.cards.R7$@4b5e7cab,
//         suit = axle.game.cards.Hearts$@7f9bb0c
//       ),
//       Card(
//         rank = axle.game.cards.R4$@b27a6b0,
//         suit = axle.game.cards.Diamonds$@50a4cdeb
//       ),
//       Card(
//         rank = axle.game.cards.R9$@5948a567,
//         suit = axle.game.cards.Clubs$@433e3685
//       ),
//       Card(
//         rank = axle.game.cards.R6$@13234499,
//         suit = axle.game.cards.Diamonds$@50a4cdeb
//       ),
//       Card(
//         rank = axle.game.cards.R2$@4d31dc4b,
//         suit = axle.game.cards.Spades$@3ede42b7
//       ),
//       Card(
//         rank = axle.game.cards.R3$@20484fe8,
//         suit = axle.game.cards.Clubs$@433e3685
//       ),
//       Card(
//         rank = axle.game.cards.R6$@13234499,
//         suit = axle.game.cards.Clubs$@433e3685
//       ),
//       Card(
//         rank = axle.game.cards.R4$@b27a6b0,
//         suit = axle.game.cards.Clubs$@433e3685
//       ),
//       Card(
//         rank = axle.game.cards.R9$@5948a567,
//         suit = axle.game.cards.Hearts$@7f9bb0c
//       ),
//       Card(
//         rank = axle.game.cards.R2$@4d31dc4b,
//         suit = axle.game.cards.Diamonds$@50a4cdeb
//       ),
// ...
```

Display outcome to each player

```scala
val outcome = evGame.mover(game, endState).swap.toOption.get
// outcome: PokerOutcome = PokerOutcome(
//   winner = Some(value = Player(id = "P2", description = "Player 2")),
//   hand = None
// )

evGame.players(game).foreach { player =>
  playerToWriter(player)(evGameIO.displayOutcomeTo(game, outcome, player)).unsafeRunSync()
}
// D> Winner: Player 2
// D> Hand  : not shown
// P1> Winner: Player 2
// P1> Hand  : not shown
// P2> Winner: Player 2
// P2> Hand  : not shown
```

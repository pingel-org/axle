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
// res0: String = "6♡ 8♣ 9♡ J♣ A♠"
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala
val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted
// hands: IndexedSeq[PokerHand] = Vector(
//   PokerHand(
//     cards = Vector(
//       Card(
//         rank = axle.game.cards.R5$@45eb4a20,
//         suit = axle.game.cards.Diamonds$@30400a37
//       ),
//       Card(
//         rank = axle.game.cards.R10$@73016d8,
//         suit = axle.game.cards.Hearts$@2c992469
//       ),
//       Card(
//         rank = axle.game.cards.R8$@35aec0be,
//         suit = axle.game.cards.Spades$@2c80ac6
//       ),
//       Card(
//         rank = axle.game.cards.Ace$@3550a1a,
//         suit = axle.game.cards.Clubs$@35dd135
//       ),
//       Card(
//         rank = axle.game.cards.R4$@23e51716,
//         suit = axle.game.cards.Hearts$@2c992469
//       )
//     )
//   ),
//   PokerHand(
//     cards = Vector(
//       Card(
//         rank = axle.game.cards.R7$@4305872e,
//         suit = axle.game.cards.Spades$@2c80ac6
//       ),
//       Card(
//         rank = axle.game.cards.Jack$@66395,
//         suit = axle.game.cards.Hearts$@2c992469
//       ),
//       Card(
//         rank = axle.game.cards.R6$@6559b590,
//         suit = axle.game.cards.Clubs$@35dd135
//       ),
//       Card(
//         rank = axle.game.cards.Ace$@3550a1a,
//         suit = axle.game.cards.Spades$@2c80ac6
//       ),
//       Card(
//         rank = axle.game.cards.R9$@41a09111,
//         suit = axle.game.cards.Hearts$@2c992469
//       )
//     )
//   ),
// ...

hands.map({ hand => hand.show + "  " + hand.description }).mkString("\n")
// res1: String = """4♡ 5♢ 8♠ T♡ A♣  high A high
// 6♣ 7♠ 9♡ J♡ A♠  high A high
// 7♠ 9♢ T♣ J♠ A♠  high A high
// 8♠ T♢ Q♣ K♢ A♡  high A high
// 3♠ 3♢ 9♡ K♢ A♢  pair of 3
// 5♡ 5♣ 6♢ J♠ K♣  pair of 5
// 6♡ 6♣ 7♠ 9♡ J♠  pair of 6
// 6♡ 6♢ T♠ J♢ K♣  pair of 6
// 6♠ 6♡ 9♣ J♣ A♠  pair of 6
// 5♠ 7♠ 7♣ 9♢ K♢  pair of 7
// 5♢ 6♢ 8♢ 9♡ 9♢  pair of 9
// 9♡ 9♢ T♢ J♢ A♠  pair of 9
// 5♣ 7♠ 9♣ T♠ T♡  pair of T
// 6♡ 8♣ T♡ Q♡ Q♣  pair of Q
// 9♡ J♡ K♠ K♣ A♠  pair of K
// 9♣ J♢ Q♡ A♡ A♣  pair of A
// 3♡ 3♢ 8♡ 8♢ A♢  two pair 8 and 3
// 2♡ 2♣ T♠ Q♡ Q♢  two pair Q and 2
// 6♡ 6♣ 9♣ K♠ K♡  two pair K and 6
// 3♡ 3♢ 3♣ 5♡ 5♣  full house 3 over 5"""
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
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.TwoPair$@51cba237, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.TwoPair$@51cba237, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.Pair$@6639ad3d, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.ThreeOfAKind$@55d7936a, 5),
//   (axle.game.poker.High$@20cac23f, 5),
//   (axle.game.poker.High$@20cac23f, 5),
// ...
```

BarChartGrouped to visualize the results

```scala
import spire.algebra.CRing

import axle.visualize.BarChartGrouped
import axle.visualize.Color._
import axle.syntax.talliable.talliableOps

implicit val ringInt: CRing[Int] = spire.implicits.IntAlgebra
// ringInt: CRing[Int] = spire.std.IntAlgebra@4438df1b

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
//   linkOf = axle.visualize.BarChartGrouped$$$Lambda$7210/0x0000000802278f50@51a068ad
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
// P1> P1:  hand 9♡ 8♢ in for $1, $99 remaining
// P1> P2:  hand -- in for $2, $98 remaining
// P2> To: You
// P2> Current bet: 10
// P2> Pot: 12
// P2> Shared: 
// P2> 
// P2> P1:  hand -- in for $10, $90 remaining
// P2> P2:  hand 3♣ 9♣ in for $2, $98 remaining
// P1> To: You
// P1> Current bet: 61
// P1> Pot: 71
// P1> Shared: 
// P1> 
// P1> P1:  hand 9♡ 8♢ in for $10, $90 remaining
// P1> P2:  hand -- in for $61, $39 remaining
// P2> To: You
// P2> Current bet: 69
// P2> Pot: 130
// P2> Shared: 
// P2> 
// P2> P1:  hand -- in for $69, $31 remaining
// P2> P2:  hand 3♣ 9♣ in for $61, $39 remaining
// P1> To: You
// P1> Current bet: 100
// P1> Pot: 169
// P1> Shared: 
// P1> 
// P1> P1:  hand 9♡ 8♢ in for $69, $31 remaining
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
// P1> Shared: J♣ 2♠ 2♡
// P1> 
// P1> P1:  hand 9♡ 8♢ in for $--, $0 remaining
// P1> P2:  hand -- in for $--, $0 remaining
// P2> To: You
// P2> Current bet: 0
// P2> Pot: 200
// P2> Shared: J♣ 2♠ 2♡
// P2> 
// P2> P1:  hand -- in for $0, $0 remaining
// P2> P2:  hand 3♣ 9♣ in for $--, $0 remaining
// D> To: You
// D> Current bet: 0
// D> Pot: 200
// D> Shared: J♣ 2♠ 2♡
// D> 
// D> P1:  hand -- in for $0, $0 remaining
// D> P2:  hand -- out, $0 remaining
// endState: PokerState = PokerState(
//   moverFn = axle.game.poker.package$$anon$1$$Lambda$7526/0x0000000802354530@7c299519,
//   deck = Deck(
//     cards = List(
//       Card(
//         rank = axle.game.cards.King$@14c5e299,
//         suit = axle.game.cards.Hearts$@2c992469
//       ),
//       Card(
//         rank = axle.game.cards.R4$@23e51716,
//         suit = axle.game.cards.Diamonds$@30400a37
//       ),
//       Card(
//         rank = axle.game.cards.R3$@737fe3a,
//         suit = axle.game.cards.Spades$@2c80ac6
//       ),
//       Card(
//         rank = axle.game.cards.R10$@73016d8,
//         suit = axle.game.cards.Spades$@2c80ac6
//       ),
//       Card(
//         rank = axle.game.cards.R3$@737fe3a,
//         suit = axle.game.cards.Diamonds$@30400a37
//       ),
//       Card(
//         rank = axle.game.cards.R7$@4305872e,
//         suit = axle.game.cards.Diamonds$@30400a37
//       ),
//       Card(
//         rank = axle.game.cards.Jack$@66395,
//         suit = axle.game.cards.Hearts$@2c992469
//       ),
//       Card(
//         rank = axle.game.cards.R6$@6559b590,
//         suit = axle.game.cards.Diamonds$@30400a37
//       ),
//       Card(
//         rank = axle.game.cards.R7$@4305872e,
//         suit = axle.game.cards.Spades$@2c80ac6
//       ),
//       Card(
//         rank = axle.game.cards.R10$@73016d8,
//         suit = axle.game.cards.Clubs$@35dd135
//       ),
//       Card(
//         rank = axle.game.cards.Jack$@66395,
//         suit = axle.game.cards.Spades$@2c80ac6
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

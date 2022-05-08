# Game Theory

Framework for expressing arbitrary games.

## Monty Hall

See the Wikipedia page on the [Monty Hall problem](https://en.wikipedia.org/wiki/Monty_Hall_problem)

The `axle.game.OldMontyHall` object contains a model of the rules of the game.

```scala mdoc:silent
import spire.math.Rational

import axle.probability._
import axle.game.OldMontyHall._
```

The models supports querying the chance of winning given the odds that the
player switches his or her initial choice.

At one extreme, the odds of winning given that the other door is always chosen:

```scala mdoc
chanceOfWinning(Rational(1))
```

At the other extreme, the player always sticks with the initial choice.

```scala mdoc
chanceOfWinning(Rational(0))
```

The newer `axl.game.montyhall._` package uses `axle.game` typeclasses to model the game:

```scala mdoc:silent
import axle.game._
import axle.game.montyhall._

val game = MontyHall()
```

Create a `writer` for each player that prefixes the player id to all output.

```scala mdoc:silent
import cats.effect.IO
import axle.IO.printMultiLinePrefixed

val playerToWriter: Map[Player, String => IO[Unit]] =
  evGame.players(game).map { player =>
    player -> (printMultiLinePrefixed[IO](player.id) _)
  } toMap
```

Use a uniform distribution on moves as the demo strategy:

```scala mdoc:silent
val randomMove: MontyHallState => ConditionalProbabilityTable[MontyHallMove,
 Rational] =
  (state: MontyHallState) =>
    ConditionalProbabilityTable.uniform[MontyHallMove, Rational](evGame.moves(game, state))
```

Wrap the strategies in the calls to `writer` that log the transitions from state to state.

```scala mdoc:silent
val strategies: Player => MontyHallState => IO[ConditionalProbabilityTable[MontyHallMove, Rational]] = 
  (player: Player) =>
    (state: MontyHallState) =>
      for {
        _ <- playerToWriter(player)(evGameIO.displayStateTo(game, state, player))
        move <- randomMove.andThen( m => IO { m })(state)
      } yield move
```

Play the game -- compute the end state from the start state.

```scala mdoc
import spire.random.Generator.rng

val endState: MontyHallState =
  play(game, strategies, evGame.startState(game), rng).unsafeRunSync()
```

Display outcome to each player

```scala mdoc
val outcome: MontyHallOutcome =
  evGame.mover(game, endState).swap.toOption.get

evGame.players(game).foreach { player =>
  playerToWriter(player)(evGameIO.displayOutcomeTo(game, outcome, player)).unsafeRunSync()
}
```

## Poker

An N-Player, Imperfect Information, Zero-sum game

### Poker Analytics Example

The `axle.game.cards` package models decks, cards, ranks, suits, and ordering.

Define a function that takes the hand size and returns the best 5-card hand

```scala mdoc:silent:reset
import cats.implicits._
import cats.Order.catsKernelOrderingForOrder

import axle.game.cards.Deck
import axle.game.poker.PokerHand

def winnerFromHandSize(handSize: Int) =
  Deck().cards.take(handSize).combinations(5).map(cs => PokerHand(cs.toVector)).toList.max
```

```scala mdoc
winnerFromHandSize(7).show
```

20 simulated 5-card hands made of 7-card hands.  Sorted.

```scala mdoc:silent
val hands = (1 to 20).map(n => winnerFromHandSize(7)).sorted
```

```scala mdoc
hands.map({ hand => hand.show + "  " + hand.description }).mkString("\n")
```

Record 1000 simulated hands for each drawn hand size from 5 to 9

```scala mdoc:silent
import axle.game.poker.PokerHandCategory

val data: IndexedSeq[(PokerHandCategory, Int)] =
  for {
    handSize <- 5 to 9
    trial <- 1 to 1000
  } yield (winnerFromHandSize(handSize).category, handSize)
```

BarChartGrouped to visualize the results

```scala mdoc:silent
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

chart.svg[IO]("@DOCWD@/images/poker_hands.svg").unsafeRunSync()
```

![poker hands](/images/poker_hands.svg)

### Playing Texas Hold 'Em Poker

As a game of "imperfect information", poker introduces the concept of Information Set.

```scala mdoc:silent
import axle.game._
import axle.game.poker._

val p1 = Player("P1", "Player 1")
val p2 = Player("P2", "Player 2")

val game = Poker(Vector(p1, p2))
```

Create a `writer` for each player that prefixes the player id to all output.

```scala mdoc:silent
import cats.effect.IO
import axle.IO.printMultiLinePrefixed

val playerToWriter: Map[Player, String => IO[Unit]] =
  evGame.players(game).map { player =>
    player -> (printMultiLinePrefixed[IO](player.id) _)
  } toMap
```

Use a uniform distribution on moves as the demo strategy:

```scala mdoc:silent
import axle.probability._
import spire.math.Rational

val randomMove =
  (state: PokerStateMasked) =>
    ConditionalProbabilityTable.uniform[PokerMove, Rational](evGame.moves(game, state))
```

Wrap the strategies in the calls to `writer` that log the transitions from state to state.

```scala mdoc:silent
val strategies: Player => PokerStateMasked => IO[ConditionalProbabilityTable[PokerMove, Rational]] = 
  (player: Player) =>
    (state: PokerStateMasked) =>
      for {
        _ <- playerToWriter(player)(evGameIO.displayStateTo(game, state, player))
        move <- randomMove.andThen( m => IO { m })(state)
      } yield move
```

Play the game -- compute the end state from the start state.

```scala mdoc
import spire.random.Generator.rng

val endState = play(game, strategies, evGame.startState(game), rng).unsafeRunSync()
```

Display outcome to each player

```scala mdoc
val outcome = evGame.mover(game, endState).swap.toOption.get

evGame.players(game).foreach { player =>
  playerToWriter(player)(evGameIO.displayOutcomeTo(game, outcome, player)).unsafeRunSync()
}
```

## Prisoner's Dilemma

See the Wikipedia page on the [Prisoner's Dilemma](https://en.wikipedia.org/wiki/Prisoner%27s_dilemma)

The `axl.game.prisoner._` package uses `axle.game` typeclasses to model the game:

```scala mdoc:silent:reset
import axle.game._
import axle.game.prisoner._

val p1 = Player("P1", "Player 1")
val p2 = Player("P2", "Player 2")

val game = PrisonersDilemma(p1, p2)
```

Create a `writer` for each player that prefixes the player id to all output.

```scala mdoc:silent
import cats.effect.IO
import axle.IO.printMultiLinePrefixed

val playerToWriter: Map[Player, String => IO[Unit]] =
  evGame.players(game).map { player =>
    player -> (printMultiLinePrefixed[IO](player.id) _)
  } toMap
```

Use a uniform distribution on moves as the demo strategy:

```scala mdoc:silent
import axle.probability._
import spire.math.Rational

val randomMove =
  (state: PrisonersDilemmaState) =>
    ConditionalProbabilityTable.uniform[PrisonersDilemmaMove, Rational](evGame.moves(game, state))
```

Wrap the strategies in the calls to `writer` that log the transitions from state to state.

```scala mdoc:silent
val strategies: Player => PrisonersDilemmaState => IO[ConditionalProbabilityTable[PrisonersDilemmaMove, Rational]] = 
  (player: Player) =>
    (state: PrisonersDilemmaState) =>
      for {
        _ <- playerToWriter(player)(evGameIO.displayStateTo(game, state, player))
        move <- randomMove.andThen( m => IO { m })(state)
      } yield move
```

Play the game -- compute the end state from the start state.

```scala mdoc
import spire.random.Generator.rng

val endState = play(game, strategies, evGame.startState(game), rng).unsafeRunSync()
```

Display outcome to each player

```scala mdoc
val outcome = evGame.mover(game, endState).swap.toOption.get

evGame.players(game).foreach { player =>
  playerToWriter(player)(evGameIO.displayOutcomeTo(game, outcome, player)).unsafeRunSync()
}
```

## Tic-Tac-Toe

A Perfect Information, Zero-sum game

### Playing Tic-Tac-Toe

```scala mdoc:silent:reset
import axle.game._
import axle.game.ttt._

val x = Player("X", "Player X")
val o = Player("O", "Player O")

val game = TicTacToe(3, x, o)
```

Create a `writer` for each player that prefixes the player id to all output.

```scala mdoc:silent
import cats.effect.IO
import axle.IO.printMultiLinePrefixed

val playerToWriter: Map[Player, String => IO[Unit]] =
  evGame.players(game).map { player =>
    player -> (printMultiLinePrefixed[IO](player.id) _)
  } toMap
```

Use a uniform distribution on moves as the demo strategy:

```scala mdoc:silent
import axle.probability._
import spire.math.Rational

val randomMove =
  (state: TicTacToeState) =>
    ConditionalProbabilityTable.uniform[TicTacToeMove, Rational](evGame.moves(game, state))
```

Wrap the strategies in the calls to `writer` that log the transitions from state to state.

```scala mdoc:silent
val strategies: Player => TicTacToeState => IO[ConditionalProbabilityTable[TicTacToeMove, Rational]] = 
  (player: Player) =>
    (state: TicTacToeState) =>
      for {
        _ <- playerToWriter(player)(evGameIO.displayStateTo(game, state, player))
        move <- randomMove.andThen( m => IO { m })(state)
      } yield move
```

Play the game -- compute the end state from the start state.

```scala mdoc
import spire.random.Generator.rng

val endState = play(game, strategies, evGame.startState(game), rng).unsafeRunSync()
```

Display outcome to each player

```scala mdoc
val outcome = evGame.mover(game, endState).swap.toOption.get

evGame.players(game).foreach { player =>
  playerToWriter(player)(evGameIO.displayOutcomeTo(game, outcome, player)).unsafeRunSync()
}
```

## Future Work

### Missing functionality

* Remove moveStateStream

* For one game (probably Poker)
  * Record witnessed and unwitnessed history `Seq[(M, S)]` in `State`
  * Display to user in interactiveMove
    * `val mm = evGame.maskMove(game, move, mover, observer)`
    * `evGameIO.displayMoveTo(game, mm, mover, observer)`
  * Then generalize and pull into framework

### Motivating Examples

* Generalize `OldMontyHall.chanceOfWinning`

* GuessRiffle.md
  * Walk through game
  * Plot distribution of sum(entropy) for both strategies
  * Plot entropy by turn # for each strategy
  * Plot simulated score distribution for each strategy

* GuessRiffleSpec: use `moveFromRandomState`

* Gerrymandering sensitivity

* "You split, I choose" as game

### Deeper changes to axle.game

* `aiMover.unmask` prevents `MontyHallSpec` "AI vs. AI game produces moveStateStream" from working
  * will be an issue for all non-perfect information

* Identify all uses of `spire.random.Generator` (and other random value generation)

* See uses of `seed` in `GuessRiffleProperties`

* Eliminate entropy consumption of `rng` side-effect (eg `applyMove(Riffle())`)
  * `Chance` should be its own player
  * Consider whether `PM` should be a part of `Strategy` type (`MS => PM[M, V]`)
    * More abstractly, more many intents and purposes, all we are about is that resolving PM to M consumes entropy
    * In which cases should the `PM` be retained?
  * Each N bits consumed during `Riffle()` is its own move
  * Chance moves consume `UnittedQuantity[Information, N]`

* `perceive` could return a lower-entropy probability model
  * Perhaps in exchange for a given amount of energy
  * Or ask for a 0-entropy model and be told how expensive that was

* Game theory axioms (Nash, etc)

* `axle.game`: `Observable[T]`

### Hygeine

* performance benchmark

* Replace `axle.game.moveFromRandomState.mapToProb`

* Clean up `axle.game.playWithIntroAndOutcomes`

* The references to `movesMap` in `MoveFromRandomStateSpec.scala` illustrate a need for a cleaner way to create a hard-coded strategy -- which could just be in the form of a couple utility functions from `movesMap` to the data needed by `evGame.{moves,applyMove}` and `rm` strategy

* Generalize `ConditionalProbabilityTable.uniform` into typeclass

* Simplify `GuessRiffleProperties` (especially second property)
* stateStreamMap only used in GuessRiffleProperties -- stop using chain?
* stateStrategyMoveStream only used in GuessRiffleProperties

* `Game.players` should be a part of GameState (or take it as an argument)?  Will wait for pressing use case.

### Game Theory and Examples

* Game Theory: information sets, equilibria

* Factor `axle.game.moveFromRandomState` in terms of a random walk on a graph.
  * See "TODO scale mass down"
  * Compare to Brownian motion, Random walk, Ito process, ...
  * Provide some axioms
    * no outgoing with path in from non-zero mass monotonically increases
    * no incoming with path out monotonically decreases
  * possibly provide a version for acyclic graphs
* Iterative game playing algorithm is intractible, but shares intent with sequential monte carlo
* Think about Information Theory's "Omega" vis-a-vis Sequential Monte Carlo
* Improve `axle.stats.rationalProbabilityDist` as probabilities become smaller
* SimpsonsParadox.md
* Axioms of partial differentiation
  * [Plotkin Partial Differentiation](https://math.ucr.edu/home/baez/mathematical/ACTUCR/Plotkin_Partial_Differentiation.pdf)
* Conal Elliott: Efficient automatic differentiation made easy via category theory
* Max bet for Poker
* syntax for `Game` typeclass

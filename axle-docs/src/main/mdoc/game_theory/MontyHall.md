# Monty Hall

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

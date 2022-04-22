---
layout: page
title: Monty Hall
permalink: /tutorial/monty_hall/
---

See the Wikipedia page on the [Monty Hall problem](https://en.wikipedia.org/wiki/Monty_Hall_problem)

The `axle.game.OldMontyHall` object contains a model of the rules of the game.

```scala
import spire.math.Rational

import axle.probability._
import axle.game.OldMontyHall._
```

The models supports querying the chance of winning given the odds that the
player switches his or her initial choice.

At one extreme, the odds of winning given that the other door is always chosen:

```scala
chanceOfWinning(Rational(1))
// res0: Rational = 2/3
```

At the other extreme, the player always sticks with the initial choice.

```scala
chanceOfWinning(Rational(0))
// res1: Rational = 1/3
```

The newer `axl.game.montyhall._` package uses `axle.game` typeclasses to model the game:

```scala
import axle.game._
import axle.game.montyhall._

val game = MontyHall()
// game: MontyHall = MontyHall()
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
//   Player(id = "C", description = "Contestant") -> <function1>,
//   Player(id = "M", description = "Monty Hall") -> <function1>
// )
```

Use a uniform distribution on moves as the demo strategy:

```scala
val randomMove =
  (state: MontyHallState) =>
    ConditionalProbabilityTable.uniform[MontyHallMove, Rational](evGame.moves(game, state))
// randomMove: MontyHallState => ConditionalProbabilityTable[MontyHallMove, Rational] = <function1>
```

Wrap the strategies in the calls to `writer` that log the transitions from state to state.

```scala
val strategies: Player => MontyHallState => IO[ConditionalProbabilityTable[MontyHallMove, Rational]] = 
  (player: Player) =>
    (state: MontyHallState) =>
      for {
        _ <- playerToWriter(player)(evGameIO.displayStateTo(game, state, player))
        move <- randomMove.andThen( m => IO { m })(state)
      } yield move
// strategies: Player => MontyHallState => IO[ConditionalProbabilityTable[MontyHallMove, Rational]] = <function1>
```

Play the game -- compute the end state from the start state.

```scala
import spire.random.Generator.rng

val endState = play(game, strategies, evGame.startState(game), rng).unsafeRunSync()
// M> Door #1: ???
// M> Door #2: ???
// M> Door #3: ???
// C> Door #1: 
// C> Door #2: 
// C> Door #3: 
// M> Door #1: car
// M> Door #2: goat
// M> Door #3: goat, first choice
// C> Door #1: 
// C> Door #2: , revealed goat
// C> Door #3: first choice
// endState: MontyHallState = MontyHallState(
//   placement = Some(value = PlaceCar(door = 1)),
//   placed = true,
//   firstChoice = Some(value = FirstChoice(door = 3)),
//   reveal = Some(value = Reveal(door = 2)),
//   secondChoice = Some(value = Left(value = Change()))
// )
```

Display outcome to each player

```scala
val outcome = evGame.mover(game, endState).swap.toOption.get
// outcome: MontyHallOutcome = MontyHallOutcome(car = true)

evGame.players(game).foreach { player =>
  playerToWriter(player)(evGameIO.displayOutcomeTo(game, outcome, player)).unsafeRunSync()
}
// C> You won the car!
// M> Contestant won the car!
```

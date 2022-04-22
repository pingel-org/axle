---
layout: page
title: Prisoner's Dilemma
permalink: /tutorial/prisoner/
---

See the Wikipedia page on the [Prisoner's Dilemma](https://en.wikipedia.org/wiki/Prisoner%27s_dilemma)

The `axl.game.prisoner._` package uses `axle.game` typeclasses to model the game:

```scala
import axle.game._
import axle.game.prisoner._

val p1 = Player("P1", "Player 1")
// p1: Player = Player(id = "P1", description = "Player 1")
val p2 = Player("P2", "Player 2")
// p2: Player = Player(id = "P2", description = "Player 2")

val game = PrisonersDilemma(p1, p2)
// game: PrisonersDilemma = PrisonersDilemma(
//   p1 = Player(id = "P1", description = "Player 1"),
//   p2 = Player(id = "P2", description = "Player 2")
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
//   Player(id = "P1", description = "Player 1") -> <function1>,
//   Player(id = "P2", description = "Player 2") -> <function1>
// )
```

Use a uniform distribution on moves as the demo strategy:

```scala
import axle.probability._
import spire.math.Rational

val randomMove =
  (state: PrisonersDilemmaState) =>
    ConditionalProbabilityTable.uniform[PrisonersDilemmaMove, Rational](evGame.moves(game, state))
// randomMove: PrisonersDilemmaState => ConditionalProbabilityTable[PrisonersDilemmaMove, Rational] = <function1>
```

Wrap the strategies in the calls to `writer` that log the transitions from state to state.

```scala
val strategies: Player => PrisonersDilemmaState => IO[ConditionalProbabilityTable[PrisonersDilemmaMove, Rational]] = 
  (player: Player) =>
    (state: PrisonersDilemmaState) =>
      for {
        _ <- playerToWriter(player)(evGameIO.displayStateTo(game, state, player))
        move <- randomMove.andThen( m => IO { m })(state)
      } yield move
// strategies: Player => PrisonersDilemmaState => IO[ConditionalProbabilityTable[PrisonersDilemmaMove, Rational]] = <function1>
```

Play the game -- compute the end state from the start state.

```scala
import spire.random.Generator.rng

val endState = play(game, strategies, evGame.startState(game), rng).unsafeRunSync()
// P1> You have been caught
// P2> You have been caught
// endState: PrisonersDilemmaState = PrisonersDilemmaState(
//   p1Move = Some(value = Silence()),
//   p1Moved = true,
//   p2Move = Some(value = Silence())
// )
```

Display outcome to each player

```scala
val outcome = evGame.mover(game, endState).swap.toOption.get
// outcome: PrisonersDilemmaOutcome = PrisonersDilemmaOutcome(
//   p1YearsInPrison = 1,
//   p2YearsInPrison = 1
// )

evGame.players(game).foreach { player =>
  playerToWriter(player)(evGameIO.displayOutcomeTo(game, outcome, player)).unsafeRunSync()
}
// P1> You is imprisoned for 1 years
// P1> Player 2 is imprisoned for 1 years
// P2> Player 1 is imprisoned for 1 years
// P2> You is imprisoned for 1 years
```

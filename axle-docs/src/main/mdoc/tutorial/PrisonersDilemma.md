---
layout: page
title: Prisoner's Dilemma
permalink: /tutorial/prisoner/
---

See the Wikipedia page on the [Prisoner's Dilemma](https://en.wikipedia.org/wiki/Prisoner%27s_dilemma)

The `axl.game.prisoner._` package uses `axle.game` typeclasses to model the game:

```scala mdoc
import axle.game._
import axle.game.prisoner._

val p1 = Player("P1", "Player 1")
val p2 = Player("P2", "Player 2")

val game = PrisonersDilemma(p1, p2)
```

Create a `writer` for each player that prefixes the player id to all output.

```scala mdoc
import cats.effect.IO
import axle.IO.printMultiLinePrefixed

val playerToWriter: Map[Player, String => IO[Unit]] =
  evGame.players(game).map { player =>
    player -> (printMultiLinePrefixed[IO](player.id) _)
  } toMap
```

Use a uniform distribution on moves as the demo strategy:

```scala mdoc
import axle.probability._
import spire.math.Rational

val randomMove =
  (state: PrisonersDilemmaState) =>
    ConditionalProbabilityTable.uniform[PrisonersDilemmaMove, Rational](evGame.moves(game, state))
```

Wrap the strategies in the calls to `writer` that log the transitions from state to state.

```scala mdoc
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

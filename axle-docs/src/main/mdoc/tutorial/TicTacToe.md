# Tic Tac Toe

A Perfect Information, Zero-sum game

## Example

```scala mdoc
import axle.game._
import axle.game.ttt._

val x = Player("X", "Player X")
val o = Player("O", "Player O")

val game = TicTacToe(3, x, o)
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
  (state: TicTacToeState) =>
    ConditionalProbabilityTable.uniform[TicTacToeMove, Rational](evGame.moves(game, state))
```

Wrap the strategies in the calls to `writer` that log the transitions from state to state.

```scala mdoc
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

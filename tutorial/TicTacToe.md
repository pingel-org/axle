---
layout: page
title: Tic Tac Toe
permalink: /tutorial/tic_tac_toe/
---

A Perfect Information, Zero-sum game

## Example

```scala
import axle.game._
import axle.game.ttt._

val x = Player("X", "Player X")
// x: Player = Player(id = "X", description = "Player X")
val o = Player("O", "Player O")
// o: Player = Player(id = "O", description = "Player O")

val game = TicTacToe(3, x, o)
// game: TicTacToe = TicTacToe(
//   boardSize = 3,
//   x = Player(id = "X", description = "Player X"),
//   o = Player(id = "O", description = "Player O")
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
//   Player(id = "X", description = "Player X") -> <function1>,
//   Player(id = "O", description = "Player O") -> <function1>
// )
```

Use a uniform distribution on moves as the demo strategy:

```scala
import axle.probability._
import spire.math.Rational

val randomMove =
  (state: TicTacToeState) =>
    ConditionalProbabilityTable.uniform[TicTacToeMove, Rational](evGame.moves(game, state))
// randomMove: TicTacToeState => ConditionalProbabilityTable[TicTacToeMove, Rational] = <function1>
```

Wrap the strategies in the calls to `writer` that log the transitions from state to state.

```scala
val strategies: Player => TicTacToeState => IO[ConditionalProbabilityTable[TicTacToeMove, Rational]] = 
  (player: Player) =>
    (state: TicTacToeState) =>
      for {
        _ <- playerToWriter(player)(evGameIO.displayStateTo(game, state, player))
        move <- randomMove.andThen( m => IO { m })(state)
      } yield move
// strategies: Player => TicTacToeState => IO[ConditionalProbabilityTable[TicTacToeMove, Rational]] = <function1>
```

Play the game -- compute the end state from the start state.

```scala
import spire.random.Generator.rng

val endState = play(game, strategies, evGame.startState(game), rng).unsafeRunSync()
// X> Board:         Movement Key:
// X>  | |           1|2|3
// X>  | |           4|5|6
// X>  | |           7|8|9
// O> Board:         Movement Key:
// O>  | |           1|2|3
// O>  | |           4|5|6
// O> X| |           7|8|9
// X> Board:         Movement Key:
// X>  |O|           1|2|3
// X>  | |           4|5|6
// X> X| |           7|8|9
// O> Board:         Movement Key:
// O>  |O|           1|2|3
// O>  |X|           4|5|6
// O> X| |           7|8|9
// X> Board:         Movement Key:
// X>  |O|           1|2|3
// X>  |X|           4|5|6
// X> X| |O          7|8|9
// O> Board:         Movement Key:
// O>  |O|           1|2|3
// O>  |X|X          4|5|6
// O> X| |O          7|8|9
// X> Board:         Movement Key:
// X>  |O|O          1|2|3
// X>  |X|X          4|5|6
// X> X| |O          7|8|9
// O> Board:         Movement Key:
// O> X|O|O          1|2|3
// O>  |X|X          4|5|6
// O> X| |O          7|8|9
// X> Board:         Movement Key:
// X> X|O|O          1|2|3
// X>  |X|X          4|5|6
// X> X|O|O          7|8|9
// endState: TicTacToeState = TicTacToeState(
//   moverOpt = None,
//   board = Array(
//     Some(value = Player(id = "X", description = "Player X")),
//     Some(value = Player(id = "O", description = "Player O")),
//     Some(value = Player(id = "O", description = "Player O")),
//     Some(value = Player(id = "X", description = "Player X")),
//     Some(value = Player(id = "X", description = "Player X")),
//     Some(value = Player(id = "X", description = "Player X")),
//     Some(value = Player(id = "X", description = "Player X")),
//     Some(value = Player(id = "O", description = "Player O")),
//     Some(value = Player(id = "O", description = "Player O"))
//   ),
//   boardSize = 3
// )
```

Display outcome to each player

```scala
val outcome = evGame.mover(game, endState).swap.toOption.get
// outcome: TicTacToeOutcome = TicTacToeOutcome(
//   winner = Some(value = Player(id = "X", description = "Player X"))
// )

evGame.players(game).foreach { player =>
  playerToWriter(player)(evGameIO.displayOutcomeTo(game, outcome, player)).unsafeRunSync()
}
// X> You beat Player O!
// O> Player X beat You!
```

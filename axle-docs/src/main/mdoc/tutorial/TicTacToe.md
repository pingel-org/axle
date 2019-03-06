---
layout: page
title: Tic Tac Toe
permalink: /tutorial/tic_tac_toe/
---

A Perfect Information, Zero-sum game

## Example

```scala mdoc
import axle._
import axle.game._
import axle.game.ttt._
import axle.game.ttt.evGame._
import Strategies._

val x = Player("X", "Player X")
val o = Player("O", "Player O")

val game = TicTacToe(3,
  x, randomMove, prefixedDisplay("X")(println),
  o, randomMove, prefixedDisplay("O")(println))
```

Compute the end state from the start state

```scala mdoc
import spire.random.Generator.rng

play(game, startState(game), false, rng)
```

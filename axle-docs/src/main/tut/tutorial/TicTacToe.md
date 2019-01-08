---
layout: page
title: Tic Tac Toe
permalink: /tutorial/tic_tac_toe/
---

A Perfect Information, Zero-sum game

## Example

```tut
import axle._
import axle.game._
import axle.game.ttt._
import Strategies._

val x = Player("X", "Player X")
val o = Player("O", "Player O")

val game = TicTacToe(3,
  x, randomMove, prefixedDisplay("X")(println),
  o, randomMove, prefixedDisplay("O")(println))

play(game)
```

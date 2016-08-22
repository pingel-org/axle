---
layout: page
title: Tic Tac Toe
permalink: /tutorial/tic_tac_toe/
---

A Perfect Information, Zero-sum game

Example
-------

```
import axle._
import axle.game.ttt._

val game = TicTacToe(3, "human", "ai")
val start = game.startState()

game.play(start)
```

Will pit a human player (X) against an AI player (O).  The AI is simple 3-move lookahead minimax algorithm, with no heuristic (it only counts wins and losses).

An example of the first few lines of output:

```
        Tic Tac Toe
Moves are numbers 1-9.

Board:         Movement Key:
 | |           1|2|3
 | |           4|5|6
 | |           7|8|9
```

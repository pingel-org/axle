---
layout: page
title: Prisoner's Dilemma
permalink: /tutorial/prisoner/
---

See the Wikipedia page on the [Prisoner's Dilemma](https://en.wikipedia.org/wiki/Prisoner%27s_dilemma)

The `axl.game.prisoner._` package uses `axle.game` typeclasses to model the game:

```tut
import axle._
import axle.game._
import axle.game.prisoner._

import Strategies._

val p1 = Player("P1", "Player 1")
val p2 = Player("P2", "Player 2")

val game = PrisonersDilemma(
  p1, randomMove, prefixedDisplay("1")(println),
  p2, randomMove, prefixedDisplay("2")(println))

play(game)
```

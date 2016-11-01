---
layout: page
title: Monty Hall
permalink: /tutorial/monty_hall/
---

See the Wikipedia page on the [Monty Hall problem](https://en.wikipedia.org/wiki/Monty_Hall_problem)

The `axle.game.OldMontyHall` object contains a model of the rules of the game.

```tut:silent
import axle.game.OldMontyHall._
import spire.math.Rational
```

The models supports querying the chance of winning given the odds that the
player switches his or her initial choice.

At one extreme, the odds of winning given that the other door is always chosen:

```tut:book
chanceOfWinning(Rational(1))
```

At the other extreme, the player always sticks with the initial choice.

```tut:book
chanceOfWinning(Rational(0))
```

The newer `axl.game.montyhall._` package uses `axle.game` typeclasses to model the game:

```tut
import axle._
import axle.game._
import axle.game.montyhall._

import Strategies._

val contestant = Player("C", "Contestant")
val monty = Player("M", "Monty Hall")

val game = MontyHall(
  contestant, randomMove, prefixedDisplay("C")(println),
  monty, randomMove, prefixedDisplay("M")(println))

play(game)
```

# Future Work for Axle Game

## Missing functionality

* Remove moveStateStream

* For one game (probably Poker)
  * Record witnessed and unwitnessed history `Seq[(M, S)]` in `State`
  * Display to user in interactiveMove
    * `val mm = evGame.maskMove(game, move, mover, observer)`
    * `evGameIO.displayMoveTo(game, mm, mover, observer)`
  * Then generalize and pull into framework

## Motivating Examples

* Generalize `OldMontyHall.chanceOfWinning`

* GuessRiffle.md
  * Walk through game
  * Plot distribution of sum(entropy) for both strategies
  * Plot entropy by turn # for each strategy
  * Plot simulated score distribution for each strategy

* GuessRiffleSpec: use `moveFromRandomState`

* Gerrymandering sensitivity

* "You split, I choose" as game

## Deeper changes to axle.game

* `aiMover.unmask` prevents `MontyHallSpec` "AI vs. AI game produces moveStateStream" from working
  * will be an issue for all non-perfect information

* Identify all uses of `spire.random.Generator` (and other random value generation)

* See uses of `seed` in `GuessRiffleProperties`

* Eliminate entropy consumption of `rng` side-effect (eg `applyMove(Riffle())`)
  * `Chance` should be its own player
  * Consider whether `PM` should be a part of `Strategy` type (`MS => PM[M, V]`)
    * More abstractly, more many intents and purposes, all we are about is that resolving PM to M consumes entropy
    * In which cases should the `PM` be retained?
  * Each N bits consumed during `Riffle()` is its own move
  * Chance moves consume `UnittedQuantity[Information, N]`

* `perceive` could return a lower-entropy probability model
  * Perhaps in exchange for a given amount of energy
  * Or ask for a 0-entropy model and be told how expensive that was

* Game theory axioms (Nash, etc)

* `axle.game`: `Observable[T]`

## Hygeine

* performance benchmark

* Replace `axle.game.moveFromRandomState.mapToProb`

* Clean up `axle.game.playWithIntroAndOutcomes`

* The references to `movesMap` in `MoveFromRandomStateSpec.scala` illustrate a need for a cleaner way to create a hard-coded strategy -- which could just be in the form of a couple utility functions from `movesMap` to the data needed by `evGame.{moves,applyMove}` and `rm` strategy

* Generalize `ConditionalProbabilityTable.uniform` into typeclass

* Simplify `GuessRiffleProperties` (especially second property)
* stateStreamMap only used in GuessRiffleProperties -- stop using chain?
* stateStrategyMoveStream only used in GuessRiffleProperties

* `Game.players` should be a part of GameState (or take it as an argument)?  Will wait for pressing use case.

## Game Theory and Examples

* Factor `axle.game.moveFromRandomState` in terms of a random walk on a graph.
  * See "TODO scale mass down"
  * Compare to Brownian motion, Random walk, Ito process, ...
  * Provide some axioms
    * no outgoing with path in from non-zero mass monotonically increases
    * no incoming with path out monotonically decreases
  * possibly provide a version for acyclic graphs
* Iterative game playing algorithm is intractible, but shares intent with sequential monte carlo
* Think about Information Theory's "Omega" vis-a-vis Sequential Monte Carlo
* Improve `axle.stats.rationalProbabilityDist` as probabilities become smaller
* SimpsonsParadox.md
* Axioms of partial differentiation
  * [Plotkin Partial Differentiation](https://math.ucr.edu/home/baez/mathematical/ACTUCR/Plotkin_Partial_Differentiation.pdf)
* Conal Elliott: Efficient automatic differentiation made easy via category theory
* Max bet for Poker
* syntax for `Game` typeclass


Information Theory
==================

Entropy
-------

The calculation of the entropy of a distribution is available as a function called `entropy`
as well as the traditional `H`:

Imports and implicits

```scala
import axle._
import axle.stats._
import spire.math._
import spire.algebra._
import axle.quanta.Information
import spire.implicits.DoubleAlgebra
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.quanta.UnitOfMeasurement
import axle.game.Dice.die

implicit val informationConverter = Information.converterGraphK2[Double, DirectedSparseGraph]
```

Usage

```scala
scala> string(H(die(6)))
res1: String = 2.584963 b

scala> string(entropy(coin(Rational(7, 10))))
res2: String = 0.881291 b

scala> string(H(coin()))
res3: String = 1.000000 b
```

See also the [Coin Entropy](CoinEntropy.md) example.

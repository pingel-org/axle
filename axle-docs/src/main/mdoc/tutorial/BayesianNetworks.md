---
layout: page
title: Bayesian Networks
permalink: /tutorial/bayesian_networks/
---

See the Wikipedia page on [Bayesian networks](https://en.wikipedia.org/wiki/Bayesian_network)

## Alarm Example

Define random variables

```scala mdoc
import axle.probability._

val bools = Vector(true, false)

val B = Variable[Boolean]("Burglary")
val E = Variable[Boolean]("Earthquake")
val A = Variable[Boolean]("Alarm")
val J = Variable[Boolean]("John Calls")
val M = Variable[Boolean]("Mary Calls")
```

Define Factor for each variable

```scala mdoc
import spire.math._
import cats.implicits._

val bFactor =
  Factor(Vector(B -> bools), Map(
    Vector(B is true) -> Rational(1, 1000),
    Vector(B is false) -> Rational(999, 1000)))

val eFactor =
  Factor(Vector(E -> bools), Map(
    Vector(E is true) -> Rational(1, 500),
    Vector(E is false) -> Rational(499, 500)))

val aFactor =
  Factor(Vector(B -> bools, E -> bools, A -> bools), Map(
    Vector(B is false, E is false, A is true) -> Rational(1, 1000),
    Vector(B is false, E is false, A is false) -> Rational(999, 1000),
    Vector(B is true, E is false, A is true) -> Rational(940, 1000),
    Vector(B is true, E is false, A is false) -> Rational(60, 1000),
    Vector(B is false, E is true, A is true) -> Rational(290, 1000),
    Vector(B is false, E is true, A is false) -> Rational(710, 1000),
    Vector(B is true, E is true, A is true) -> Rational(950, 1000),
    Vector(B is true, E is true, A is false) -> Rational(50, 1000)))

val jFactor =
  Factor(Vector(A -> bools, J -> bools), Map(
    Vector(A is true, J is true) -> Rational(9, 10),
    Vector(A is true, J is false) -> Rational(1, 10),
    Vector(A is false, J is true) -> Rational(5, 100),
    Vector(A is false, J is false) -> Rational(95, 100)))

val mFactor =
  Factor(Vector(A -> bools, M -> bools), Map(
    Vector(A is true, M is true) -> Rational(7, 10),
    Vector(A is true, M is false) -> Rational(3, 10),
    Vector(A is false, M is true) -> Rational(1, 100),
    Vector(A is false, M is false) -> Rational(99, 100)))
```

Arrange into a graph

```scala mdoc
import axle.pgm._
import axle.jung._
import edu.uci.ics.jung.graph.DirectedSparseGraph

// edges: ba, ea, aj, am

val bn: BayesianNetwork[Boolean, Rational, DirectedSparseGraph[BayesianNetworkNode[Boolean, Rational], Edge]] =
  BayesianNetwork.withGraphK2[Boolean, Rational, DirectedSparseGraph](
    Map(
      B -> bFactor,
      E -> eFactor,
      A -> aFactor,
      J -> jFactor,
      M -> mFactor))
```

Create an SVG visualization

```scala mdoc
import axle.visualize._

val bnVis  = BayesianNetworkVisualization(bn, 1000, 1000, 20)
```

Render as SVG file

```scala mdoc
import axle.web._
import cats.effect._

bnVis.svg[IO]("alarmbayes.svg").unsafeRunSync()
```

![alarm bayes network](/tutorial/images/alarmbayes.svg)

The network can be used to compute the joint probability table:

```scala mdoc
import axle.math.showRational

val jpt = bn.jointProbabilityTable

jpt.show
```

Variables can be summed out of the factor:

```scala mdoc
import axle._

jpt.Σ(M).Σ(J).Σ(A).Σ(B).Σ(E)
```

```scala mdoc
jpt.sumOut(M).sumOut(J).sumOut(A).sumOut(B).sumOut(E)
```

Multiplication of factors also works:

```scala mdoc
import spire.implicits.multiplicativeSemigroupOps

val f = (bn.factorFor(A) * bn.factorFor(B)) * bn.factorFor(E)

f.show
```

Markov assumptions:

```scala mdoc
bn.markovAssumptionsFor(M).show
```

This is read as "M is independent of E, B, and J given A".

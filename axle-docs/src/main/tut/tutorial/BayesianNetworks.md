---
layout: page
title: Bayesian Networks
permalink: /tutorial/bayesian_networks/
---

See the Wikipedia page on [Bayesian networks](https://en.wikipedia.org/wiki/Bayesian_network)


Alarm Example
-------------

Imports

```tut:book:silent
import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.implicits._
import spire.math._
import spire.implicits._
import axle._
import axle.algebra.DirectedGraph
import axle.stats._
import axle.pgm._
import axle.jblas._
import axle.jung.directedGraphJung
```

Setup

```tut:book
val bools = Vector(true, false)

val B = UnknownDistribution0[Boolean, Rational](bools, "Burglary")
val E = UnknownDistribution0[Boolean, Rational](bools, "Earthquake")
val A = UnknownDistribution0[Boolean, Rational](bools, "Alarm")
val J = UnknownDistribution0[Boolean, Rational](bools, "John Calls")
val M = UnknownDistribution0[Boolean, Rational](bools, "Mary Calls")

val bFactor =
  Factor(Vector(B), Map(
    Vector(B is true) -> Rational(1, 1000),
    Vector(B is false) -> Rational(999, 1000)))

val eFactor =
  Factor(Vector(E), Map(
    Vector(E is true) -> Rational(1, 500),
    Vector(E is false) -> Rational(499, 500)))

val aFactor =
  Factor(Vector(B, E, A), Map(
    Vector(B is false, E is false, A is true) -> Rational(1, 1000),
    Vector(B is false, E is false, A is false) -> Rational(999, 1000),
    Vector(B is true, E is false, A is true) -> Rational(940, 1000),
    Vector(B is true, E is false, A is false) -> Rational(60, 1000),
    Vector(B is false, E is true, A is true) -> Rational(290, 1000),
    Vector(B is false, E is true, A is false) -> Rational(710, 1000),
    Vector(B is true, E is true, A is true) -> Rational(950, 1000),
    Vector(B is true, E is true, A is false) -> Rational(50, 1000)))

val jFactor =
  Factor(Vector(A, J), Map(
    Vector(A is true, J is true) -> Rational(9, 10),
    Vector(A is true, J is false) -> Rational(1, 10),
    Vector(A is false, J is true) -> Rational(5, 100),
    Vector(A is false, J is false) -> Rational(95, 100)))

val mFactor =
  Factor(Vector(A, M), Map(
    Vector(A is true, M is true) -> Rational(7, 10),
    Vector(A is true, M is false) -> Rational(3, 10),
    Vector(A is false, M is true) -> Rational(1, 100),
    Vector(A is false, M is false) -> Rational(99, 100)))

// edges: ba, ea, aj, am
val bn = BayesianNetwork.withGraphK2[Boolean, Rational, DirectedSparseGraph](
  "A sounds (due to Burglary or Earthquake) and John or Mary Call",
  Map(B -> bFactor,
    E -> eFactor,
    A -> aFactor,
    J -> jFactor,
    M -> mFactor))
```

Create an SVG visualization

```tut:book
import axle.visualize._
import axle.web._

svg(bn, "alarmbayes.svg")
```

![alarm bayes network](/tutorial/images/alarmbayes.svg)

The network can be used to compute the joint probability table:

```tut:book
val jpt = bn.jointProbabilityTable

string(jpt)
```

Variables can be summed out of the factor:

```tut:book
jpt.Σ(M).Σ(J).Σ(A).Σ(B).Σ(E)
```

```tut:book
jpt.sumOut(M).sumOut(J).sumOut(A).sumOut(B).sumOut(E)
```

Multiplication of factors also works:

```tut:book
val f = (bn.cpt(A) * bn.cpt(B)) * bn.cpt(E)

string(f)
```

Markov assumptions:

```tut:book
string(bn.markovAssumptionsFor(M))
```

This is read as "M is independent of E, B, and J given A".

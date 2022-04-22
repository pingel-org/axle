---
layout: page
title: Bayesian Networks
permalink: /tutorial/bayesian_networks/
---

See the Wikipedia page on [Bayesian networks](https://en.wikipedia.org/wiki/Bayesian_network)

## Alarm Example

Define random variables

```scala
import axle.probability._

val bools = Vector(true, false)
// bools: Vector[Boolean] = Vector(true, false)

val B = Variable[Boolean]("Burglary")
// B: Variable[Boolean] = Variable(name = "Burglary")
val E = Variable[Boolean]("Earthquake")
// E: Variable[Boolean] = Variable(name = "Earthquake")
val A = Variable[Boolean]("Alarm")
// A: Variable[Boolean] = Variable(name = "Alarm")
val J = Variable[Boolean]("John Calls")
// J: Variable[Boolean] = Variable(name = "John Calls")
val M = Variable[Boolean]("Mary Calls")
// M: Variable[Boolean] = Variable(name = "Mary Calls")
```

Define Factor for each variable

```scala
import spire.math._
import cats.implicits._

val bFactor =
  Factor(Vector(B -> bools), Map(
    Vector(B is true) -> Rational(1, 1000),
    Vector(B is false) -> Rational(999, 1000)))
// bFactor: Factor[Boolean, Rational] = Factor(
//   variablesWithValues = Vector(
//     (Variable(name = "Burglary"), Vector(true, false))
//   ),
//   probabilities = Map(
//     Vector(RegionEq(x = true)) -> 1/1000,
//     Vector(RegionEq(x = false)) -> 999/1000
//   )
// )

val eFactor =
  Factor(Vector(E -> bools), Map(
    Vector(E is true) -> Rational(1, 500),
    Vector(E is false) -> Rational(499, 500)))
// eFactor: Factor[Boolean, Rational] = Factor(
//   variablesWithValues = Vector(
//     (Variable(name = "Earthquake"), Vector(true, false))
//   ),
//   probabilities = Map(
//     Vector(RegionEq(x = true)) -> 1/500,
//     Vector(RegionEq(x = false)) -> 499/500
//   )
// )

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
// aFactor: Factor[Boolean, Rational] = Factor(
//   variablesWithValues = Vector(
//     (Variable(name = "Burglary"), Vector(true, false)),
//     (Variable(name = "Earthquake"), Vector(true, false)),
//     (Variable(name = "Alarm"), Vector(true, false))
//   ),
//   probabilities = HashMap(
//     Vector(RegionEq(x = false), RegionEq(x = false), RegionEq(x = false)) -> 999/1000,
//     Vector(RegionEq(x = true), RegionEq(x = false), RegionEq(x = true)) -> 47/50,
//     Vector(RegionEq(x = true), RegionEq(x = true), RegionEq(x = false)) -> 1/20,
//     Vector(RegionEq(x = false), RegionEq(x = true), RegionEq(x = true)) -> 29/100,
//     Vector(RegionEq(x = true), RegionEq(x = false), RegionEq(x = false)) -> 3/50,
//     Vector(RegionEq(x = true), RegionEq(x = true), RegionEq(x = true)) -> 19/20,
//     Vector(RegionEq(x = false), RegionEq(x = false), RegionEq(x = true)) -> 1/1000,
//     Vector(RegionEq(x = false), RegionEq(x = true), RegionEq(x = false)) -> 71/100
//   )
// )

val jFactor =
  Factor(Vector(A -> bools, J -> bools), Map(
    Vector(A is true, J is true) -> Rational(9, 10),
    Vector(A is true, J is false) -> Rational(1, 10),
    Vector(A is false, J is true) -> Rational(5, 100),
    Vector(A is false, J is false) -> Rational(95, 100)))
// jFactor: Factor[Boolean, Rational] = Factor(
//   variablesWithValues = Vector(
//     (Variable(name = "Alarm"), Vector(true, false)),
//     (Variable(name = "John Calls"), Vector(true, false))
//   ),
//   probabilities = Map(
//     Vector(RegionEq(x = true), RegionEq(x = true)) -> 9/10,
//     Vector(RegionEq(x = true), RegionEq(x = false)) -> 1/10,
//     Vector(RegionEq(x = false), RegionEq(x = true)) -> 1/20,
//     Vector(RegionEq(x = false), RegionEq(x = false)) -> 19/20
//   )
// )

val mFactor =
  Factor(Vector(A -> bools, M -> bools), Map(
    Vector(A is true, M is true) -> Rational(7, 10),
    Vector(A is true, M is false) -> Rational(3, 10),
    Vector(A is false, M is true) -> Rational(1, 100),
    Vector(A is false, M is false) -> Rational(99, 100)))
// mFactor: Factor[Boolean, Rational] = Factor(
//   variablesWithValues = Vector(
//     (Variable(name = "Alarm"), Vector(true, false)),
//     (Variable(name = "Mary Calls"), Vector(true, false))
//   ),
//   probabilities = Map(
//     Vector(RegionEq(x = true), RegionEq(x = true)) -> 7/10,
//     Vector(RegionEq(x = true), RegionEq(x = false)) -> 3/10,
//     Vector(RegionEq(x = false), RegionEq(x = true)) -> 1/100,
//     Vector(RegionEq(x = false), RegionEq(x = false)) -> 99/100
//   )
// )
```

Arrange into a graph

```scala
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
// bn: BayesianNetwork[Boolean, Rational, DirectedSparseGraph[BayesianNetworkNode[Boolean, Rational], Edge]] = BayesianNetwork(
//   variableFactorMap = HashMap(
//     Variable(name = "John Calls") -> Factor(
//       variablesWithValues = Vector(
//         (Variable(name = "Alarm"), Vector(true, false)),
//         (Variable(name = "John Calls"), Vector(true, false))
//       ),
//       probabilities = Map(
//         Vector(RegionEq(x = true), RegionEq(x = true)) -> 9/10,
//         Vector(RegionEq(x = true), RegionEq(x = false)) -> 1/10,
//         Vector(RegionEq(x = false), RegionEq(x = true)) -> 1/20,
//         Vector(RegionEq(x = false), RegionEq(x = false)) -> 19/20
//       )
//     ),
//     Variable(name = "Mary Calls") -> Factor(
//       variablesWithValues = Vector(
//         (Variable(name = "Alarm"), Vector(true, false)),
//         (Variable(name = "Mary Calls"), Vector(true, false))
//       ),
//       probabilities = Map(
//         Vector(RegionEq(x = true), RegionEq(x = true)) -> 7/10,
//         Vector(RegionEq(x = true), RegionEq(x = false)) -> 3/10,
//         Vector(RegionEq(x = false), RegionEq(x = true)) -> 1/100,
//         Vector(RegionEq(x = false), RegionEq(x = false)) -> 99/100
//       )
//     ),
//     Variable(name = "Alarm") -> Factor(
//       variablesWithValues = Vector(
//         (Variable(name = "Burglary"), Vector(true, false)),
//         (Variable(name = "Earthquake"), Vector(true, false)),
//         (Variable(name = "Alarm"), Vector(true, false))
//       ),
//       probabilities = HashMap(
//         Vector(RegionEq(x = false), RegionEq(x = false), RegionEq(x = false)) -> 999/1000,
//         Vector(RegionEq(x = true), RegionEq(x = false), RegionEq(x = true)) -> 47/50,
//         Vector(RegionEq(x = true), RegionEq(x = true), RegionEq(x = false)) -> 1/20,
//         Vector(RegionEq(x = false), RegionEq(x = true), RegionEq(x = true)) -> 29/100,
//         Vector(RegionEq(x = true), RegionEq(x = false), RegionEq(x = false)) -> 3/50,
//         Vector(RegionEq(x = true), RegionEq(x = true), RegionEq(x = true)) -> 19/20,
//         Vector(RegionEq(x = false), RegionEq(x = false), RegionEq(x = true)) -> 1/1000,
//         Vector(RegionEq(x = false), RegionEq(x = true), RegionEq(x = false)) -> 71/100
// ...
```

Create an SVG visualization

```scala
import axle.visualize._

val bnVis  = BayesianNetworkVisualization(bn, 1000, 1000, 20)
// bnVis: BayesianNetworkVisualization[Boolean, Rational, DirectedSparseGraph[BayesianNetworkNode[Boolean, Rational], Edge]] = BayesianNetworkVisualization(
//   bn = BayesianNetwork(
//     variableFactorMap = HashMap(
//       Variable(name = "John Calls") -> Factor(
//         variablesWithValues = Vector(
//           (Variable(name = "Alarm"), Vector(true, false)),
//           (Variable(name = "John Calls"), Vector(true, false))
//         ),
//         probabilities = Map(
//           Vector(RegionEq(x = true), RegionEq(x = true)) -> 9/10,
//           Vector(RegionEq(x = true), RegionEq(x = false)) -> 1/10,
//           Vector(RegionEq(x = false), RegionEq(x = true)) -> 1/20,
//           Vector(RegionEq(x = false), RegionEq(x = false)) -> 19/20
//         )
//       ),
//       Variable(name = "Mary Calls") -> Factor(
//         variablesWithValues = Vector(
//           (Variable(name = "Alarm"), Vector(true, false)),
//           (Variable(name = "Mary Calls"), Vector(true, false))
//         ),
//         probabilities = Map(
//           Vector(RegionEq(x = true), RegionEq(x = true)) -> 7/10,
//           Vector(RegionEq(x = true), RegionEq(x = false)) -> 3/10,
//           Vector(RegionEq(x = false), RegionEq(x = true)) -> 1/100,
//           Vector(RegionEq(x = false), RegionEq(x = false)) -> 99/100
//         )
//       ),
//       Variable(name = "Alarm") -> Factor(
//         variablesWithValues = Vector(
//           (Variable(name = "Burglary"), Vector(true, false)),
//           (Variable(name = "Earthquake"), Vector(true, false)),
//           (Variable(name = "Alarm"), Vector(true, false))
//         ),
//         probabilities = HashMap(
//           Vector(RegionEq(x = false), RegionEq(x = false), RegionEq(x = false)) -> 999/1000,
//           Vector(RegionEq(x = true), RegionEq(x = false), RegionEq(x = true)) -> 47/50,
//           Vector(RegionEq(x = true), RegionEq(x = true), RegionEq(x = false)) -> 1/20,
//           Vector(RegionEq(x = false), RegionEq(x = true), RegionEq(x = true)) -> 29/100,
//           Vector(RegionEq(x = true), RegionEq(x = false), RegionEq(x = false)) -> 3/50,
//           Vector(RegionEq(x = true), RegionEq(x = true), RegionEq(x = true)) -> 19/20,
//           Vector(RegionEq(x = false), RegionEq(x = false), RegionEq(x = true)) -> 1/1000,
//           Vector(RegionEq(x = false), RegionEq(x = true), RegionEq(x = false)) -...
```

Render as SVG file

```scala
import axle.web._
import cats.effect._

bnVis.svg[IO]("alarm_bayes.svg").unsafeRunSync()
```

![alarm bayes network](/tutorial/images/alarm_bayes.svg)

The network can be used to compute the joint probability table:

```scala
import axle.math.showRational

val jpt = bn.jointProbabilityTable
// jpt: Factor[Boolean, Rational] = Factor(
//   variablesWithValues = Vector(
//     (Variable(name = "John Calls"), Vector(true, false)),
//     (Variable(name = "Alarm"), Vector(true, false)),
//     (Variable(name = "Earthquake"), Vector(true, false)),
//     (Variable(name = "Burglary"), Vector(true, false)),
//     (Variable(name = "Mary Calls"), Vector(true, false))
//   ),
//   probabilities = HashMap(
//     Vector(
//       RegionEq(x = true),
//       RegionEq(x = true),
//       RegionEq(x = true),
//       RegionEq(x = true),
//       RegionEq(x = true)
//     ) -> 1197/1000000000,
//     Vector(
//       RegionEq(x = true),
//       RegionEq(x = false),
//       RegionEq(x = true),
//       RegionEq(x = true),
//       RegionEq(x = true)
//     ) -> 1/20000000000,
//     Vector(
//       RegionEq(x = false),
//       RegionEq(x = false),
//       RegionEq(x = false),
//       RegionEq(x = false),
//       RegionEq(x = true)
//     ) -> 9462047481/1000000000000,
//     Vector(
//       RegionEq(x = false),
//       RegionEq(x = true),
//       RegionEq(x = false),
//       RegionEq(x = true),
//       RegionEq(x = false)
//     ) -> 70359/2500000000,
//     Vector(
//       RegionEq(x = true),
//       RegionEq(x = true),
//       RegionEq(x = false),
//       RegionEq(x = false),
//       RegionEq(x = true)
//     ) -> 31405563/50000000000,
//     Vector(
//       RegionEq(x = true),
//       RegionEq(x = true),
//       RegionEq(x = false),
//       RegionEq(x = true),
// ...

jpt.show
// res1: String = """John Calls Alarm Earthquake Burglary Mary Calls
// true       true  true       true     true       1197/1000000000
// true       true  true       true     false      513/1000000000
// true       true  true       false    true       1825173/5000000000
// true       true  true       false    false      782217/5000000000
// true       true  false      true     true       1477539/2500000000
// true       true  false      true     false      633231/2500000000
// true       true  false      false    true       31405563/50000000000
// true       true  false      false    false      13459527/50000000000
// true       false true       true     true       1/20000000000
// true       false true       true     false      99/20000000000
// true       false true       false    true       70929/100000000000
// true       false true       false    false      7021971/100000000000
// true       false false      true     true       1497/50000000000
// true       false false      true     false      148203/50000000000
// true       false false      false    true       498002499/1000000000000
// true       false false      false    false      49302247401/1000000000000
// false      true  true       true     true       133/1000000000
// false      true  true       true     false      57/1000000000
// false      true  true       false    true       202797/5000000000
// false      true  true       false    false      86913/5000000000
// false      true  false      true     true       164171/2500000000
// false      true  false      true     false      70359/2500000000
// false      true  false      false    true       3489507/50000000000
// false      true  false      false    false      1495503/50000000000
// false      false true       true     true       19/20000000000
// false      false true       true     false      1881/20000000000
// false      false true       false    true       1347651/100000000000
// false      false true       false    false      133417449/100000000000
// false      false false      true     true       28443/50000000000
// false      false false      true     false      2815857/50000000000
// false      false false      false    true       9462047481/1000000000000
// false      false false      false    false      936742700619/1000000000000"""
```

Variables can be summed out of the factor:

```scala
import axle._

jpt.Σ(M).Σ(J).Σ(A).Σ(B).Σ(E)
// res2: Factor[Boolean, Rational] = Factor(
//   variablesWithValues = Vector(),
//   probabilities = Map(Vector() -> 1)
// )
```

```scala
jpt.sumOut(M).sumOut(J).sumOut(A).sumOut(B).sumOut(E)
// res3: Factor[Boolean, Rational] = Factor(
//   variablesWithValues = Vector(),
//   probabilities = Map(Vector() -> 1)
// )
```

Multiplication of factors also works:

```scala
import spire.implicits.multiplicativeSemigroupOps

val f = (bn.factorFor(A) * bn.factorFor(B)) * bn.factorFor(E)
// f: Factor[Boolean, Rational] = Factor(
//   variablesWithValues = Vector(
//     (Variable(name = "Burglary"), Vector(true, false)),
//     (Variable(name = "Earthquake"), Vector(true, false)),
//     (Variable(name = "Alarm"), Vector(true, false))
//   ),
//   probabilities = HashMap(
//     Vector(RegionEq(x = false), RegionEq(x = false), RegionEq(x = false)) -> 498002499/500000000,
//     Vector(RegionEq(x = true), RegionEq(x = false), RegionEq(x = true)) -> 23453/25000000,
//     Vector(RegionEq(x = true), RegionEq(x = true), RegionEq(x = false)) -> 1/10000000,
//     Vector(RegionEq(x = false), RegionEq(x = true), RegionEq(x = true)) -> 28971/50000000,
//     Vector(RegionEq(x = true), RegionEq(x = false), RegionEq(x = false)) -> 1497/25000000,
//     Vector(RegionEq(x = true), RegionEq(x = true), RegionEq(x = true)) -> 19/10000000,
//     Vector(RegionEq(x = false), RegionEq(x = false), RegionEq(x = true)) -> 498501/500000000,
//     Vector(RegionEq(x = false), RegionEq(x = true), RegionEq(x = false)) -> 70929/50000000
//   )
// )

f.show
// res4: String = """Burglary Earthquake Alarm
// true     true       true  19/10000000
// true     true       false 1/10000000
// true     false      true  23453/25000000
// true     false      false 1497/25000000
// false    true       true  28971/50000000
// false    true       false 70929/50000000
// false    false      true  498501/500000000
// false    false      false 498002499/500000000"""
```

Markov assumptions:

```scala
bn.markovAssumptionsFor(M).show
// res5: String = "I({Mary Calls}, {Alarm}, {John Calls, Earthquake, Burglary})"
```

This is read as "M is independent of E, B, and J given A".

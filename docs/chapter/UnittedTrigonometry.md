Unitted Trigonometry
====================

Versions of the trigonometric functions sine, cosine, and tangent, require that the arguments are Angles.

Examples
--------

Examples of the functions

Imports and implicits

```scala
import axle._
import axle.quanta.Angle
import axle.quanta.UnittedQuantity
import axle.quanta.UnitOfMeasurement
import axle.algebra.modules.doubleRationalModule
import spire.implicits.DoubleAlgebra
import spire.algebra.Module
import spire.math.Rational
import axle.jung.directedGraphJung
import edu.uci.ics.jung.graph.DirectedSparseGraph

implicit val angleConverter = Angle.converterGraphK2[Double, DirectedSparseGraph]

import angleConverter.degree
import angleConverter.radian
```

Usage

```scala
scala> cosine(10d *: degree)
res2: Double = 0.984807753012208

scala> sine(3d *: radian)
res3: Double = 0.1411200080598672

scala> tangent(40d *: degree)
res4: Double = 0.8390996311772799
```

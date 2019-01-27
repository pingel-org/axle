---
layout: page
title: Entropy of a Biased Coin
permalink: /tutorial/entropy_biased_coin/
---

Visualize the relationship of a coin's bias to its entropy with this code snippet.

Imports and implicits:

```scala mdoc:silent
import scala.collection.immutable.TreeMap
import cats.implicits._
import spire.math.Rational
import axle._
import axle.stats._
import axle.quanta.Information
import axle.quanta.UnittedQuantity

type D = TreeMap[Rational, UnittedQuantity[Information, Double]]

import spire.implicits.DoubleAlgebra
import axle.jung.directedGraphJung
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.quanta.UnitOfMeasurement

implicit val id = Information.converterGraphK2[Double, DirectedSparseGraph]

import axle.stats.H
import axle.stats.coin
```

Create dataset

```scala mdoc
val hm: D = new TreeMap[Rational, UnittedQuantity[Information, Double]]() ++ (0 to 100).map(i => (Rational(i / 100d), H(coin(Rational(i.toLong, 100))))).toMap
```

Define visualization

```scala mdoc
import axle.visualize._
implicit val bitDouble = id.bit

implicit val ut = axle.quanta.unittedTicsGraphK2[Information, Double, DirectedSparseGraph]

val plot = Plot[String, Rational, UnittedQuantity[Information, Double], D](
  () => List(("h", hm)),
  connect = true,
  drawKey = false,
  colorOf = _ => Color.black,
  xAxis = Some(0d *: bitDouble),
  xAxisLabel = Some("p(x='HEAD)"),
  yAxis = Some(Rational(0)),
  yAxisLabel = Some("H"),
  title = Some("Entropy"))
```

Create the SVG

```scala mdoc
import axle.web._

svg(plot, "coinentropy.svg")
```

The result is the classic Claude Shannon graph

![coin entropy](/tutorial/images/coinentropy.svg)

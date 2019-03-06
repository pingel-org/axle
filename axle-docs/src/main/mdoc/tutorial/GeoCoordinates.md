---
layout: page
title: Geo Coordinates
permalink: /tutorial/geo_coordinates/
---

Imports and implicits

```scala mdoc:silent
import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.algebra.Field
import spire.algebra.Trig
import spire.algebra.NRoot

import axle._
import axle.quanta._
import axle.algebra.GeoCoordinates
import axle.jung.directedGraphJung
import axle.algebra.modules.doubleRationalModule

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val trigDouble: Trig[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

implicit val angleConverter = Angle.converterGraphK2[Double, DirectedSparseGraph]
import angleConverter.°
```

Locations of SFO and HEL airports:

```scala mdoc
val sfo = GeoCoordinates(37.6189 *: °, 122.3750 *: °)

val hel = GeoCoordinates(60.3172 *: °, -24.9633 *: °)
```

Import the `LengthSpace`

```scala mdoc
import axle.algebra.GeoCoordinates.geoCoordinatesLengthSpace
```

Use it to compute the points at 10% increments from SFO to HEL

```scala mdoc
(0 to 10).map(i => geoCoordinatesLengthSpace.onPath(sfo, hel, i / 10d)) map { mp => mp.show } mkString("\n")
```

![SFO to HEL](/tutorial/images/sfo_hel.png)

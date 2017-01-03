---
layout: page
title: Geo Coordinates
permalink: /tutorial/geo_coordinates/
---

Imports and implicits

```tut:silent
import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.implicits._
import spire.implicits.DoubleAlgebra
import axle._
import axle.quanta._
import axle.algebra.GeoCoordinates
import axle.jung.directedGraphJung
import axle.algebra.modules.doubleRationalModule

implicit val angleConverter = Angle.converterGraphK2[Double, DirectedSparseGraph]
import angleConverter.°
```

Locations of SFO and HEL airports:

```tut:book
val sfo = GeoCoordinates(37.6189 *: °, 122.3750 *: °)

val hel = GeoCoordinates(60.3172 *: °, -24.9633 *: °)
```

Import the `LengthSpace`

```tut:book
import axle.algebra.GeoCoordinates.geoCoordinatesLengthSpace
```

Use it to compute the points at 10% increments from SFO to HEL

```tut:book
(0 to 10).map(i => geoCoordinatesLengthSpace.onPath(sfo, hel, i / 10d)) map { mp => string(mp) } mkString("\n")
```

![SFO to HEL](/tutorial/images/sfo_hel.png)


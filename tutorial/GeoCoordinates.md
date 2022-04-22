---
layout: page
title: Geo Coordinates
permalink: /tutorial/geo_coordinates/
---

Imports and implicits

```scala
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

```scala
val sfo = GeoCoordinates(37.6189 *: °, 122.3750 *: °)
// sfo: GeoCoordinates[Double] = GeoCoordinates(
//   latitude = UnittedQuantity(
//     magnitude = 37.6189,
//     unit = UnitOfMeasurement(
//       name = "degree",
//       symbol = "°",
//       wikipediaUrl = Some(value = "http://en.wikipedia.org/wiki/Degree_(angle)")
//     )
//   ),
//   longitude = UnittedQuantity(
//     magnitude = 122.375,
//     unit = UnitOfMeasurement(
//       name = "degree",
//       symbol = "°",
//       wikipediaUrl = Some(value = "http://en.wikipedia.org/wiki/Degree_(angle)")
//     )
//   )
// )

val hel = GeoCoordinates(60.3172 *: °, -24.9633 *: °)
// hel: GeoCoordinates[Double] = GeoCoordinates(
//   latitude = UnittedQuantity(
//     magnitude = 60.3172,
//     unit = UnitOfMeasurement(
//       name = "degree",
//       symbol = "°",
//       wikipediaUrl = Some(value = "http://en.wikipedia.org/wiki/Degree_(angle)")
//     )
//   ),
//   longitude = UnittedQuantity(
//     magnitude = -24.9633,
//     unit = UnitOfMeasurement(
//       name = "degree",
//       symbol = "°",
//       wikipediaUrl = Some(value = "http://en.wikipedia.org/wiki/Degree_(angle)")
//     )
//   )
// )
```

Import the `LengthSpace`

```scala
import axle.algebra.GeoCoordinates.geoCoordinatesLengthSpace
```

Use it to compute the points at 10% increments from SFO to HEL

```scala
(0 to 10).map(i => geoCoordinatesLengthSpace.onPath(sfo, hel, i / 10d)) map { mp => mp.show } mkString("\n")
// res0: String = """37.618900000000004° N 122.37500000000003° W
// 45.13070460867812° N 119.34966960499106° W
// 52.538395227224065° N 115.40855064022753° W
// 59.76229827032038° N 109.88311454897514° W
// 66.62843399359917° N 101.39331801935985° W
// 72.70253233457194° N 86.91316673834633° W
// 76.8357649372965° N 61.093630209243706° W
// 77.01752181288721° N 25.892878424459116° W
// 73.11964173748505° N -0.9862308621078928° W
// 67.1423066577233° N -16.143753987066464° W
// 60.3172° N -24.9633° W"""
```

![SFO to HEL](/tutorial/images/sfo_hel.png)

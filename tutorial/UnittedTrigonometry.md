---
layout: page
title: Unitted Trigonometry
permalink: /tutorial/unitted_trigonometry/
---

Versions of the trigonometric functions sine, cosine, and tangent, require that the arguments are Angles.

## Examples

Examples of the functions

Imports and implicits

```scala
import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.algebra.Field
import spire.algebra.Trig

import axle.math._
import axle.quanta.Angle
import axle.quanta.UnitOfMeasurement
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val trigDouble: Trig[Double] = spire.implicits.DoubleAlgebra

implicit val angleConverter = Angle.converterGraphK2[Double, DirectedSparseGraph]

import angleConverter.degree
import angleConverter.radian
```

Usage

```scala
cosine(10d *: degree)
// res0: Double = 0.984807753012208

sine(3d *: radian)
// res1: Double = 0.1411200080598672

tangent(40d *: degree)
// res2: Double = 0.8390996311772799
```

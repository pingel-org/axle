---
layout: page
title: Logistic Map
permalink: /tutorial/logistic_map/
---

See the wikipedia page on [Logistic Map](https://en.wikipedia.org/wiki/Logistic_map) function

Create data for a range of the logistic map function

```scala mdoc:silent
import java.util.TreeSet
import cats.implicits._
import spire.math.abs
import spire.implicits.DoubleAlgebra
import axle.math.logisticMap
import axle.{ orbit, applyK }
import axle.visualize._
import axle.awt._
```

```scala mdoc
val memo = collection.mutable.Map.empty[Double, TreeSet[Double]]

def doubleClose(z: Double)(y: Double) = abs(z - y) < 1e-6

def f(λ: Double, maxX: Double, maxY: Double, minY: Double): Boolean = {
  val f = logisticMap(λ)
  val set = memo.get(λ).getOrElse {
    val set = new TreeSet[Double]()
    orbit(f, applyK(f, 0.3, 100000), doubleClose) foreach { set.add }
    memo += λ -> set
    set
  }
  !set.tailSet(minY).headSet(maxY).isEmpty
}
```

Define a "value to color" function.

```scala mdoc
val v2c = (v: Boolean) => if (v) Color.black else Color.white
```

Define a `PixelatedColoredArea` to show a range of Logistic Map.

```scala mdoc
val pca = PixelatedColoredArea(f, v2c, 4000, 4000, 2.9, 4d, 0d, 1d)
```

Create the PNG

```scala mdoc
png(pca, "logMap.png")
```

![Logistic Map](/tutorial/images/logMap.png)

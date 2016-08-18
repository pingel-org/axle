Bar Charts
==========

Two-dimensional bar charts.

Example
-------

The dataset:

```tut
val sales = Map(
  "apple" -> 83.8,
  "banana" -> 77.9,
  "coconut" -> 10.1
)
```

Can be visualized as a bar chart with:

```tut
import spire.implicits.DoubleAlgebra
import spire.implicits.StringOrder
import axle.visualize.BarChart

val chart = BarChart[String, Double, Map[String, Double]](
  sales,
  title = Some("fruit sales")
)

import axle.web._
svg(chart, "fruitsales.svg")
```

![fruit sales](../images/fruitsales.svg)

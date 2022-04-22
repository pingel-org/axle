---
layout: page
title: Mean Average Precision at K
permalink: /tutorial/map_at_k/
---

See the page on [mean average precision](https://www.kaggle.com/wiki/MeanAveragePrecision) at Kaggle

```scala
import spire.math.Rational
import axle.ml.RankedClassifierPerformance._
```

Examples (from [benhamner/Metrics](https://github.com/benhamner/Metrics))

```scala
meanAveragePrecisionAtK[Int, Rational](List(1 until 5), List(1 until 5), 3)
// res0: Rational = 1
```

```scala
meanAveragePrecisionAtK[Int, Rational](List(List(1, 3, 4), List(1, 2, 4), List(1, 3)), List(1 until 6, 1 until 6, 1 until 6), 3)
// res1: Rational = 37/54
```

```scala
meanAveragePrecisionAtK[Int, Rational](List(1 until 6, 1 until 6), List(List(6, 4, 7, 1, 2), List(1, 1, 1, 1, 1)), 5)
// res2: Rational = 13/50
```

```scala
meanAveragePrecisionAtK[Int, Rational](List(List(1, 3), List(1, 2, 3), List(1, 2, 3)), List(1 until 6, List(1, 1, 1), List(1, 2, 1)), 3)
// res3: Rational = 11/18
```

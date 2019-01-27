---
layout: page
title: Mean Average Precision at K
permalink: /tutorial/map_at_k/
---

See the page on [mean average precision](https://www.kaggle.com/wiki/MeanAveragePrecision) at Kaggle

```scala mdoc:silent
import spire.math.Rational
import axle.ml.RankedClassifierPerformance._
```

Examples (from [benhamner/Metrics](https://github.com/benhamner/Metrics))

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(1 until 5), List(1 until 5), 3)

// should be Rational.one
```

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(List(1, 3, 4), List(1, 2, 4), List(1, 3)), List(1 until 6, 1 until 6, 1 until 6), 3)

// should be Rational(37, 54) aka 0.6851851851851851
```

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(1 until 6, 1 until 6), List(List(6, 4, 7, 1, 2), List(1, 1, 1, 1, 1)), 5)

// should be Rational(13, 50)
```

```scala mdoc
meanAveragePrecisionAtK[Int, Rational](List(List(1, 3), List(1, 2, 3), List(1, 2, 3)), List(1 until 6, List(1, 1, 1), List(1, 2, 1)), 3)

// should be Rational(11, 18) aka 0.611111111111111
```

---
layout: page
title: Edit Distance
permalink: /tutorial/edit_distance/
---

See the Wikipedia page on [Edit distance](https://en.wikipedia.org/wiki/Edit_distance)

## Levenshtein

See the Wikipedia page on [Levenshtein distance](https://en.wikipedia.org/wiki/Levenshtein_distance)

Imports and implicits

```scala
import org.jblas.DoubleMatrix

import cats.implicits._

import spire.algebra.Ring
import spire.algebra.NRoot

import axle._
import axle.nlp.Levenshtein
import axle.jblas._

implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
implicit val nrootInt: NRoot[Int] = spire.implicits.IntAlgebra
implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]
implicit val space = Levenshtein[IndexedSeq, Char, DoubleMatrix, Int]()
```

Usage

```scala
space.distance("the quick brown fox", "the quik brown fax")
// res0: Int = 2
```

Usage with spire's `distance` operator

Imports

```scala
import axle.algebra.metricspaces.wrappedStringSpace
import spire.syntax.metricSpace.metricSpaceOps
```

Usage

```scala
"the quick brown fox" distance "the quik brown fax"
// res1: Int = 2

"the quick brown fox" distance "the quik brown fox"
// res2: Int = 1

"the quick brown fox" distance "the quick brown fox"
// res3: Int = 0
```

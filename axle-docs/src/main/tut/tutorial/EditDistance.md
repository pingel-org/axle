---
layout: page
title: Edit Distance
permalink: /tutorial/edit_distance/
---

See the Wikipedia page on [Edit distance](https://en.wikipedia.org/wiki/Edit_distance)

Levenshtein
-----------

See the Wikipedia page on [Levenshtein distance](https://en.wikipedia.org/wiki/Levenshtein_distance)

Imports and implicits

```tut:book:silent
import org.jblas.DoubleMatrix

import cats.implicits._

import spire.implicits.IntAlgebra
import spire.implicits.CharAlgebra

import axle._
import axle.nlp._
import axle.nlp.Levenshtein
import axle.jblas._

implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]
implicit val space = Levenshtein[IndexedSeq[Char], Char, DoubleMatrix, Int]()
```

Usage

```tut:book
space.distance("the quick brown fox", "the quik brown fax")
```

Usage with spire's `distance` operator

Imports

```tut:book:silent
import axle.algebra.wrappedStringSpace
import spire.syntax.metricSpace.metricSpaceOps
```

Usage

```tut:book
"the quick brown fox" distance "the quik brown fax"

"the quick brown fox" distance "the quik brown fox"

"the quick brown fox" distance "the quick brown fox"
```

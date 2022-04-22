---
layout: page
title: Smith-Waterman
permalink: /tutorial/smith_waterman/
---

See the Wikipedia page on the
[Smith-Waterman](https://en.wikipedia.org/wiki/Smith%E2%80%93Waterman_algorithm) algorithm.

## Example

Imports and implicits

```scala
import org.jblas.DoubleMatrix

import cats.implicits._

import spire.algebra.Ring
import spire.algebra.NRoot

import axle.bio._
import SmithWatermanDefaults._
import SmithWaterman.optimalAlignment

implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
implicit val nrootInt: NRoot[Int] = spire.implicits.IntAlgebra
implicit val laJblasInt = axle.jblas.linearAlgebraDoubleMatrix[Int]
```

Setup

```scala
val dna3 = "ACACACTA"
// dna3: String = "ACACACTA"
val dna4 = "AGCACACA"
// dna4: String = "AGCACACA"
```

Align the sequences

```scala
val swAlignment = optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Int](
  dna3, dna4, w, mismatchPenalty, gap)
// swAlignment: (IndexedSeq[Char], IndexedSeq[Char]) = (
//   Vector('A', '-', 'C', 'A', 'C', 'A', 'C', 'T', 'A'),
//   Vector('A', 'G', 'C', 'A', 'C', 'A', 'C', '-', 'A')
// )
```

Compute distance of the sequences

```scala
val space = SmithWatermanSimilaritySpace[IndexedSeq, Char, DoubleMatrix, Int, Int](w, mismatchPenalty)
// space: SmithWatermanSimilaritySpace[IndexedSeq, Char, DoubleMatrix, Int, Int] = SmithWatermanSimilaritySpace(
//   w = <function3>,
//   mismatchPenalty = -1
// )

space.similarity(dna3, dna4)
// res0: Int = 12
```

---
layout: page
title: Needleman-Wunsch
permalink: /tutorial/needleman_wunsch/
---

See the Wikipedia page on the
[Needleman-Wunsch](https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm) algorithm.

## Example

Imports and implicits

```scala
import org.jblas.DoubleMatrix

import cats.implicits._

import spire.algebra.Ring
import spire.algebra.NRoot
import spire.algebra.Field

import axle.algebra._
import axle.algebra.functors._
import axle.bio._
import NeedlemanWunsch.optimalAlignment
import NeedlemanWunschDefaults._

implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
import axle.algebra.modules.doubleIntModule

implicit val laJblasInt = {
  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  axle.jblas.linearAlgebraDoubleMatrix[Double]
}
```

```scala
val dna1 = "ATGCGGCC"
// dna1: String = "ATGCGGCC"
val dna2 = "ATCGCCGG"
// dna2: String = "ATCGCCGG"
```

Setup

```scala
val nwAlignment = optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Double](
  dna1, dna2, similarity, gap, gapPenalty)
// nwAlignment: (IndexedSeq[Char], IndexedSeq[Char]) = (
//   Vector('A', 'T', 'G', 'C', 'G', 'G', 'C', 'C', '-', '-'),
//   Vector('A', 'T', '-', 'C', '-', 'G', 'C', 'C', 'G', 'G')
// )
```

Score aligment

```scala
import NeedlemanWunsch.alignmentScore

alignmentScore(nwAlignment._1, nwAlignment._2, gap, similarity, gapPenalty)
// res0: Double = 32.0
```

Compute distance

```scala
val space = NeedlemanWunschSimilaritySpace[IndexedSeq, Char, DoubleMatrix, Int, Double](similarity, gapPenalty)
// space: NeedlemanWunschSimilaritySpace[IndexedSeq, Char, DoubleMatrix, Int, Double] = NeedlemanWunschSimilaritySpace(
//   baseSimilarity = <function2>,
//   gapPenalty = -5.0
// )

space.similarity(dna1, dna2)
// res1: Double = 32.0
```

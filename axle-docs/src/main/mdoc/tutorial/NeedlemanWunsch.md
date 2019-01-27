---
layout: page
title: Needleman-Wunsch
permalink: /tutorial/needleman_wunsch/
---

See the Wikipedia page on the
[Needleman-Wunsch](https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm) algorithm.

## Example

Setup:

```scala mdoc:silent
import org.jblas.DoubleMatrix
import cats.implicits._
import axle.bio._
import NeedlemanWunsch.alignmentScore
import NeedlemanWunsch.optimalAlignment
import NeedlemanWunschDefaults._

implicit val laJblasDouble = {
  import spire.implicits.DoubleAlgebra
  axle.jblas.linearAlgebraDoubleMatrix[Double]
}

val dna1 = "ATGCGGCC"
val dna2 = "ATCGCCGG"
```

DNA Sequence Alignment

```scala mdoc
val nwAlignment =
  optimalAlignment[IndexedSeq[Char], Char, DoubleMatrix, Int, Double](
    dna1, dna2, similarity, gap, gapPenalty)
```

Score aligment

```scala mdoc
import NeedlemanWunsch.alignmentScore

alignmentScore(nwAlignment._1, nwAlignment._2, gap, similarity, gapPenalty)
```

Compute distance

```scala mdoc
val space = NeedlemanWunschMetricSpace[IndexedSeq[Char], Char, DoubleMatrix, Int, Double](similarity, gapPenalty)

space.distance(dna1, dna2)
```

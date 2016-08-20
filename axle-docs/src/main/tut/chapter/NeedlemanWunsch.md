
Needleman-Wunsch
================

See the Wikipedia page on the 
<a href="https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm">Needleman-Wunsch</a> algorithm.

Example
-------

Setup:

```tut:silent
import axle.bio._
import org.jblas.DoubleMatrix
import NeedlemanWunsch.alignmentScore
import NeedlemanWunsch.optimalAlignment
import NeedlemanWunsch.Default._

implicit val laJblasDouble = {
  import spire.implicits.DoubleAlgebra
  axle.jblas.linearAlgebraDoubleMatrix[Double]
}

val dna1 = "ATGCGGCC"
val dna2 = "ATCGCCGG"
```

DNA Sequence Alignment

```tut
val nwAlignment =
  optimalAlignment[IndexedSeq[Char], Char, DoubleMatrix, Int, Double](
    dna1, dna2, similarity, gap, gapPenalty)
```

Score aligment

```tut
import NeedlemanWunsch.alignmentScore

alignmentScore(nwAlignment._1, nwAlignment._2, gap, similarity, gapPenalty)
```

Compute distance

```tut
val space = NeedlemanWunschMetricSpace[IndexedSeq[Char], Char, DoubleMatrix, Int, Double](similarity, gapPenalty)

space.distance(dna1, dna2)
```

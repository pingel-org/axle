
Needleman-Wunsch
================

See the Wikipedia page on the 
<a href="https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm">Needleman-Wunsch</a> algorithm.

Example
-------

Setup:

```scala
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

```scala
scala> val nwAlignment =
     |   optimalAlignment[IndexedSeq[Char], Char, DoubleMatrix, Int, Double](
     |     dna1, dna2, similarity, gap, gapPenalty)
nwAlignment: (IndexedSeq[Char], IndexedSeq[Char]) = (Vector(A, T, G, C, G, G, C, C, -, -),Vector(A, T, -, C, -, G, C, C, G, G))
```

Score aligment

```scala
scala> import NeedlemanWunsch.alignmentScore
import NeedlemanWunsch.alignmentScore

scala> alignmentScore(nwAlignment._1, nwAlignment._2, gap, similarity, gapPenalty)
res2: Double = 32.0
```

Compute distance

```scala
scala> val space = NeedlemanWunschMetricSpace[IndexedSeq[Char], Char, DoubleMatrix, Int, Double](similarity, gapPenalty)
space: axle.bio.NeedlemanWunschMetricSpace[IndexedSeq[Char],Char,org.jblas.DoubleMatrix,Int,Double] = NeedlemanWunschMetricSpace(<function2>,-5.0)

scala> space.distance(dna1, dna2)
res3: Double = 32.0
```

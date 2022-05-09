# Biology

## Needleman-Wunsch

See the Wikipedia page on the
[Needleman-Wunsch](https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm) algorithm.

### Example

Imports and implicits

```scala mdoc:silent
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

```scala mdoc:silent
val dna1 = "ATGCGGCC"
val dna2 = "ATCGCCGG"
```

Setup

```scala mdoc
val nwAlignment = optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Double](
  dna1, dna2, similarity, gap, gapPenalty)
```

Score aligment

```scala mdoc
import NeedlemanWunsch.alignmentScore

alignmentScore(nwAlignment._1, nwAlignment._2, gap, similarity, gapPenalty)
```

Compute distance

```scala mdoc
val space = NeedlemanWunschSimilaritySpace[IndexedSeq, Char, DoubleMatrix, Int, Double](similarity, gapPenalty)

space.similarity(dna1, dna2)
```

## Smith-Waterman

See the Wikipedia page on the
[Smith-Waterman](https://en.wikipedia.org/wiki/Smith%E2%80%93Waterman_algorithm) algorithm.

### Smith-Waterman Example

Imports and implicits

```scala mdoc:silent:reset
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

```scala mdoc:silent
val dna3 = "ACACACTA"
val dna4 = "AGCACACA"
```

Align the sequences

```scala mdoc
val swAlignment = optimalAlignment[IndexedSeq, Char, DoubleMatrix, Int, Int](
  dna3, dna4, w, mismatchPenalty, gap)
```

Compute distance of the sequences

```scala mdoc
val space = SmithWatermanSimilaritySpace[IndexedSeq, Char, DoubleMatrix, Int, Int](w, mismatchPenalty)

space.similarity(dna3, dna4)
```

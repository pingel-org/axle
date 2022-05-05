# Smith-Waterman

See the Wikipedia page on the
[Smith-Waterman](https://en.wikipedia.org/wiki/Smith%E2%80%93Waterman_algorithm) algorithm.

## Example

Imports and implicits

```scala mdoc:silent
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

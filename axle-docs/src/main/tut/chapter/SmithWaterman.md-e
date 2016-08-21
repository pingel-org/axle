Smith-Waterman
==============

See the Wikipedia page on the
<a href="https://en.wikipedia.org/wiki/Smith%E2%80%93Waterman_algorithm">Smith-Waterman</a> algorithm.

Example
-------

Imports and implicits

```book:silent
import axle.bio._
import SmithWaterman.Default._
import SmithWaterman.optimalAlignment
import spire.implicits.IntAlgebra
implicit val laJblasInt = axle.jblas.linearAlgebraDoubleMatrix[Int]
import org.jblas.DoubleMatrix
```

Setup

```book
val dna3 = "ACACACTA"
val dna4 = "AGCACACA"
```

Align the sequences

```book
val swAlignment = optimalAlignment[IndexedSeq[Char], Char, DoubleMatrix, Int, Int](
  dna3, dna4, w, mismatchPenalty, gap)
```

Compute distance of the sequences

```book
import spire.implicits.DoubleAlgebra

val space = SmithWatermanMetricSpace[IndexedSeq[Char], Char, DoubleMatrix, Int, Int](w, mismatchPenalty)

space.distance(dna3, dna4)
```

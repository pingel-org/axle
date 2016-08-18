Smith-Waterman
==============

See the Wikipedia page on the
<a href="https://en.wikipedia.org/wiki/Smith%E2%80%93Waterman_algorithm">Smith-Waterman</a> algorithm.

Example
-------

Imports and implicits

```scala
import axle.bio._
import SmithWaterman.Default._
import SmithWaterman.optimalAlignment
import spire.implicits.IntAlgebra
implicit val laJblasInt = axle.jblas.linearAlgebraDoubleMatrix[Int]
import org.jblas.DoubleMatrix
```

Setup

```scala
scala> val dna3 = "ACACACTA"
dna3: String = ACACACTA

scala> val dna4 = "AGCACACA"
dna4: String = AGCACACA
```

Align the sequences

```scala
scala> val swAlignment = optimalAlignment[IndexedSeq[Char], Char, DoubleMatrix, Int, Int](
     |   dna3, dna4, w, mismatchPenalty, gap)
swAlignment: (IndexedSeq[Char], IndexedSeq[Char]) = (Vector(A, -, C, A, C, A, C, T, A),Vector(A, G, C, A, C, A, C, -, A))
```

Compute distance of the sequences

```scala
scala> import spire.implicits.DoubleAlgebra
import spire.implicits.DoubleAlgebra

scala> val space = SmithWatermanMetricSpace[IndexedSeq[Char], Char, DoubleMatrix, Int, Int](w, mismatchPenalty)
space: axle.bio.SmithWatermanMetricSpace[IndexedSeq[Char],Char,org.jblas.DoubleMatrix,Int,Int] = SmithWatermanMetricSpace(<function3>,-1)

scala> space.distance(dna3, dna4)
res0: Int = 12
```

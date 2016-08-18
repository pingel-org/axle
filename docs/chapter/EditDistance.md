Edit Distance
=============

See the Wikipedia page on <a href="https://en.wikipedia.org/wiki/Edit_distance">Edit distance</a>

Levenshtein
-----------

See the Wikipedia page on <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein distance</a>

Imports and implicits

```scala
import axle._
import axle.nlp._
import axle.nlp.Levenshtein
import spire.implicits.IntAlgebra
import spire.implicits.CharAlgebra
import org.jblas.DoubleMatrix
import axle.jblas._
import spire.algebra.Eq

implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]
implicit val space = Levenshtein[IndexedSeq[Char], Char, DoubleMatrix, Int]()
```

Usage

```scala
scala> space.distance("the quick brown fox", "the quik brown fax")
res1: Int = 2
```

Usage with spire's `distance` operator

Imports

```scala
import axle.algebra.wrappedStringSpace
import spire.syntax.metricSpace.metricSpaceOps
```

Usage

```scala
scala> "the quick brown fox" distance "the quik brown fax"
res2: Int = 2

scala> "the quick brown fox" distance "the quik brown fox"
res3: Int = 1

scala> "the quick brown fox" distance "the quick brown fox"
res4: Int = 0
```

Edit Distance
=============

See the Wikipedia page on <a href="https://en.wikipedia.org/wiki/Edit_distance">Edit distance</a>

Levenshtein
-----------

See the Wikipedia page on <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein distance</a>

Imports and implicits

```tut:silent
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

```tut
space.distance("the quick brown fox", "the quik brown fax")
```

Usage with spire's `distance` operator

Imports

```tut:silent
import axle.algebra.wrappedStringSpace
import spire.syntax.metricSpace.metricSpaceOps
```

Usage

```tut
"the quick brown fox" distance "the quik brown fax"

"the quick brown fox" distance "the quik brown fox"

"the quick brown fox" distance "the quick brown fox"
```

# Logistic Map

See the wikipedia page on [Logistic Map](https://en.wikipedia.org/wiki/Logistic_map) function

Create data for a range of the logistic map function

```scala mdoc:silent
import spire.algebra._

val initial = 0.3

import java.util.TreeSet
val memo = collection.mutable.Map.empty[Double, TreeSet[Double]]
implicit val ringDouble: Ring[Double] = spire.implicits.DoubleAlgebra

def lhsContainsMark(minX: Double, maxX: Double, maxY: Double, minY: Double): Boolean = {
  val λ = minX
  val f = axle.math.logisticMap(λ)
  val set = memo.get(λ).getOrElse {
    val set = new TreeSet[Double]()
    axle.algebra.applyForever(f, initial).drop(10000).take(200) foreach { set.add }
    memo += minX -> set
    set
  }
  !set.tailSet(minY).headSet(maxY).isEmpty
}
```

Define a "value to color" function.

```scala mdoc:silent
import axle.visualize._

val v2c: Boolean => Color =
  (v: Boolean) => if (v) Color.black else Color.white
```

Define a `PixelatedColoredArea` to show a range of Logistic Map.

```scala mdoc:silent
import cats.implicits._

val pca = PixelatedColoredArea[Double, Double, Boolean](
  lhsContainsMark,
  v2c,
  width = 500,
  height = 500,
  minX = 2.9,
  maxX = 4d,
  minY = 0d,
  maxY = 1d
)
```

Create the PNG

```scala mdoc:silent
import axle.awt._
import cats.effect._

pca.png[IO]("@DOCWD@/images/logMap.png").unsafeRunSync()
```

![Logistic Map](/images/logMap.png)

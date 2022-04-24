# Logistic Map

See the wikipedia page on [Logistic Map](https://en.wikipedia.org/wiki/Logistic_map) function

Create data for a range of the logistic map function

```scala mdoc
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

```scala mdoc
import axle.visualize._

val v2c = (v: Boolean) => if (v) Color.black else Color.white
```

Define a `PixelatedColoredArea` to show a range of Logistic Map.

```scala mdoc
import cats.implicits._

val pca = PixelatedColoredArea[Double, Double, Boolean](
  lhsContainsMark,
  v2c,
  4000,    // width
  4000,    // height
  2.9, 4d, // x range
  0d, 1d   // y range
)
```

Create the PNG

```scala mdoc
import axle.awt._
import cats.effect._

pca.png[IO]("@DOCWD@/images/logMap.png").unsafeRunSync()
```

![Logistic Map](/images/logMap.png)

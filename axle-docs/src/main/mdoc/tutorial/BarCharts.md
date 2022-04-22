# Bar Charts

Two-dimensional bar charts.

## Example

The dataset:

```scala mdoc
val sales = Map(
  "apple" -> 83.8,
  "banana" -> 77.9,
  "coconut" -> 10.1
)
```

Define a bar chart visualization

```scala mdoc:silent
import spire.algebra.Field
import cats.implicits._
import axle.visualize.BarChart
import axle.visualize.Color.lightGray
```

```scala mdoc
implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

val chart = BarChart[String, Double, Map[String, Double], String](
  () => sales,
  title = Some("fruit sales"),
  hoverOf = (c: String) => Some(c),
  linkOf = (c: String) => Some((new java.net.URL(s"http://wikipedia.org/wiki/$c"), lightGray))
)
```

Create the SVG

```scala mdoc:silent
import axle.web._
import cats.effect._
```

```scala mdoc
chart.svg[IO]("fruitsales.svg").unsafeRunSync()
```

<object data="/tutorial/images/fruitsales.svg" type="image/svg+xml" alt="fruit sales"/>

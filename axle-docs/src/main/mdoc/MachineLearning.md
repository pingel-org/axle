# Machine Learning

## Naive Bayes

Naïve Bayes

### Example: Tennis and Weather

```scala mdoc:silent:reset
case class Tennis(outlook: String, temperature: String, humidity: String, wind: String, play: Boolean)

val events = List(
  Tennis("Sunny", "Hot", "High", "Weak", false),
  Tennis("Sunny", "Hot", "High", "Strong", false),
  Tennis("Overcast", "Hot", "High", "Weak", true),
  Tennis("Rain", "Mild", "High", "Weak", true),
  Tennis("Rain", "Cool", "Normal", "Weak", true),
  Tennis("Rain", "Cool", "Normal", "Strong", false),
  Tennis("Overcast", "Cool", "Normal", "Strong", true),
  Tennis("Sunny", "Mild", "High", "Weak", false),
  Tennis("Sunny", "Cool", "Normal", "Weak", true),
  Tennis("Rain", "Mild", "Normal", "Weak", true),
  Tennis("Sunny", "Mild", "Normal", "Strong", true),
  Tennis("Overcast", "Mild", "High", "Strong", true),
  Tennis("Overcast", "Hot", "Normal", "Weak", true),
  Tennis("Rain", "Mild", "High", "Strong", false))
```

Build a classifier to predict the Boolean feature 'play' given all the other features of the observations

```scala mdoc:silent
import cats.implicits._

import spire.math._

import axle._
import axle.probability._
import axle.ml.NaiveBayesClassifier
```

```scala mdoc:silent
val classifier = NaiveBayesClassifier[Tennis, String, Boolean, List, Rational](
  events,
  List(
    (Variable[String]("Outlook") -> Vector("Sunny", "Overcast", "Rain")),
    (Variable[String]("Temperature") -> Vector("Hot", "Mild", "Cool")),
    (Variable[String]("Humidity") -> Vector("High", "Normal", "Low")),
    (Variable[String]("Wind") -> Vector("Weak", "Strong"))),
  (Variable[Boolean]("Play") -> Vector(true, false)),
  (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
  (t: Tennis) => t.play)
```

Use the classifier to predict:

```scala mdoc
events map { datum => datum.toString + "\t" + classifier(datum) } mkString("\n")
```

Measure the classifier's performance

```scala mdoc
import axle.ml.ClassifierPerformance

ClassifierPerformance[Rational, Tennis, List](events, classifier, _.play).show
```

See [Precision and Recall](http://en.wikipedia.org/wiki/Precision_and_recall)
for the definition of the performance metrics.

## Genetic Algorithms

See the wikipedia page on [Genetic Algorithms](https://en.wikipedia.org/wiki/Genetic_algorithm)

## Example: Rabbits

Consider a `Rabbit` class

```scala mdoc:silent:reset
case class Rabbit(a: Int, b: Double, c: Double, d: Double, e: Double, f: Double, g: Double, h: Double)
```

Define the `Species` for a Genetic Algorithm, which requires a random generator and
a fitness function.

```scala mdoc:silent
import shapeless._

val gen = Generic[Rabbit]

import axle.ml._

import scala.util.Random.nextDouble
import scala.util.Random.nextInt

implicit val rabbitSpecies = new Species[gen.Repr] {

  def random(rg: spire.random.Generator): gen.Repr = {

    val rabbit = Rabbit(
      1 + nextInt(2),
      5 + 20 * nextDouble(),
      1 + 4 * nextDouble(),
      3 + 10 * nextDouble(),
      10 + 5 * nextDouble(),
      2 + 2 * nextDouble(),
      3 + 5 * nextDouble(),
      2 + 10 * nextDouble())
    gen.to(rabbit)
  }

  def fitness(rg: gen.Repr): Double = {
    val rabbit = gen.from(rg)
    import rabbit._
    a * 100 + 100.0 * b + 2.2 * (1.1 * c + 0.3 * d) + 1.3 * (1.4 * e - 3.1 * f + 1.3 * g) - 1.4 * h
  }

}
```

Run the genetic algorithm

```scala mdoc:silent
import cats.implicits._

val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)

val log = ga.run(spire.random.Generator.rng)
```

```scala mdoc
val winner = log.winners.last
```

Plot the min, average, and max fitness function by generation

```scala mdoc:silent
import scala.collection.immutable.TreeMap
import axle.visualize._

val plot = Plot[String, Int, Double, TreeMap[Int,Double]](
  () => List("min" -> log.mins, "ave" -> log.aves, "max" -> log.maxs),
  connect = true,
  colorOf = (label: String) => label match {
    case "min" => Color.black
    case "ave" => Color.blue
    case "max" => Color.green },
  title = Some("GA Demo"),
  xAxis = Some(0d),
  xAxisLabel = Some("generation"),
  yAxis = Some(0),
  yAxisLabel = Some("fitness"))
```

Render to an SVG file

```scala mdoc
import axle.web._
import cats.effect._

plot.svg[IO]("@DOCWD@/images/ga.svg").unsafeRunSync()
```

![ga](/images/ga.svg)

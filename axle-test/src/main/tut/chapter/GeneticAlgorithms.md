
Genetic Algorithms
==================

See the wikipedia page on <a href="https://en.wikipedia.org/wiki/Genetic_algorithm">Genetic Algorithms</a>

Example
-------

```tut
case class Rabbit(a: Int, b: Double, c: Double, d: Double, e: Double, f: Double, g: Double, h: Double)
```

Define a random rabbit generator and fitness function

```tut
import shapeless._
import syntax.singleton._
import record._
import util.Random.nextDouble
import util.Random.nextInt
import axle.ml._

val gen = Generic[Rabbit]

// val pMutation = 0.003

implicit val rabbitSpecies = new Species[gen.Repr] {

  def random() = {
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

```tut
import spire.implicits._

val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)

val log = ga.run()
```

Plot the min, average, and max fitness function by generation

```tut
import scala.collection.immutable.TreeMap
import axle.visualize._

val plot = Plot(
  List("min" -> log.mins, "ave" -> log.aves, "max" -> log.maxs),
  connect = true,
  title = Some("GA Demo"),
  xAxis = Some(0d),
  xAxisLabel = Some("generation"),
  yAxis = Some(0),
  yAxisLabel = Some("fitness"))

import axle.web._
svg(plot, "ga.svg")
```

![ga](../images/ga.svg)
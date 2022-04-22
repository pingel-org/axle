---
layout: page
title: Genetic Algorithms
permalink: /tutorial/genetic_algorithms/
---

See the wikipedia page on [Genetic Algorithms](https://en.wikipedia.org/wiki/Genetic_algorithm)

## Example

Consider a `Rabbit` class

```scala
case class Rabbit(a: Int, b: Double, c: Double, d: Double, e: Double, f: Double, g: Double, h: Double)
```

Define the `Species` for a Genetic Algorithm, which requires a random generator and
a fitness function.

```scala
import shapeless._

val gen = Generic[Rabbit]
// gen: Generic[Rabbit]{type Repr = Int :: Double :: Double :: Double :: Double :: Double :: Double :: Double :: shapeless.HNil} = shapeless.Generic$$anon$1@9162985

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
// rabbitSpecies: AnyRef with Species[gen.Repr] = repl.MdocSession$App$$anon$1@17ebdf31
```

Run the genetic algorithm

```scala
import cats.implicits._

val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)
// ga: GeneticAlgorithm[gen.Repr, ops.hlist.Mapper.<refinement>.this.type.Out] = GeneticAlgorithm(
//   populationSize = 100,
//   numGenerations = 100
// )

val log = ga.run(spire.random.Generator.rng)
// log: GeneticAlgorithmLog[gen.Repr] = GeneticAlgorithmLog(
//   winners = Vector(
//     2 :: 24.8455680612464 :: 4.664145959427794 :: 12.8027036789789 :: 14.720087332661219 :: 2.0946534923295586 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.681061038512302 :: 9.869006983294096 :: 13.248132843280763 :: 2.0946534923295586 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.843563269700001 :: 12.8027036789789 :: 14.720087332661219 :: 2.0946534923295586 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.955669116957157 :: 12.059760684305044 :: 14.720087332661219 :: 2.0946534923295586 :: 4.5633300950859805 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.862945218926712 :: 11.666745875473485 :: 14.720087332661219 :: 2.0946534923295586 :: 7.958234092119779 :: 2.083538176780673 :: HNil,
//     2 :: 24.8455680612464 :: 1.2832648844477301 :: 11.666745875473485 :: 14.720087332661219 :: 2.0946534923295586 :: 7.958234092119779 :: 2.083538176780673 :: HNil,
//     2 :: 24.8455680612464 :: 4.843563269700001 :: 12.8027036789789 :: 14.720087332661219 :: 2.0946534923295586 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.627115558518435 :: 12.8027036789789 :: 14.720087332661219 :: 2.0946534923295586 :: 4.2578199274567 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.843563269700001 :: 12.8027036789789 :: 14.720087332661219 :: 2.0946534923295586 :: 3.76509808401785 :: 7.809220015140427 :: HNil,
//     2 :: 24.8455680612464 :: 4.664145959427794 :: 12.059760684305044 :: 14.720087332661219 :: 2.0946534923295586 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.843563269700001 :: 11.826170945997806 :: 14.720087332661219 :: 2.0946534923295586 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.955669116957157 :: 12.8027036789789 :: 14.720087332661219 :: 2.0946534923295586 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.962444578307583 :: 4.843563269700001 :: 12.8027036789789 :: 14.720087332661219 :: 2.0946534923295586 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.843563269700001 :: 12.8027036789789 :: 14.720087332661219 :: 3.888969052140059 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.955669116957157 :: 12.8027036789789 :: 14.720087332661219 :: 2.0946534923295586 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.8455680612464 :: 4.955669116957157 :: 12.8027036789789 :: 14.720087332661219 :: 3.636679188811211 :: 7.958234092119779 :: 2.0923251148191806 :: HNil,
//     2 :: 24.962444578307583 :: 4.843563269700001 :: 12.8027036789789 :: 14.72008...

val winner = log.winners.last
// winner: gen.Repr = 2 :: 24.8455680612464 :: 4.955669116957157 :: 12.8027036789789 :: 10.66445174146629 :: 2.0946534923295586 :: 7.259912118202424 :: 2.0923251148191806 :: HNil
```

Plot the min, average, and max fitness function by generation

```scala
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
// plot: Plot[String, Int, Double, TreeMap[Int, Double]] = Plot(
//   dataFn = <function0>,
//   connect = true,
//   drawKey = true,
//   width = 700,
//   height = 600,
//   border = 50,
//   pointDiameter = 4,
//   keyLeftPadding = 20,
//   keyTopPadding = 50,
//   keyWidth = 80,
//   fontName = "Courier New",
//   fontSize = 12,
//   bold = false,
//   titleFontName = "Palatino",
//   titleFontSize = 20,
//   colorOf = <function1>,
//   title = Some(value = "GA Demo"),
//   keyTitle = None,
//   xAxis = Some(value = 0.0),
//   xAxisLabel = Some(value = "generation"),
//   yAxis = Some(value = 0),
//   yAxisLabel = Some(value = "fitness")
// )
```

Render to an SVG file

```scala
import axle.web._
import cats.effect._

plot.svg[IO]("ga.svg").unsafeRunSync()
```

![ga](/tutorial/images/ga.svg)

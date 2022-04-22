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
// gen: Generic[Rabbit]{type Repr = Int :: Double :: Double :: Double :: Double :: Double :: Double :: Double :: shapeless.HNil} = shapeless.Generic$$anon$1@39699803

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
// rabbitSpecies: AnyRef with Species[gen.Repr] = repl.MdocSession$App$$anon$1@284d3e75
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
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 7.1391744733472 :: 14.76695524410434 :: 2.050251366217017 :: 4.726494970588359 :: 2.0040761321501614 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 11.114311298528365 :: 14.76695524410434 :: 2.050251366217017 :: 4.534925294636501 :: 2.0040761321501614 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 11.114311298528365 :: 14.76695524410434 :: 2.050251366217017 :: 7.724578791844171 :: 2.0040761321501614 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 11.114311298528365 :: 14.76695524410434 :: 2.050251366217017 :: 7.724578791844171 :: 2.0040761321501614 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 8.705800192654785 :: 14.76695524410434 :: 2.050251366217017 :: 7.724578791844171 :: 2.2844338166224536 :: HNil,
//     2 :: 24.79907655811716 :: 4.243971733227008 :: 6.505041558634603 :: 14.76695524410434 :: 2.050251366217017 :: 4.534925294636501 :: 2.0040761321501614 :: HNil,
//     2 :: 24.988012710786442 :: 4.243971733227008 :: 8.705800192654785 :: 14.76695524410434 :: 2.050251366217017 :: 7.639695080468497 :: 2.0040761321501614 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 12.07906682430522 :: 14.76695524410434 :: 2.050251366217017 :: 7.639695080468497 :: 2.7743559743064967 :: HNil,
//     2 :: 24.79907655811716 :: 4.243971733227008 :: 10.341853338844087 :: 14.76695524410434 :: 2.050251366217017 :: 3.5276181691707365 :: 4.185217170843767 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 7.1391744733472 :: 14.539432144908861 :: 2.050251366217017 :: 7.639695080468497 :: 2.0040761321501614 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 10.271015764107673 :: 14.76695524410434 :: 2.050251366217017 :: 7.131746111738051 :: 2.0040761321501614 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 11.114311298528365 :: 14.76695524410434 :: 2.050251366217017 :: 7.692702744407835 :: 2.0040761321501614 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 11.114311298528365 :: 14.76695524410434 :: 2.050251366217017 :: 3.7204503368111546 :: 10.649094693677918 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 11.114311298528365 :: 14.76695524410434 :: 2.050251366217017 :: 7.724578791844171 :: 2.0040761321501614 :: HNil,
//     2 :: 12.790500723350025 :: 4.919466546117167 :: 10.271015764107673 :: 14.76695524410434 :: 2.050251366217017 :: 7.724578791844171 :: 2.0040761321501614 :: HNil,
//     2 :: 24.988012710786442 :: 4.919466546117167 :: 11.114311298528365 :: 14.76695524410434 :: 2.050251366217017 :: 4.534925294636501 :: 2.0040761321501614 :: H...

val winner = log.winners.last
// winner: gen.Repr = 2 :: 24.988012710786442 :: 4.919466546117167 :: 11.114311298528365 :: 14.76695524410434 :: 2.050251366217017 :: 7.692702744407835 :: 2.0040761321501614 :: HNil
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

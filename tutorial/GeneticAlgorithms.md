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
// gen: Generic[Rabbit]{type Repr = Int :: Double :: Double :: Double :: Double :: Double :: Double :: Double :: shapeless.HNil} = shapeless.Generic$$anon$1@5605a968

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
// rabbitSpecies: AnyRef with Species[gen.Repr] = repl.MdocSession$App$$anon$1@7102ff17
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
//     2 :: 24.87503690915363 :: 4.952489877479895 :: 12.958040649216835 :: 14.787074141105487 :: 2.011818308299283 :: 7.43028202501603 :: 2.6280037119620676 :: HNil,
//     2 :: 24.87503690915363 :: 4.918315121095343 :: 12.958040649216835 :: 14.640546021361658 :: 2.011818308299283 :: 7.43028202501603 :: 2.260438945594964 :: HNil,
//     2 :: 24.87503690915363 :: 4.986127021103391 :: 12.958040649216835 :: 14.640546021361658 :: 3.853875500566909 :: 7.652306882416528 :: 2.260438945594964 :: HNil,
//     1 :: 24.87503690915363 :: 4.254170948678578 :: 12.78158281354612 :: 14.997696718248005 :: 2.011818308299283 :: 7.43028202501603 :: 10.735632256305934 :: HNil,
//     2 :: 24.87503690915363 :: 4.918315121095343 :: 12.958040649216835 :: 14.640546021361658 :: 2.011818308299283 :: 7.43028202501603 :: 2.260438945594964 :: HNil,
//     2 :: 24.87503690915363 :: 4.918315121095343 :: 12.958040649216835 :: 14.640546021361658 :: 2.011818308299283 :: 7.43028202501603 :: 2.260438945594964 :: HNil,
//     2 :: 24.87503690915363 :: 4.952489877479895 :: 12.958040649216835 :: 14.640546021361658 :: 2.011818308299283 :: 7.43028202501603 :: 2.260438945594964 :: HNil,
//     2 :: 24.87503690915363 :: 4.952489877479895 :: 12.958040649216835 :: 14.602351460742511 :: 2.011818308299283 :: 7.43028202501603 :: 2.260438945594964 :: HNil,
//     2 :: 24.87503690915363 :: 4.952489877479895 :: 12.958040649216835 :: 14.997696718248005 :: 2.011818308299283 :: 7.43028202501603 :: 2.0441292409917713 :: HNil,
//     2 :: 24.87503690915363 :: 4.918315121095343 :: 12.958040649216835 :: 14.257453113741072 :: 2.011818308299283 :: 5.913151799865873 :: 2.260438945594964 :: HNil,
//     2 :: 24.87503690915363 :: 4.254170948678578 :: 12.958040649216835 :: 14.997696718248005 :: 2.011818308299283 :: 7.43028202501603 :: 2.260438945594964 :: HNil,
//     2 :: 24.87503690915363 :: 4.986127021103391 :: 12.78158281354612 :: 12.277658878473474 :: 2.011818308299283 :: 7.43028202501603 :: 2.6280037119620676 :: HNil,
//     2 :: 24.87503690915363 :: 3.120913886342218 :: 12.958040649216835 :: 14.640546021361658 :: 2.011818308299283 :: 7.43028202501603 :: 2.260438945594964 :: HNil,
//     2 :: 24.87503690915363 :: 4.952489877479895 :: 12.78158281354612 :: 13.4730166459127 :: 3.853875500566909 :: 7.43028202501603 :: 2.260438945594964 :: HNil,
//     2 :: 8.149107227197074 :: 4.952489877479895 :: 12.958040649216835 :: 14.640546021361658 :: 2.011818308299283 :: 7.43028202501603 :: 2.260438945594964 :: HNil,
//     2 :: 24.87503690915363 :: 4.952489877479895 :: 12.958040649216835 :: 13.4730166459127 :: 2.011818308299283 :: 7.43028202501603 :: 2.0441292409917713 :: HNil,
// ...

val winner = log.winners.last
// winner: gen.Repr = 2 :: 24.87503690915363 :: 2.0660423160909342 :: 12.958040649216835 :: 13.871672715091435 :: 2.235751609028726 :: 7.43028202501603 :: 2.260438945594964 :: HNil
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

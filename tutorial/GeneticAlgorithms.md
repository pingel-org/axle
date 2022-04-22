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
// gen: Generic[Rabbit]{type Repr = Int :: Double :: Double :: Double :: Double :: Double :: Double :: Double :: shapeless.HNil} = shapeless.Generic$$anon$1@21874dc2

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
// rabbitSpecies: AnyRef with Species[gen.Repr] = repl.MdocSession$App$$anon$1@4df532f8
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
//     2 :: 24.97907846123837 :: 4.745848901760972 :: 12.903215977797599 :: 14.761868443942273 :: 2.0045126689222714 :: 7.858858163083869 :: 10.049914283394777 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 12.903215977797599 :: 12.667175770176407 :: 2.0045126689222714 :: 7.858858163083869 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 12.903215977797599 :: 14.629634966839193 :: 2.0045126689222714 :: 7.769633834022687 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 12.903215977797599 :: 14.573241513875164 :: 2.0045126689222714 :: 7.889792086102523 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 11.64925438286232 :: 14.573241513875164 :: 2.0045126689222714 :: 7.947076895887257 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 5.668261415095888 :: 14.761868443942273 :: 2.0045126689222714 :: 7.943018505532342 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 6.856129641488657 :: 14.573241513875164 :: 2.0045126689222714 :: 7.858858163083869 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 12.903215977797599 :: 14.573241513875164 :: 2.0045126689222714 :: 7.858858163083869 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 12.903215977797599 :: 14.935638177144343 :: 2.0045126689222714 :: 7.769633834022687 :: 2.05217137509597 :: HNil,
//     2 :: 15.334684264407416 :: 4.996896678544738 :: 12.903215977797599 :: 13.595010118872572 :: 2.0045126689222714 :: 7.769633834022687 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.4616755133181885 :: 12.085340616851706 :: 10.393607720994106 :: 2.5066151338080505 :: 7.769633834022687 :: 10.049914283394777 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 12.903215977797599 :: 14.761868443942273 :: 2.0045126689222714 :: 7.769633834022687 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 12.903215977797599 :: 13.595010118872572 :: 2.0045126689222714 :: 7.858858163083869 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 12.903215977797599 :: 14.573241513875164 :: 2.0045126689222714 :: 7.769633834022687 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 12.903215977797599 :: 13.595010118872572 :: 2.0045126689222714 :: 7.858858163083869 :: 2.05217137509597 :: HNil,
//     2 :: 24.97907846123837 :: 4.996896678544738 :: 4.714421799148465 :: 14.573241513875164 :: 2.0045126689222714 :: 7.769633834022687 :: 2.05217137509597 :: HNil...

val winner = log.winners.last
// winner: gen.Repr = 2 :: 24.97907846123837 :: 4.996896678544738 :: 12.903215977797599 :: 14.935638177144343 :: 2.0045126689222714 :: 7.858858163083869 :: 2.05217137509597 :: HNil
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

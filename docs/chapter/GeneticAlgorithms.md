
Genetic Algorithms
==================

See the wikipedia page on <a href="https://en.wikipedia.org/wiki/Genetic_algorithm">Genetic Algorithms</a>

Example
-------

```scala
scala> case class Rabbit(a: Int, b: Double, c: Double, d: Double, e: Double, f: Double, g: Double, h: Double)
defined class Rabbit
```

Define a random rabbit generator and fitness function

```scala
scala> import shapeless._
import shapeless._

scala> import syntax.singleton._
import syntax.singleton._

scala> import record._
import record._

scala> import util.Random.nextDouble
import util.Random.nextDouble

scala> import util.Random.nextInt
import util.Random.nextInt

scala> import axle.ml._
import axle.ml._

scala> val gen = Generic[Rabbit]
gen: shapeless.Generic[Rabbit]{type Repr = shapeless.::[Int,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.HNil]]]]]]]]} = anon$macro$9$1@515512fb

scala> // val pMutation = 0.003
     | 
     | implicit val rabbitSpecies = new Species[gen.Repr] {
     | 
     |   def random() = {
     |     val rabbit = Rabbit(
     |       1 + nextInt(2),
     |       5 + 20 * nextDouble(),
     |       1 + 4 * nextDouble(),
     |       3 + 10 * nextDouble(),
     |       10 + 5 * nextDouble(),
     |       2 + 2 * nextDouble(),
     |       3 + 5 * nextDouble(),
     |       2 + 10 * nextDouble())
     |     gen.to(rabbit)
     |   }
     | 
     |   def fitness(rg: gen.Repr): Double = {
     |     val rabbit = gen.from(rg)
     |     import rabbit._
     |     a * 100 + 100.0 * b + 2.2 * (1.1 * c + 0.3 * d) + 1.3 * (1.4 * e - 3.1 * f + 1.3 * g) - 1.4 * h
     |   }
     | 
     | }
rabbitSpecies: axle.ml.Species[gen.Repr] = $anon$1@19c574bf
```

Run the genetic algorithm

```scala
scala> import spire.implicits._
import spire.implicits._

scala> val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)
ga: axle.ml.GeneticAlgorithm[gen.Repr,this.Out] = GeneticAlgorithm(100,100)

scala> val log = ga.run()
log: axle.ml.GeneticAlgorithmLog[gen.Repr] = GeneticAlgorithmLog(Vector((2 :: 24.936748250340994 :: 1.3466011360449572 :: 12.860175727198516 :: 14.91283279611198 :: 2.0188749698298487 :: 7.464345667589066 :: 9.706234284527685 :: HNil,2723.452621503675), (2 :: 24.936748250340994 :: 4.8923599394381085 :: 12.860175727198516 :: 14.91283279611198 :: 2.0188749698298487 :: 7.464345667589066 :: 7.090402285191305 :: HNil,2735.6955226069576), (2 :: 24.936748250340994 :: 4.8923599394381085 :: 12.860175727198516 :: 14.811900502635364 :: 2.0188749698298487 :: 7.311796271561316 :: 2.2088336579222316 :: HNil,2742.0882134317203), (2 :: 24.936748250340994 :: 4.8923599394381085 :: 12.860175727198516 :: 14.91283279611198 :: 2.0188749698298487 :: 7.464345667589066 :: 2.44919264522986 :: HNil,2742.193216102...
```

Plot the min, average, and max fitness function by generation

```scala
scala> import scala.collection.immutable.TreeMap
import scala.collection.immutable.TreeMap

scala> import axle.visualize._
import axle.visualize._

scala> val plot = Plot(
     |   List("min" -> log.mins, "ave" -> log.aves, "max" -> log.maxs),
     |   connect = true,
     |   title = Some("GA Demo"),
     |   xAxis = Some(0d),
     |   xAxisLabel = Some("generation"),
     |   yAxis = Some(0),
     |   yAxisLabel = Some("fitness"))
plot: axle.visualize.Plot[Int,Double,scala.collection.immutable.TreeMap[Int,Double]] = Plot(List((min,Map(0 -> 696.3299342871377, 1 -> 685.6767922481062, 2 -> 790.55964837122, 3 -> 797.5974981681288, 4 -> 971.2816232399502, 5 -> 663.7712114770999, 6 -> 663.4280634358341, 7 -> 1708.2076198609782, 8 -> 1090.6122315817988, 9 -> 813.1647458686037, 10 -> 802.621789914907, 11 -> 645.4552700527739, 12 -> 639.662366587523, 13 -> 1138.5248348394398, 14 -> 1140.9904161063346, 15 -> 758.2024968724759, 16 -> 1190.0589483055796, 17 -> 804.9497120730184, 18 -> 1364.1300380126622, 19 -> 1203.570830010826, 20 -> 2203.3229518517214, 21 -> 899.4970264663157, 22 -> 2621.8125638807905, 23 -> 2061.0435435099853, 24 -> 939.9829373141545, 25 -> 942.2066005323937, 26 -> 1006.5411717281768, 27 -> 868.9071040999...

scala> import axle.web._
import axle.web._

scala> svg(plot, "ga.svg")
```

![ga](../images/ga.svg)

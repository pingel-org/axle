
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
gen: shapeless.Generic[Rabbit]{type Repr = shapeless.::[Int,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.HNil]]]]]]]]} = anon$macro$9$1@30cfa127

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
rabbitSpecies: axle.ml.Species[gen.Repr] = $anon$1@7ad1cb68
```

Run the genetic algorithm

```scala
scala> import spire.implicits._
import spire.implicits._

scala> val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)
ga: axle.ml.GeneticAlgorithm[gen.Repr,this.Out] = GeneticAlgorithm(100,100)

scala> val log = ga.run()
log: axle.ml.GeneticAlgorithmLog[gen.Repr] = GeneticAlgorithmLog(Vector((2 :: 24.932220238256047 :: 4.938079071474383 :: 12.483046638733027 :: 14.455116196242852 :: 2.306568539995822 :: 7.985406534749245 :: 2.625014156161934 :: HNil,2740.2441434462144), (2 :: 24.932220238256047 :: 4.822568222260811 :: 11.26976300921662 :: 14.518731915459494 :: 2.01733015175135 :: 7.105692402875868 :: 2.625014156161934 :: HNil,2738.9585344263705), (2 :: 24.932220238256047 :: 4.291055403289651 :: 11.26976300921662 :: 14.518731915459494 :: 2.306568539995822 :: 7.985406534749245 :: 2.4960359809026613 :: HNil,2738.1739290280643), (2 :: 24.932220238256047 :: 4.822568222260811 :: 11.216114183685136 :: 14.377613860047884 :: 2.306568539995822 :: 4.113680060315613 :: 2.625014156161934 :: HNil,2732.4441597771183),...
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
plot: axle.visualize.Plot[Int,Double,scala.collection.immutable.TreeMap[Int,Double]] = Plot(List((min,Map(0 -> 715.4480882109451, 1 -> 757.6546552651525, 2 -> 878.1391807307916, 3 -> 1005.5172478926738, 4 -> 799.7983616178639, 5 -> 804.8292958548814, 6 -> 798.9434592422668, 7 -> 792.3443153999251, 8 -> 695.1800605902239, 9 -> 707.4778483113238, 10 -> 937.9112921465087, 11 -> 743.2524036449452, 12 -> 1099.5842421787916, 13 -> 798.2717583515823, 14 -> 873.7950122237813, 15 -> 1739.140179236244, 16 -> 2041.6109892263069, 17 -> 1152.1407502056825, 18 -> 992.1341583947249, 19 -> 836.9038546671816, 20 -> 778.9268337245925, 21 -> 1029.5627193299583, 22 -> 1024.2383445198118, 23 -> 1756.3724808127326, 24 -> 894.5386087282527, 25 -> 1103.4433446362457, 26 -> 1102.5344593981824, 27 -> 1539.685005...

scala> import axle.web._
import axle.web._

scala> svg(plot, "ga.svg")
```

![ga](../images/ga.svg)

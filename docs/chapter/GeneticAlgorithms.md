
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
gen: shapeless.Generic[Rabbit]{type Repr = shapeless.::[Int,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.HNil]]]]]]]]} = anon$macro$9$1@6ea590f6

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
rabbitSpecies: axle.ml.Species[gen.Repr] = $anon$1@76bc9ca
```

Run the genetic algorithm

```scala
scala> import spire.implicits._
import spire.implicits._

scala> val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)
ga: axle.ml.GeneticAlgorithm[gen.Repr,this.Out] = GeneticAlgorithm(100,100)

scala> val log = ga.run()
log: axle.ml.GeneticAlgorithmLog[gen.Repr] = GeneticAlgorithmLog(Vector((2 :: 24.95033323809586 :: 4.7718757638331475 :: 7.691123832872853 :: 14.653860939117825 :: 2.0136544088860204 :: 7.930724881387133 :: 2.2993671839154466 :: HNil,2740.396215521205), (2 :: 24.95033323809586 :: 4.7718757638331475 :: 12.92539966571358 :: 14.718958741057861 :: 2.0136544088860204 :: 4.618747181353706 :: 2.2993671839154466 :: HNil,2738.3720732573543), (2 :: 24.95033323809586 :: 4.7718757638331475 :: 12.92539966571358 :: 14.750601838980632 :: 2.0136544088860204 :: 7.930724881387133 :: 2.2993671839154466 :: HNil,2744.0269060086302), (2 :: 24.95033323809586 :: 4.7718757638331475 :: 12.92539966571358 :: 14.718958741057861 :: 2.0136544088860204 :: 7.930724881387133 :: 2.2993671839154466 :: HNil,2743.9693155704...
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
plot: axle.visualize.Plot[Int,Double,scala.collection.immutable.TreeMap[Int,Double]] = Plot(List((min,Map(0 -> 642.3359806762224, 1 -> 643.2697224485476, 2 -> 733.8062398757947, 3 -> 678.4100348301087, 4 -> 776.7918381027328, 5 -> 780.0196883708057, 6 -> 781.0473747478467, 7 -> 685.7924653759981, 8 -> 871.5196799379958, 9 -> 875.9908527980543, 10 -> 874.5311557270185, 11 -> 1387.4750726432758, 12 -> 921.2523568494239, 13 -> 975.3119929151819, 14 -> 790.0887916206028, 15 -> 1819.3480847504504, 16 -> 1235.369828094773, 17 -> 1595.6639513858618, 18 -> 858.8500014509241, 19 -> 1053.3903692093465, 20 -> 1374.4298357136038, 21 -> 1281.9546581566215, 22 -> 1962.9624162181758, 23 -> 771.4177175869542, 24 -> 797.7082651542439, 25 -> 788.4019950400495, 26 -> 1866.5355778838973, 27 -> 880.29382334...

scala> import axle.web._
import axle.web._

scala> svg(plot, "ga.svg")
```

![ga](../images/ga.svg)

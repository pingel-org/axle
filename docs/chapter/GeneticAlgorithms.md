
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
gen: shapeless.Generic[Rabbit]{type Repr = shapeless.::[Int,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.HNil]]]]]]]]} = anon$macro$9$1@5c501ffa

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
rabbitSpecies: axle.ml.Species[gen.Repr] = $anon$1@149a5b15
```

Run the genetic algorithm

```scala
scala> import spire.implicits._
import spire.implicits._

scala> val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)
ga: axle.ml.GeneticAlgorithm[gen.Repr,this.Out] = GeneticAlgorithm(100,100)

scala> val log = ga.run()
log: axle.ml.GeneticAlgorithmLog[gen.Repr] = GeneticAlgorithmLog(Vector((2 :: 24.986189170678635 :: 4.95337681303252 :: 12.274630367241805 :: 14.82362663631982 :: 2.290695301397254 :: 7.778731281185913 :: 2.6767733224916848 :: HNil,2745.8534166249688), (2 :: 24.986189170678635 :: 4.986416786749405 :: 10.54826375180464 :: 14.82362663631982 :: 2.4484847518728494 :: 6.230004208422437 :: 2.6767733224916848 :: HNil,2741.5407311567883), (2 :: 24.986189170678635 :: 1.3607691072162278 :: 12.274630367241805 :: 14.921768336668567 :: 2.0271629343179236 :: 7.778731281185913 :: 2.6767733224916848 :: HNil,2738.399959310858), (2 :: 21.403651293030975 :: 4.452072726248714 :: 6.853097315630736 :: 10.491698675737789 :: 2.176573201671408 :: 7.778731281185913 :: 2.6767733224916848 :: HNil,2375.384064329758...
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
plot: axle.visualize.Plot[Int,Double,scala.collection.immutable.TreeMap[Int,Double]] = Plot(List((min,Map(0 -> 640.0061405999649, 1 -> 782.448558951967, 2 -> 784.9566217179365, 3 -> 776.5939391522761, 4 -> 776.4720092513417, 5 -> 796.9782311718907, 6 -> 842.6319245100675, 7 -> 846.542816943476, 8 -> 842.2150345714981, 9 -> 815.327253702344, 10 -> 1998.1090211186968, 11 -> 1719.2663425970381, 12 -> 1512.9717230299395, 13 -> 1003.8300904771655, 14 -> 858.3317489630095, 15 -> 1312.3143538248744, 16 -> 1697.3120443615896, 17 -> 1318.627182201331, 18 -> 785.0486225562585, 19 -> 859.428820680805, 20 -> 869.3856694983774, 21 -> 740.4230867501659, 22 -> 913.206467078975, 23 -> 758.0611890551448, 24 -> 798.6891250952433, 25 -> 790.900922365375, 26 -> 1081.2636936882068, 27 -> 1214.6550307525538,...

scala> import axle.web._
import axle.web._

scala> svg(plot, "ga.svg")
```

![ga](../images/ga.svg)

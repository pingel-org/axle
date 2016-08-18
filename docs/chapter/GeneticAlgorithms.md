
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
gen: shapeless.Generic[Rabbit]{type Repr = shapeless.::[Int,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.::[Double,shapeless.HNil]]]]]]]]} = anon$macro$9$1@8d93b96

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
rabbitSpecies: axle.ml.Species[gen.Repr] = $anon$1@7e51cf3b
```

Run the genetic algorithm

```scala
scala> import spire.implicits._
import spire.implicits._

scala> val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)
ga: axle.ml.GeneticAlgorithm[gen.Repr,this.Out] = GeneticAlgorithm(100,100)

scala> val log = ga.run()
log: axle.ml.GeneticAlgorithmLog[gen.Repr] = GeneticAlgorithmLog(Vector((2 :: 24.75650813723737 :: 4.780279797872401 :: 12.942463891233102 :: 14.977602444409504 :: 2.0956830764232963 :: 7.4293501724548685 :: 2.625181064747137 :: HNil,2723.4550989544446), (2 :: 24.75650813723737 :: 4.780279797872401 :: 12.966328665802838 :: 14.977602444409504 :: 2.0956830764232963 :: 5.363481851572742 :: 2.625181064747137 :: HNil,2719.9795322433692), (2 :: 24.75650813723737 :: 4.780279797872401 :: 12.349446866850673 :: 14.977602444409504 :: 2.0956830764232963 :: 7.4293501724548685 :: 2.625181064747137 :: HNil,2723.063707718352), (2 :: 24.75650813723737 :: 3.7972476898317264 :: 12.966328665802838 :: 14.977602444409504 :: 2.2364516317200858 :: 7.962381475069983 :: 2.7406047746219357 :: HNil,2721.2638444339...
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
plot: axle.visualize.Plot[Int,Double,scala.collection.immutable.TreeMap[Int,Double]] = Plot(List((min,Map(0 -> 644.3254662213274, 1 -> 734.2133927978616, 2 -> 678.3025359382892, 3 -> 648.1010439854192, 4 -> 685.364008210855, 5 -> 685.715791219673, 6 -> 792.2376359181555, 7 -> 995.7842424248114, 8 -> 993.2132958471099, 9 -> 1001.341575163798, 10 -> 806.8370030468926, 11 -> 1093.1170693268298, 12 -> 812.8856727888628, 13 -> 1261.7155182286604, 14 -> 1269.6587355553409, 15 -> 1270.2649147455686, 16 -> 1283.730737088886, 17 -> 1367.7245536352411, 18 -> 1191.8618667788965, 19 -> 1197.6385364816229, 20 -> 1033.5962580564444, 21 -> 1190.4524190179384, 22 -> 1576.7573301128684, 23 -> 918.4488545774277, 24 -> 890.7379838499548, 25 -> 893.3802005292856, 26 -> 1076.0938128622956, 27 -> 1328.319950...

scala> import axle.web._
import axle.web._

scala> svg(plot, "ga.svg")
```

![ga](../images/ga.svg)

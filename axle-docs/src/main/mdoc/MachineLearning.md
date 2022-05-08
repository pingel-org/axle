# Machine Learning

## Linear Regression

`axle.ml.LinearRegression` makes use of `axle.algebra.LinearAlgebra`.

See the wikipedia page on [Linear Regression](https://en.wikipedia.org/wiki/Linear_regression)

### Example: Home Prices

```scala mdoc:silent
case class RealtyListing(size: Double, bedrooms: Int, floors: Int, age: Int, price: Double)

val listings = List(
  RealtyListing(2104, 5, 1, 45, 460d),
  RealtyListing(1416, 3, 2, 40, 232d),
  RealtyListing(1534, 3, 2, 30, 315d),
  RealtyListing(852, 2, 1, 36, 178d))
```

Create a price estimator using linear regression.

```scala mdoc:silent
import cats.implicits._
import spire.algebra.Rng
import spire.algebra.NRoot
import axle.jblas._

implicit val rngDouble: Rng[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
implicit val rngInt: Rng[Int] = spire.implicits.IntAlgebra

import axle.ml.LinearRegression

val priceEstimator = LinearRegression(
  listings,
  numFeatures = 4,
  featureExtractor = (rl: RealtyListing) => (rl.size :: rl.bedrooms.toDouble :: rl.floors.toDouble :: rl.age.toDouble :: Nil),
  objectiveExtractor = (rl: RealtyListing) => rl.price,
  α = 0.1,
  iterations = 100)
```

Use the estimator

```scala mdoc
priceEstimator(RealtyListing(1416, 3, 2, 40, 0d))
```

Create a Plot of the error during the training

```scala mdoc:silent
import axle.visualize._
import axle.algebra.Plottable._

val errorPlot = Plot(
  () => List(("error" -> priceEstimator.errTree)),
  connect = true,
  drawKey = true,
  colorOf = (label: String) => Color.black,
  title = Some("Linear Regression Error"),
  xAxis = Some(0d),
  xAxisLabel = Some("step"),
  yAxis = Some(0),
  yAxisLabel = Some("error"))
```

Create the SVG

```scala mdoc:silent
import axle.web._
import cats.effect._

errorPlot.svg[IO]("@DOCWD@/images/lrerror.svg").unsafeRunSync()
```

![lr error](/images/lrerror.svg)

## Naive Bayes

Naïve Bayes

### Example: Tennis and Weather

```scala mdoc:silent:reset
case class Tennis(outlook: String, temperature: String, humidity: String, wind: String, play: Boolean)

val events = List(
  Tennis("Sunny", "Hot", "High", "Weak", false),
  Tennis("Sunny", "Hot", "High", "Strong", false),
  Tennis("Overcast", "Hot", "High", "Weak", true),
  Tennis("Rain", "Mild", "High", "Weak", true),
  Tennis("Rain", "Cool", "Normal", "Weak", true),
  Tennis("Rain", "Cool", "Normal", "Strong", false),
  Tennis("Overcast", "Cool", "Normal", "Strong", true),
  Tennis("Sunny", "Mild", "High", "Weak", false),
  Tennis("Sunny", "Cool", "Normal", "Weak", true),
  Tennis("Rain", "Mild", "Normal", "Weak", true),
  Tennis("Sunny", "Mild", "Normal", "Strong", true),
  Tennis("Overcast", "Mild", "High", "Strong", true),
  Tennis("Overcast", "Hot", "Normal", "Weak", true),
  Tennis("Rain", "Mild", "High", "Strong", false))
```

Build a classifier to predict the Boolean feature 'play' given all the other features of the observations

```scala mdoc:silent
import cats.implicits._

import spire.math._

import axle._
import axle.probability._
import axle.ml.NaiveBayesClassifier
```

```scala mdoc:silent
val classifier = NaiveBayesClassifier[Tennis, String, Boolean, List, Rational](
  events,
  List(
    (Variable[String]("Outlook") -> Vector("Sunny", "Overcast", "Rain")),
    (Variable[String]("Temperature") -> Vector("Hot", "Mild", "Cool")),
    (Variable[String]("Humidity") -> Vector("High", "Normal", "Low")),
    (Variable[String]("Wind") -> Vector("Weak", "Strong"))),
  (Variable[Boolean]("Play") -> Vector(true, false)),
  (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
  (t: Tennis) => t.play)
```

Use the classifier to predict:

```scala mdoc
events map { datum => datum.toString + "\t" + classifier(datum) } mkString("\n")
```

Measure the classifier's performance

```scala mdoc
import axle.ml.ClassifierPerformance

ClassifierPerformance[Rational, Tennis, List](events, classifier, _.play).show
```

See [Precision and Recall](http://en.wikipedia.org/wiki/Precision_and_recall)
for the definition of the performance metrics.

## k-Means Clustering

### Example: Irises

See the wikipedia page on [k-Means Clustering](https://en.wikipedia.org/wiki/K-means_clustering)

A demonstration of k-Means Clustering using the [Iris flower data set](https://en.wikipedia.org/wiki/Iris_flower_data_set)

Imports for Distance quanta

```scala mdoc:silent:reset
import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.implicits._
import spire.algebra._
import axle._
import axle.quanta.Distance
import axle.quanta.DistanceConverter
import axle.jung._

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra

implicit val distanceConverter = {
  import axle.algebra.modules.doubleRationalModule
  Distance.converterGraphK2[Double, DirectedSparseGraph]
}
```

Import the Irises data set

```scala mdoc:silent
import axle.data.Irises
import axle.data.Iris
```

```scala mdoc:silent
val ec = scala.concurrent.ExecutionContext.global
val blocker = cats.effect.Blocker.liftExecutionContext(ec)
implicit val cs = cats.effect.IO.contextShift(ec)

val irisesIO = new Irises[cats.effect.IO](blocker)
val irises = irisesIO.irises.unsafeRunSync()
```

Make a 2-D Euclidean space implicitly available for clustering

```scala mdoc:silent
import org.jblas.DoubleMatrix
import axle.algebra.distance.Euclidean
import axle.jblas.linearAlgebraDoubleMatrix
import axle.jblas.rowVectorInnerProductSpace

implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

implicit val space: Euclidean[DoubleMatrix, Double] = {
  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
  implicit val inner = rowVectorInnerProductSpace[Int, Int, Double](2)
  new Euclidean[DoubleMatrix, Double]
}
```

Build a classifier of irises based on sepal length and width using the K-Means algorithm

```scala mdoc:silent
import spire.random.Generator.rng
import axle.ml.KMeans
import axle.ml.PCAFeatureNormalizer
import distanceConverter.cm
```

```scala mdoc:silent
val irisFeaturizer =
  (iris: Iris) => List((iris.sepalLength in cm).magnitude.toDouble, (iris.sepalWidth in cm).magnitude.toDouble)

implicit val la = linearAlgebraDoubleMatrix[Double]

val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)

val classifier: KMeans[Iris, List, DoubleMatrix] =
  KMeans[Iris, List, DoubleMatrix](
    irises,
    N = 2,
    irisFeaturizer,
    normalizer,
    K = 3,
    iterations = 20)(rng)
```

Produce a "confusion matrix"

```scala mdoc:silent
import axle.ml.ConfusionMatrix

val confusion = ConfusionMatrix[Iris, Int, String, Vector, DoubleMatrix](
  classifier,
  irises.toVector,
  _.species,
  0 to 2)
```

```scala mdoc
confusion.show
```

Visualize the final (two dimensional) centroid positions

```scala mdoc:silent
import axle.visualize.KMeansVisualization
import axle.visualize.Color._

val colors = Vector(red, blue, green)

val vis = KMeansVisualization[Iris, List, DoubleMatrix](classifier, colors)
```

Create the SVG

```scala mdoc:silent
import axle.web._
import cats.effect._

vis.svg[IO]("@DOCWD@/images/k_means.svg").unsafeRunSync()
```

![kmeans](/images/k_means.svg)

Average centroid/cluster vs iteration:

```scala mdoc:silent
import scala.collection.immutable.TreeMap
import axle.visualize._

val plot = Plot(
  () => classifier.distanceLogSeries,
  connect = true,
  drawKey = true,
  colorOf = colors,
  title = Some("KMeans Mean Centroid Distances"),
  xAxis = Some(0d),
  xAxisLabel = Some("step"),
  yAxis = Some(0),
  yAxisLabel = Some("average distance to centroid"))
```

Create the SVG

```scala mdoc:silent
import axle.web._
import cats.effect._

plot.svg[IO]("@DOCWD@/images/kmeansvsiteration.svg").unsafeRunSync()
```

![kmeans](/images/kmeansvsiteration.svg)

## Example: Federalist Papers

Imports

```scala mdoc:silent:reset
import axle.data.FederalistPapers
import FederalistPapers.Article
```

Download (and cache) the Federalist articles downloader:

```scala mdoc:silent
val ec = scala.concurrent.ExecutionContext.global
val blocker = cats.effect.Blocker.liftExecutionContext(ec)
implicit val cs = cats.effect.IO.contextShift(ec)

val articlesIO = FederalistPapers.articles[cats.effect.IO](blocker)

val articles = articlesIO.unsafeRunSync()
```

The result is a `List[Article]`.  How many articles are there?

```scala mdoc
articles.size
```

Construct a `Corpus` object to assist with content analysis

```scala mdoc:silent
import axle.nlp._
import axle.nlp.language.English

import spire.algebra.CRing
implicit val ringLong: CRing[Long] = spire.implicits.LongAlgebra

val corpus = Corpus[Vector, Long](articles.map(_.text).toVector, English)
```

Define a feature extractor using top words and bigrams.

```scala mdoc:height=15
val frequentWords = corpus.wordsMoreFrequentThan(100)
```

```scala mdoc:height=15
val topBigrams = corpus.topKBigrams(200)
```

```scala mdoc
val numDimensions = frequentWords.size + topBigrams.size
```

```scala mdoc:silent
import axle.syntax.talliable.talliableOps

def featureExtractor(fp: Article): List[Double] = {

  val tokens = English.tokenize(fp.text.toLowerCase)
  val wordCounts = tokens.tally[Long]
  val bigramCounts =  bigrams(tokens).tally[Long]
  val wordFeatures = frequentWords.map(wordCounts(_) + 0.1)
  val bigramFeatures = topBigrams.map(bigramCounts(_) + 0.1)
  wordFeatures ++ bigramFeatures
}
```

Place a `MetricSpace` implicitly in scope that defines the space in which to
measure similarity of Articles.

```scala mdoc:silent
import spire.algebra._

import axle.algebra.distance.Euclidean

import org.jblas.DoubleMatrix
import axle.jblas.linearAlgebraDoubleMatrix

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

implicit val space = {
  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
  implicit val inner = axle.jblas.rowVectorInnerProductSpace[Int, Int, Double](numDimensions)
  new Euclidean[DoubleMatrix, Double]
}
```

Create 4 clusters using k-Means

```scala mdoc:silent
import axle.ml.KMeans
import axle.ml.PCAFeatureNormalizer
```

```scala mdoc:silent
import cats.implicits._
import spire.random.Generator.rng

val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)

val classifier = KMeans[Article, List, DoubleMatrix](
  articles,
  N = numDimensions,
  featureExtractor,
  normalizer,
  K = 4,
  iterations = 100)(rng)
```

Show cluster vs author in a confusion matrix:

```scala mdoc:silent
import axle.ml.ConfusionMatrix

val confusion = ConfusionMatrix[Article, Int, String, Vector, DoubleMatrix](
  classifier,
  articles.toVector,
  _.author,
  0 to 3)
```

```scala mdoc
confusion.show
```

## Genetic Algorithms

See the wikipedia page on [Genetic Algorithms](https://en.wikipedia.org/wiki/Genetic_algorithm)

## Example: Rabbits

Consider a `Rabbit` class

```scala mdoc:silent:reset
case class Rabbit(a: Int, b: Double, c: Double, d: Double, e: Double, f: Double, g: Double, h: Double)
```

Define the `Species` for a Genetic Algorithm, which requires a random generator and
a fitness function.

```scala mdoc:silent
import shapeless._

val gen = Generic[Rabbit]

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
```

Run the genetic algorithm

```scala mdoc:silent
import cats.implicits._

val ga = GeneticAlgorithm(populationSize = 100, numGenerations = 100)

val log = ga.run(spire.random.Generator.rng)
```

```scala mdoc
val winner = log.winners.last
```

Plot the min, average, and max fitness function by generation

```scala mdoc:silent
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
```

Render to an SVG file

```scala mdoc
import axle.web._
import cats.effect._

plot.svg[IO]("@DOCWD@/images/ga.svg").unsafeRunSync()
```

![ga](/images/ga.svg)

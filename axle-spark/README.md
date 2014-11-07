
Examples

Monte Carlo estimation of π
---------------------------

```scala
package demo

import scala.math.random

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import axle.algebra.Aggregatable
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.Σ
import spire.implicits.IntAlgebra

object DemoPi extends App with java.io.Serializable {

  val randomPointInCircle: () => Int = () => {
    val x = random * 2 - 1
    val y = random * 2 - 1
    if (x * x + y * y < 1) 1 else 0
  }

  def estimatePi[F[_]: Functor: Aggregatable: Finite](trials: F[Int]): Double = {

    val n = implicitly[Finite[F]].size(trials)

    val numInCircle = implicitly[Functor[F]].map(trials)(i => randomPointInCircle())

    4d * Σ(numInCircle) / n
  }

  val slices = 2
  val n = 100000 * slices

  val conf = new SparkConf().setAppName("Pi on Axle on Spark").setMaster("local[2]")
  val spark = new SparkContext(conf)

  //val trials = spark.parallelize(1 to n, slices)
  //val trials = (1 to n).toList
  val trials = 1 to n
  //val trials = (1 to n).par

  val π = estimatePi(trials)

  println(s"π ≅ $π")

  spark.stop()

}
```

Naive Bayes
-----------

```scala
package demo

import java.lang.Boolean.parseBoolean

import scala.Vector

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import axle.ml.NaiveBayesClassifier
import axle.orderBooleans
import axle.orderStrings
import axle.spark.aggregatableRDD
import axle.spark.functorRDD
import axle.stats.UnknownDistribution0
import axle.stats.rationalProbabilityDist
import spire.math.Rational

case class Tennis(outlook: String, temperature: String, humidity: String, wind: String, play: Boolean)

object Tennis {

  def fromArray(a: Array[String]): Tennis = Tennis(a(0), a(1), a(2), a(3), parseBoolean(a(4)))
}

object DemoTennis extends App {

  val tennisFile = "tennis.csv"

  val conf = new SparkConf().setAppName("Tennis on Axle on Spark").setMaster("local[2]")
  conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
  val spark = new SparkContext(conf)

  val tennisCells =
    spark
      .textFile(tennisFile, 2)
      .map(_.split(",").map(_.trim))
      .filter(_(0) != "Outlook")
      .cache()

  // val tennisCells = scala.io.Source.fromFile(tennisFile).getLines().drop(1).toSeq.map(_.split(","))

  val tennisData = tennisCells map { Tennis.fromArray }

  val classifier = NaiveBayesClassifier(
    tennisData,
    List(
      UnknownDistribution0[String, Rational](Vector("Sunny", "Overcast", "Rain"), "Outlook"),
      UnknownDistribution0[String, Rational](Vector("Hot", "Mild", "Cool"), "Temperature"),
      UnknownDistribution0[String, Rational](Vector("High", "Normal", "Low"), "Humidity"),
      UnknownDistribution0[String, Rational](Vector("Weak", "Strong"), "Wind")),
    UnknownDistribution0[Boolean, Rational](Vector(true, false), "Play"),
    (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
    (t: Tennis) => t.play)

  tennisData foreach { datum =>
    println(datum.toString + "\t" + classifier(datum))
  }

  println(classifier.performance(tennisData, _.play, true))

  spark.stop()
}
```

package axle.data

import java.io.File

import axle.quanta._
import axle.quanta.Distance
import Distance._

import spire.implicits._
import spire.math._
import spire.algebra._

import scala.Option.option2Iterable
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.reflect.io.Path.string2path
import scala.util.Try

import spire.algebra.Eq

import dispatch.Defaults.executor
import dispatch.Http
import dispatch.as
import dispatch.implyRequestHandlerTuple
import dispatch.url

/**
 *
 * Description: http://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.names
 *
 * Data url: http://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data
 *
 * Columns:
 *
 *  1. sepal length in cm
 *  2. sepal width in cm
 *  3. petal length in cm
 *  4. petal width in cm
 *  5. class: Iris Setosa, Iris Versicolour, or Iris Virginica
 *
 * Examples:
 *
 *   5.0,3.3,1.4,0.2,Iris-setosa
 *   7.0,3.2,4.7,1.4,Iris-versicolor
 *
 * For examples of analyzing this dataset with R, see
 * http://horicky.blogspot.com/2012/04/machine-learning-in-r-clustering.html
 *
 */

object Irises {

  case class Iris(
    sepalLength: Distance.Q,
    sepalWidth: Distance.Q,
    petalLength: Distance.Q,
    petalWidth: Distance.Q,
    species: String)

  object Iris {
    implicit val irisEq = new Eq[Iris] { def eqv(x: Iris, y: Iris) = x equals y }
  }

  val dataUrl = "http://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data"

  val filename = "iris.data"

  val file = new File(filename)

  if (!file.exists) {
    val svc = url(dataUrl)
    val requestFuture = Http(svc OK as.String) map { content =>
      scala.tools.nsc.io.File(filename).writeAll(content)
    }
    Await.result(requestFuture, 1 minute)
  }

  val irises = io.Source.fromFile(file).getLines().toList flatMap { line =>
    Try {
      val fields = line.split(",")
      Iris(
        fields(0).toDouble *: cm,
        fields(1).toDouble *: cm,
        fields(2).toDouble *: cm,
        fields(3).toDouble *: cm,
        fields(4))
    } toOption
  }

}
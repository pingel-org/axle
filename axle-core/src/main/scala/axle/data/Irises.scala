package axle.data

import java.io.File
import scala.Option.option2Iterable
import scala.util.Try
import dispatch._
import Defaults._
import scala.concurrent.Await
import scala.concurrent.duration._

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

  case class Iris(sepalLength: Double, sepalWidth: Double, petalLength: Double, petalWidth: Double, clazz: String)

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
      Iris(fields(0).toDouble, fields(1).toDouble, fields(2).toDouble, fields(3).toDouble, fields(4))
    } toOption
  }

}
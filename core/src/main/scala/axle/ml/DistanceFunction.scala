package axle.ml

import spire.math._
import spire.implicits._
import spire.algebra._
import axle.matrix._
import axle._

object DistanceFunctionModule {

  import JblasMatrixModule._

  import math.{ abs, sqrt }

  def norm(row: Matrix[Double]): Double = sqrt((0 until row.columns).map(i => row(0, i) ** 2).sum)

  def dotProduct(row1: Matrix[Double], row2: Matrix[Double]): Double = ???

  lazy val euclidian = new MetricSpace[Matrix[Double], Double] {

    // TODO: assert(r1.isRowVector && r2.isRowVector && r1.length == r2.length)    

    def distance(r1: Matrix[Double], r2: Matrix[Double]) = norm(r1 - r2)
  }

  lazy val manhattan = new MetricSpace[Matrix[Double], Double] {

    def distance(r1: Matrix[Double], r2: Matrix[Double]) = (r1 - r2).map(abs(_)).toList.sum
  }

  lazy val cosine = new MetricSpace[Matrix[Double], Double] {

    def distance(r1: Matrix[Double], r2: Matrix[Double]) = 1.0 - (dotProduct(r1, r2) / (norm(r1) * norm(r2)))
  }

}

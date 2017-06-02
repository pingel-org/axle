package axle.spark

import org.scalatest._

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import spire.implicits._
import axle.syntax.setfrom._
import axle.syntax.mapfrom._
import axle.syntax.mapreducible._
import axle.math.monteCarloPiEstimate

class MonteCarloSpec extends FunSuite with Matchers {

  test("Spark has enough witnesses to support axle.monteCarloPiEstimate") {

    val slices = 2
    val n = 1000L * slices
    val conf = new SparkConf().setAppName("Pi on Axle on Spark").setMaster("local[2]")
    val spark = new SparkContext(conf)
    val trials: RDD[Long] = spark.parallelize(1 to n.toInt, slices).map(_.toLong)

    val π = monteCarloPiEstimate[RDD[Long], Long, Double, RDD[Double]](trials, (n: Long) => n.toDouble)

    val s = setFromOps(trials).toSet
    val m = mapFromOps(trials.map(i => i -> i % 10)).toMap
    val mr = mapReducibleOps[RDD[Long], Long, Long, Long, RDD[(Long, Long)]](trials).
      mapReduce(i => i % 10 -> i, 0L, _ + _)
    val mrCount = mr.count()

    spark.stop()

    mrCount should be(10L)
    π should be > 2.9
  }

}

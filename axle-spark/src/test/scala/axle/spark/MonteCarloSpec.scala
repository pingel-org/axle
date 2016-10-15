package axle.spark

import org.specs2.mutable._

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import axle._
import spire.implicits._

class MonteCarloSpec extends Specification {

  "Spark" should {
    "have enough witnesses to support axle.monteCarloPiEstimate" in {

      val slices = 2
      val n = 1000L * slices
      val conf = new SparkConf().setAppName("Pi on Axle on Spark").setMaster("local[2]")
      val spark = new SparkContext(conf)
      val trials = spark.parallelize(1 to n.toInt, slices).map(_.toLong)

      val π = monteCarloPiEstimate[RDD[Long], Long, Double, RDD[Double]](trials, (n: Long) => n.toDouble)

      spark.stop()

      π must be greaterThan 3.1
    }
  }

}

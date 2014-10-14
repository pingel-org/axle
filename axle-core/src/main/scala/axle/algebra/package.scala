
package axle

import axle.algebra.EnrichedMetricSpace
import axle.matrix.JblasMatrixModule
import spire.algebra.MetricSpace
import spire.math.Rational
import spire.math.Real

// http://en.wikipedia.org/wiki/Algebraic_structure

package object algebra {

  implicit def enrichMetricSpace[T: Manifest](space: MetricSpace[T, Double]): EnrichedMetricSpace[T] =
    new EnrichedMetricSpace(space) with JblasMatrixModule

  implicit val rationalDoubleMetricSpace: MetricSpace[Rational, Double] = new MetricSpace[Rational, Double] {

    def distance(v: Rational, w: Rational): Double = (v.toDouble - w.toDouble).abs
  }

  implicit val realDoubleMetricSpace: MetricSpace[Real, Double] = new MetricSpace[Real, Double] {

    def distance(v: Real, w: Real): Double = (v.toDouble - w.toDouble).abs
  }
  
  implicit val doubleDoubleMetricSpace: MetricSpace[Double, Double] = new MetricSpace[Double, Double] {

    def distance(v: Double, w: Double): Double = (v - w).abs
  }
   
    
}


package axle

import spire.algebra.MetricSpace
import spire.math.Rational

package object quanta {

  implicit val rationalDoubleMetricSpace: MetricSpace[Rational, Double] = new MetricSpace[Rational, Double] {

    def distance(v: Rational, w: Rational): Double = (v.toDouble - w.toDouble).abs

  }

}

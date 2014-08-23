
package axle

import java.math.BigDecimal
import spire.math.Rational
import spire.algebra.MetricSpace

package object quanta {
  
  implicit val rationalDoubleMetricSpace: MetricSpace[Rational, Double] = new MetricSpace[Rational, Double] {

    def distance(v: Rational, w: Rational): Double = (v.toDouble - w.toDouble).abs

  }
  
//
//  implicit def toBD(i: Int) = new BigDecimal(i.toString)
//
//  implicit def toBD(d: Double) = new BigDecimal(d.toString)
//
//  implicit def toBD(s: String) = new BigDecimal(s)

}

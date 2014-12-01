package axle.stats

import scala.math.pow
import scala.math.sqrt
import axle.algebra.Matrix
import axle.syntax.matrix._

case class ChiSquaredTest[M[_]: Matrix](tally: M[Double], threshold: Double = 0.004) {

  val rowTotals = tally.rowSums
  val columnTotals = tally.columnSums
  val total = (rowTotals.columnSums).get(0, 0)

  val χ2 =
    (0 until tally.rows) map { r =>
      (0 until tally.columns) map { c =>
        val observed = tally.get(r, c)
        val expected = rowTotals.get(r, 0) * columnTotals.get(0, c) / total
        (observed - expected) * (observed - expected) / expected
      } sum
    } sum

  /**
   * Computes whether there is a 95% probability that this correlation happened by chance
   *
   * TODO generalize this so that it looks up the P value based on user-specified confidence
   *
   *    val dof = (table.height - 1) * (table.width - 1)
   */

  def independent[M[_]: Matrix]: Boolean = χ2 < threshold

}

  /**
   * http://fonsg3.let.uva.nl/Service/Statistics/ChiSquare_distribution.html
   *
   * Z = {(X^2/DoF)^(1/3) - (1 - 2/(9*DoF))}/SQRT(2/(9*DoF))
   *
   * @param dof = degrees of freedom
   *
   * http://www.math.bcit.ca/faculty/david_sabo/apples/math2441/section8/onevariance/chisqtable/chisqtable.htm
   *
   * TODO validate this against http://www.ento.vt.edu/~sharov/PopEcol/tables/chisq.html
   */

//  def χ2probability(χ2: Double, dof: Int): Double =
//    pow((χ2 / dof), (1.0 / 3)) - (1 - 2.0 / (9 * dof)) / sqrt(2 / (9 * dof))

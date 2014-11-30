package axle.stats

import scala.math.pow
import scala.math.sqrt
import axle.algebra.Matrix

trait ChiSquaredTestModule {

  def χ2[M[_]: Matrix](tally: M[Double]): Double = {
    val witness = implicitly[Matrix[M]]
    val rowTotals = witness.rowSums(tally)
    val columnTotals = witness.columnSums(tally)
    val total = witness.get(witness.columnSums(rowTotals))(0, 0)
    (0 until witness.rows(tally)) map { r =>
      (0 until witness.columns(tally)) map { c =>
        val observed = witness.get(tally)(r, c)
        val expected = witness.get(rowTotals)(r, 0) * witness.get(columnTotals)(0, c) / total
        (observed - expected) * (observed - expected) / expected
      } sum
    } sum
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

  def χ2probability(χ2: Double, dof: Int): Double =
    pow((χ2 / dof), (1.0 / 3)) - (1 - 2.0 / (9 * dof)) / sqrt(2 / (9 * dof))

  /**
   * Computes whether there is a 95% probability that this correlation happened by chance
   *
   * TODO generalize this so that it looks up the P value based on user-specified confidence
   *
   *    val dof = (table.height - 1) * (table.width - 1)
   */

  def independent[M[_]: Matrix](table: M[Double]): Boolean = χ2(table) < 0.004

}

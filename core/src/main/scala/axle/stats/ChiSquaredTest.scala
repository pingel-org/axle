package org.pingel.bayes

import axle.matrix.JblasMatrixFactory._ // TODO: generalize

import math.{ pow, sqrt }

object ChiSquaredTest {

  def χ2(tally: Matrix[Double]): Double = {

    var rowTotals = new Array[Double](tally.rows)
    for (r <- 0 until tally.rows) {
      rowTotals(r) = 0
      for (c <- 0 until tally.columns) {
        rowTotals(r) += tally(r, c)
      }
    }

    var columnTotals = new Array[Double](tally.columns)
    for (c <- 0 until tally.columns) {
      columnTotals(c) = 0
      for (r <- 0 until tally.rows) {
        columnTotals(c) += tally(r, c)
      }
    }

    val total = 0.until(tally.rows).map(rowTotals(_)).sum
    
//    val total2 = 0.until(tally.columns).map(columnTotals(_)).sum
//    if (total != total2) {
//      throw new Exception("error calculating chi squared")
//    }

    (for (r <- 0 until tally.rows; c <- 0 until tally.columns) yield {
      val observed = tally(r, c)
      val expected = rowTotals(r) * columnTotals(c) / total
      (observed - expected) * (observed - expected) / expected
    }).sum

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

  def independent(table: Matrix[Double]): Boolean = χ2(table) < 0.004

}

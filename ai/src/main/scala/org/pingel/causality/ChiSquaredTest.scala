package org.pingel.bayes

import org.pingel.util.Matrix

// import scalala.tensor.mutable._

object ChiSquaredTest {

    def chiSquared(tally: Matrix[Double]) = {

    	val height = tally.numRows
    	val width = tally.numCols
      
        var rowTotals = new Array[Double](height)
        for( r <- 0 until height ) {
            rowTotals(r) = 0
            for( c <- 0 until width ) {
                rowTotals(r) += tally.valueAt(r, c)
            }
        }

        var columnTotals = new Array[Double](width)
        for(c <- 0 until width ) {
            columnTotals(c) = 0
            for( r <- 0 until height ) {
                columnTotals(c) += tally.valueAt(r, c)
            }
        }

        val total = 0.until(height).map(rowTotals(_)).foldLeft(0.0)({_+_})
        val total2 = 0.until(width).map(columnTotals(_)).foldLeft(0.0)({_+_})
        if( total != total2 ) {
        	throw new Exception("error calculating chi squared")
        }
        
    	var result = 0.0
        for(r <- 0 until height ) {
            for(c <- 0 until width ) {
                val observed = tally.valueAt(r, c)
                val expected = rowTotals(r) * columnTotals(c) / total
                result += (observed - expected)*(observed - expected) / expected;
            }
        }
        
        result
    }

//    private static double chiSquaredProbability(double chiSquared, int degreesOfFreedom)
//    {
//        // I got this formula from
//        // http://fonsg3.let.uva.nl/Service/Statistics/ChiSquare_distribution.html
//        //
//        //    Z = {(X^2/DoF)^(1/3) - (1 - 2/(9*DoF))}/SQRT(2/(9*DoF))
//
//        // http://www.math.bcit.ca/faculty/david_sabo/apples/math2441/section8/onevariance/chisqtable/chisqtable.htm
//        // TODO validate this against http://www.ento.vt.edu/~sharov/PopEcol/tables/chisq.html
//        
//        return (Math.pow((chiSquared/degreesOfFreedom),
//                (1/3)) - (1 - 2/(9*degreesOfFreedom))) /
//                Math.sqrt(2/(9*degreesOfFreedom));
//    }

    def independent(table: Matrix[Double]) = {
        val chiSq = chiSquared(table)
        // System.out.println("chi squared = " + chiSquared);
        
        // int degreesOfFreedom = (height - 1) * (width - 1);
        
        // TODO generalize this so that it looks up the P value from the
        
        chiSq < 0.004 // a 95% probability that this correlation happened by chance
    }
    

}

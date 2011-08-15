package org.pingel.bayes;

object ChiSquaredTest {

    def chiSquared(tally: Matrix[Double]) = {

    	val height = tally.height
    	val width = tally.width
      
        var rowTotals = new Array[Double](height)
        for( r <- 0 to height-1) {
            rowTotals(r) = 0
            for( c <- 0 to width-1 ) {
                rowTotals(r) += tally(r, c)
            }
        }

        var columnTotals = new Array[Double](width)
        for(c <- 0 to width-1 ) {
            columnTotals(c) = 0
            for( r <- 0 to height-1 ) {
                columnTotals(c) += tally(r, c)
            }
        }

        var total = 0.0
        for(r <- 0 to height-1 ) {
            total += rowTotals(r)
        }

        var total2 = 0.0
        for(c <- 0 to width-1 ) {
            total2 += columnTotals(c)
        }
        
        if( total != total2 ) {
        	throw new Exception("error calculating chi squared")
        }
        
    	var result = 0.0
        for(r <- 0 to height-1 ) {
            for(c <- 0 to width-1 ) {
                val observed = tally(r, c)
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
        val chiSquared = chiSquared(table)
        // System.out.println("chi squared = " + chiSquared);
        
        // int degreesOfFreedom = (height - 1) * (width - 1);
        
        // TODO generalize this so that it looks up the P value from the
        
        chiSquared < 0.004 // a 95% probability that this correlation happened by chance
    }
    

}

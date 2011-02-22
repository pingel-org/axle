package org.pingel.bayes;

public class ChiSquaredTest {

    private static double chiSquared(double[][] tally, int height, int width)
    {
        double result = 0;


        double rowTotals[] = new double[height];
        for(int r=0; r < height; r++) {
            rowTotals[r] = 0;
            for(int c=0; c < width; c++) {
                rowTotals[r] += tally[r][c];
            }
        }

        double columnTotals[] = new double[width];
        for(int c=0; c < width; c++ ) {
            columnTotals[c] = 0;
            for(int r=0; r < height; r++ ) {
                columnTotals[c] += tally[r][c];
            }
        }

        double total = 0;
        for(int r=0; r < height; r++) {
            total += rowTotals[r];
        }

        double total2 = 0;
        for(int c=0; c < width; c++) {
            total2 += columnTotals[c];
        }
        
        if( total != total2 ) {
            System.err.println("error calculating chi squared");
            System.exit(1);
        }
        
        for(int r=0; r < height; r++) {
            for(int c=0; c < width; c++) {
                double observed = tally[r][c];
                double expected = rowTotals[r] * columnTotals[c] / total;
                result += (observed - expected)*(observed - expected) / expected;
            }
        }
        
        return result;
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

    public static boolean independent(double[][] table, int height, int width)
    {
        double chiSquared = chiSquared(table, width, height);
        // System.out.println("chi squared = " + chiSquared);
        
        // int degreesOfFreedom = (height - 1) * (width - 1);
        
        // TODO generalize this so that it looks up the P value from the
        
        return chiSquared < 0.004; // a 95% probability that this correlation happened by chance
    }
    

}

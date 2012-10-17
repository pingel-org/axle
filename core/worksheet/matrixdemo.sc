object matrixdemo {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import axle.matrix.JblasMatrixFactory._

  // Creating matrices

  ones[Double](2, 3)                              //> res0: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 1.000000 1.000000
                                                  //|  1.000000
                                                  //| 1.000000 1.000000 1.000000
  ones[Int](2, 3)                                 //> res1: axle.matrix.JblasMatrixFactory.JblasMatrix[Int] = 1 1 1
                                                  //| 1 1 1

  ones[Boolean](2, 3)                             //> res2: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| true true true

  trues(2, 3)                                     //> res3: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| true true true

  // Creating matrices from arrays

  matrix[Double](2, 2, List(1.1, 2.2, 3.3, 4.4).toArray)
                                                  //> res4: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 1.100000 3.300000
                                                  //| 
                                                  //| 2.200000 4.400000

  matrix[Double](2, 2, List(1.1, 2.2, 3.3, 4.4).toArray).t
                                                  //> res5: axle.matrix.JblasMatrixFactory.M[Double] = 1.100000 2.200000
                                                  //| 3.300000 4.400000
  val m = matrix(4, 5, (1 to 20).toArray)         //> m  : axle.matrix.JblasMatrixFactory.JblasMatrix[Int] = 1 5 9 13 17
                                                  //| 2 6 10 14 18
                                                  //| 3 7 11 15 19
                                                  //| 4 8 12 16 20

  // Random matrices

  val r = rand[Double](3, 3)                      //> r  : axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.056911 0.124401 
                                                  //| 0.902110
                                                  //| 0.576008 0.082596 0.102244
                                                  //| 0.671649 0.408694 0.000302

  // Basic metadata

  val x = matrix(3, 1, Vector(4.0, 5.1, 6.2).toArray)
                                                  //> x  : axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000
                                                  //| 5.100000
                                                  //| 6.200000
  
  val y = matrix(3, 1, Vector(7.3, 8.4, 9.5).toArray)
                                                  //> y  : axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 7.300000
                                                  //| 8.400000
                                                  //| 9.500000

  
  x.isEmpty                                       //> res6: Boolean = false
  x.isRowVector                                   //> res7: Boolean = false
  x.isColumnVector                                //> res8: Boolean = true
  x.isSquare                                      //> res9: Boolean = false
  x.isScalar                                      //> res10: Boolean = false
  x.rows                                          //> res11: Int = 3
  x.columns                                       //> res12: Int = 1
  x.length                                        //> res13: Int = 3


  // Accessing columns, rows, and elements

  x.column(0)                                     //> res14: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000
                                                  //| 5.100000
                                                  //| 6.200000
  x.row(1)                                        //> res15: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 5.100000
  x(2, 0)                                         //> res16: Double = 6.2

  // Other operations

  x.negate                                        //> res17: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = -4.000000
                                                  //| -5.100000
                                                  //| -6.200000
  x.transpose                                     //> res18: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000 5.10000
                                                  //| 0 6.200000
  x.ceil                                          //> res19: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000
                                                  //| 6.000000
                                                  //| 7.000000
  x.floor                                         //> res20: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000
                                                  //| 5.000000
                                                  //| 6.000000
  x.log                                           //> res21: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 1.386294
                                                  //| 1.629241
                                                  //| 1.824549
  x.log10                                         //> res22: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.602060
                                                  //| 0.707570
                                                  //| 0.792392
  x.pow(2.0)                                      //> res23: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 16.000000
                                                  //| 26.010000
                                                  //| 38.440000
  x.addScalar(1.1)                                //> res24: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 5.100000
                                                  //| 6.200000
                                                  //| 7.300000
  x.subtractScalar(0.2)                           //> res25: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 3.800000
                                                  //| 4.900000
                                                  //| 6.000000
  x.multiplyScalar(10.0)                          //> res26: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 40.000000
                                                  //| 51.000000
                                                  //| 62.000000
  x.divideScalar(100.0)                           //> res27: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.040000
                                                  //| 0.051000
                                                  //| 0.062000
  r.max                                           //> res28: Double = 0.902109886397674
  r.min                                           //> res29: Double = 3.0248102228036533E-4
  r.rowMaxs                                       //> res30: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.902110
                                                  //| 0.576008
                                                  //| 0.671649
  r.rowMins                                       //> res31: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.056911
                                                  //| 0.082596
                                                  //| 0.000302
  r.columnMaxs                                    //> res32: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.671649 0.4086
                                                  //| 94 0.902110
  r.columnMins                                    //> res33: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.056911 0.0825
                                                  //| 96 0.000302

  rowRange(r)                                     //> res34: axle.matrix.JblasMatrixFactory.M[Double] = 0.845199
                                                  //| 0.493412
                                                  //| 0.671346
  columnRange(r)                                  //> res35: axle.matrix.JblasMatrixFactory.M[Double] = 0.614738 0.326098 0.90180
                                                  //| 7

  r.sortRows                                      //> res36: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.056911 0.1244
                                                  //| 01 0.902110
                                                  //| 0.082596 0.102244 0.576008
                                                  //| 0.000302 0.408694 0.671649

  r.sortColumns                                   //> res37: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.056911 0.0825
                                                  //| 96 0.000302
                                                  //| 0.576008 0.124401 0.102244
                                                  //| 0.671649 0.408694 0.902110

  r.sortRows.sortColumns                          //> res38: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.000302 0.1022
                                                  //| 44 0.576008
                                                  //| 0.056911 0.124401 0.671649
                                                  //| 0.082596 0.408694 0.902110

  // Statistics

  r.rowMeans                                      //> res39: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.361141
                                                  //| 0.253616
                                                  //| 0.360215
  r.columnMeans                                   //> res40: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.434856 0.2052
                                                  //| 31 0.334886
  
  median(r)                                       //> res41: axle.matrix.JblasMatrixFactory.M[Double] = 0.576008 0.124401 0.10224
                                                  //| 4
  sumsq(r)                                        //> res42: axle.matrix.JblasMatrixFactory.M[Double] = 0.786136 0.189329 0.82425
                                                  //| 6
  std(r)                                          //> res43: axle.matrix.JblasMatrixFactory.M[Double] = 0.270085 0.144879 0.40324
                                                  //| 2
  cov(r)                                          //> res44: axle.matrix.JblasMatrixFactory.M[Double] = 0.072946 0.020473 -0.1088
                                                  //| 15
                                                  //| 0.020473 0.020990 -0.028465
                                                  //| -0.108815 -0.028465 0.162604
  centerRows(r)                                   //> res45: axle.matrix.JblasMatrixFactory.M[Double] = -0.304230 -0.129215 0.541
                                                  //| 895
                                                  //| 0.214868 -0.171020 -0.257971
                                                  //| 0.310508 0.155078 -0.359913
  centerColumns(r)                                //> res46: axle.matrix.JblasMatrixFactory.M[Double] = -0.377945 -0.080829 0.567
                                                  //| 224
                                                  //| 0.141152 -0.122634 -0.232641
                                                  //| 0.236793 0.203464 -0.334583
  zscore(r)                                       //> res47: axle.matrix.JblasMatrixFactory.M[Double] = -1.399357 -0.557908 1.406
                                                  //| 661
                                                  //| 0.522622 -0.846459 -0.576928
                                                  //| 0.876735 1.404367 -0.829734
  
  val (u, s) = pca(r)                             //> u  : axle.matrix.JblasMatrixFactory.M[Double] = -0.550078 0.024906 0.834742
                                                  //| 
                                                  //| -0.157269 -0.984760 -0.074255
                                                  //| 0.820171 -0.172125 0.545612
                                                  //| s  : axle.matrix.JblasMatrixFactory.M[Double] = 0.241043
                                                  //| 0.015497
                                                  //| 0.000000


  // Horizontal and vertical concatenation
  
  x +|+ y                                         //> res48: axle.matrix.JblasMatrixFactory.M[Double] = 4.000000 7.300000
                                                  //| 5.100000 8.400000
                                                  //| 6.200000 9.500000
  
  x +/+ y                                         //> res49: axle.matrix.JblasMatrixFactory.M[Double] = 4.000000
                                                  //| 5.100000
                                                  //| 6.200000
                                                  //| 7.300000
                                                  //| 8.400000
                                                  //| 9.500000

  // Addition and multiplication
  
  val o = ones[Double](3, 3)                      //> o  : axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 1.000000 1.000000
                                                  //|  1.000000
                                                  //| 1.000000 1.000000 1.000000
                                                  //| 1.000000 1.000000 1.000000
  val o2 = o * 2                                  //> o2  : axle.matrix.JblasMatrixFactory.M[Double] = 2.000000 2.000000 2.000000
                                                  //| 
                                                  //| 2.000000 2.000000 2.000000
                                                  //| 2.000000 2.000000 2.000000

  o.multiplyMatrix(o2)                            //> res50: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 6.000000 6.0000
                                                  //| 00 6.000000
                                                  //| 6.000000 6.000000 6.000000
                                                  //| 6.000000 6.000000 6.000000

  o + o2                                          //> res51: axle.matrix.JblasMatrixFactory.M[Double] = 3.000000 3.000000 3.00000
                                                  //| 0
                                                  //| 3.000000 3.000000 3.000000
                                                  //| 3.000000 3.000000 3.000000

  // Boolean operators
  val half = ones[Double](3, 3) / 2.0             //> half  : axle.matrix.JblasMatrixFactory.M[Double] = 0.500000 0.500000 0.5000
                                                  //| 00
                                                  //| 0.500000 0.500000 0.500000
                                                  //| 0.500000 0.500000 0.500000

  r lt half                                       //> res52: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true fals
                                                  //| e
                                                  //| false true true
                                                  //| false true true
  
  r le half                                       //> res53: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true fals
                                                  //| e
                                                  //| false true true
                                                  //| false true true
  
  r gt half                                       //> res54: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false tr
                                                  //| ue
                                                  //| true false false
                                                  //| true false false
  
  r ge half                                       //> res55: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false tr
                                                  //| ue
                                                  //| true false false
                                                  //| true false false
  
  r eq half                                       //> res56: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false fa
                                                  //| lse
                                                  //| false false false
                                                  //| false false false
  
  r ne half                                       //> res57: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| true true true
                                                  //| true true true
   
  (r lt half) or (r gt half)                      //> res58: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| true true true
                                                  //| true true true
  
  (r lt half) and (r gt half)                     //> res59: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false fa
                                                  //| lse
                                                  //| false false false
                                                  //| false false false
  
  (r lt half) xor (r gt half)                     //> res60: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| true true true
                                                  //| true true true
  
  (r lt half) not                                 //> res61: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false tr
                                                  //| ue
                                                  //| true false false
                                                  //| true false false

  // Higher order methods


  m.map(_ + 1)                                    //> res62: axle.matrix.JblasMatrixFactory.M[Int] = 2 6 10 14 18
                                                  //| 3 7 11 15 19
                                                  //| 4 8 12 16 20
                                                  //| 5 9 13 17 21

  m.map(_ * 10)                                   //> res63: axle.matrix.JblasMatrixFactory.M[Int] = 10 50 90 130 170
                                                  //| 20 60 100 140 180
                                                  //| 30 70 110 150 190
                                                  //| 40 80 120 160 200

  m.foldLeft(zeros[Int](4, 1))(_ + _)             //> res64: axle.matrix.JblasMatrixFactory.M[Int] = 45
                                                  //| 50
                                                  //| 55
                                                  //| 60

  m.foldLeft(ones[Int](4, 1))(_ mulPointwise _)   //> res65: axle.matrix.JblasMatrixFactory.M[Int] = 9945
                                                  //| 30240
                                                  //| 65835
                                                  //| 122880

  m.foldTop(zeros[Int](1, 5))(_ + _)              //> res66: axle.matrix.JblasMatrixFactory.M[Int] = 10 26 42 58 74

  m.foldTop(ones[Int](1, 5))(_ mulPointwise _)    //> res67: axle.matrix.JblasMatrixFactory.M[Int] = 24 1680 11880 43680 116280

}
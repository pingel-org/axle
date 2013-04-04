object MatrixDemo {

 println("Matrix Demo")                           //> Matrix Demo

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

  val r = rand[Double](3, 3)                      //> r  : axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.696931 0.416299 
                                                  //| 0.573214
                                                  //| 0.885652 0.647719 0.810950
                                                  //| 0.979660 0.692330 0.788157

  // Matrices defined by functions
  
  matrix(4, 5, (r, c) => r / (c+1.0))             //> res6: axle.matrix.JblasMatrixFactory.M[Double] = 0.000000 0.000000 0.000000 
                                                  //| 0.000000 0.000000
                                                  //| 1.000000 0.500000 0.333333 0.250000 0.200000
                                                  //| 2.000000 1.000000 0.666667 0.500000 0.400000
                                                  //| 3.000000 1.500000 1.000000 0.750000 0.600000

  matrix(4, 5, 1.0, (r: Int) => r + 0.5, (c: Int) => c + 0.6, (r: Int, c: Int, diag: Double, left: Double, right: Double) => diag)
                                                  //> res7: axle.matrix.JblasMatrixFactory.M[Double] = 0.600000 1.600000 2.600000 
                                                  //| 3.600000 4.600000
                                                  //| 1.500000 0.600000 1.600000 2.600000 3.600000
                                                  //| 2.500000 1.500000 0.600000 1.600000 2.600000
                                                  //| 3.500000 2.500000 1.500000 0.600000 1.600000

  // Metadata

  val x = matrix(3, 1, Vector(4.0, 5.1, 6.2).toArray)
                                                  //> x  : axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000
                                                  //| 5.100000
                                                  //| 6.200000
  
  val y = matrix(3, 1, Vector(7.3, 8.4, 9.5).toArray)
                                                  //> y  : axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 7.300000
                                                  //| 8.400000
                                                  //| 9.500000

  
  x.isEmpty                                       //> res8: Boolean = false
  x.isRowVector                                   //> res9: Boolean = false
  x.isColumnVector                                //> res10: Boolean = true
  x.isSquare                                      //> res11: Boolean = false
  x.isScalar                                      //> res12: Boolean = false
  x.rows                                          //> res13: Int = 3
  x.columns                                       //> res14: Int = 1
  x.length                                        //> res15: Int = 3


  // Accessing columns, rows, and elements

  x.column(0)                                     //> res16: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000
                                                  //| 5.100000
                                                  //| 6.200000
  x.row(1)                                        //> res17: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 5.100000
  x(2, 0)                                         //> res18: Double = 6.2

  val fiveByFive = matrix(5, 5, (1 to 25).toArray)//> fiveByFive  : axle.matrix.JblasMatrixFactory.JblasMatrix[Int] = 1 6 11 16 2
                                                  //| 1
                                                  //| 2 7 12 17 22
                                                  //| 3 8 13 18 23
                                                  //| 4 9 14 19 24
                                                  //| 5 10 15 20 25
  fiveByFive(1 to 3, 2 to 4)                      //> res19: axle.matrix.JblasMatrixFactory.M[Int] = 12 17 22
                                                  //| 13 18 23
                                                  //| 14 19 24

  fiveByFive(0.until(5,2), 0.until(5,2))          //> res20: axle.matrix.JblasMatrixFactory.M[Int] = 1 11 21
                                                  //| 3 13 23
                                                  //| 5 15 25

  // Other operations

  x.negate                                        //> res21: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = -4.000000
                                                  //| -5.100000
                                                  //| -6.200000
  x.transpose                                     //> res22: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000 5.1000
                                                  //| 00 6.200000
  x.ceil                                          //> res23: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000
                                                  //| 6.000000
                                                  //| 7.000000
  x.floor                                         //> res24: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000
                                                  //| 5.000000
                                                  //| 6.000000
  x.log                                           //> res25: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 1.386294
                                                  //| 1.629241
                                                  //| 1.824549
  x.log10                                         //> res26: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.602060
                                                  //| 0.707570
                                                  //| 0.792392
  x.pow(2.0)                                      //> res27: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 16.000000
                                                  //| 26.010000
                                                  //| 38.440000
  x.addScalar(1.1)                                //> res28: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 5.100000
                                                  //| 6.200000
                                                  //| 7.300000
  x.subtractScalar(0.2)                           //> res29: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 3.800000
                                                  //| 4.900000
                                                  //| 6.000000
  x.multiplyScalar(10.0)                          //> res30: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 40.000000
                                                  //| 51.000000
                                                  //| 62.000000
  x.divideScalar(100.0)                           //> res31: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.040000
                                                  //| 0.051000
                                                  //| 0.062000
  r.max                                           //> res32: Double = 0.9796597953349846
  r.min                                           //> res33: Double = 0.41629858519312235
  r.rowMaxs                                       //> res34: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.696931
                                                  //| 0.885652
                                                  //| 0.979660
  r.rowMins                                       //> res35: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.416299
                                                  //| 0.647719
                                                  //| 0.692330
  r.columnMaxs                                    //> res36: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.979660 0.6923
                                                  //| 30 0.810950
  r.columnMins                                    //> res37: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.696931 0.4162
                                                  //| 99 0.573214

  rowRange(r)                                     //> res38: axle.matrix.JblasMatrixFactory.M[Double] = 0.280633
                                                  //| 0.237933
                                                  //| 0.287329
  columnRange(r)                                  //> res39: axle.matrix.JblasMatrixFactory.M[Double] = 0.282728 0.276032 0.23773
                                                  //| 6

  r.sortRows                                      //> res40: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.416299 0.5732
                                                  //| 14 0.696931
                                                  //| 0.647719 0.810950 0.885652
                                                  //| 0.692330 0.788157 0.979660

  r.sortColumns                                   //> res41: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.696931 0.4162
                                                  //| 99 0.573214
                                                  //| 0.885652 0.647719 0.788157
                                                  //| 0.979660 0.692330 0.810950

  r.sortRows.sortColumns                          //> res42: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.416299 0.5732
                                                  //| 14 0.696931
                                                  //| 0.647719 0.788157 0.885652
                                                  //| 0.692330 0.810950 0.979660

  // Statistics

  r.rowMeans                                      //> res43: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.562148
                                                  //| 0.781440
                                                  //| 0.820049
  r.columnMeans                                   //> res44: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.854081 0.5854
                                                  //| 49 0.724107
  
  median(r)                                       //> res45: axle.matrix.JblasMatrixFactory.M[Double] = 0.885652 0.647719 0.78815
                                                  //| 7
  sumsq(r)                                        //> res46: axle.matrix.JblasMatrixFactory.M[Double] = 2.229827 1.072166 1.60740
                                                  //| 4
  std(r)                                          //> res47: axle.matrix.JblasMatrixFactory.M[Double] = 0.117562 0.120986 0.10710
                                                  //| 2
  cov(r)                                          //> res48: axle.matrix.JblasMatrixFactory.M[Double] = 0.013821 0.013990 0.01149
                                                  //| 9
                                                  //| 0.013990 0.014638 0.012592
                                                  //| 0.011499 0.012592 0.011471
  centerRows(r)                                   //> res49: axle.matrix.JblasMatrixFactory.M[Double] = 0.134783 -0.145849 0.0110
                                                  //| 66
                                                  //| 0.104212 -0.133721 0.029509
                                                  //| 0.159611 -0.127718 -0.031892
  centerColumns(r)                                //> res50: axle.matrix.JblasMatrixFactory.M[Double] = -0.157150 -0.169151 -0.15
                                                  //| 0893
                                                  //| 0.031571 0.062270 0.086843
                                                  //| 0.125579 0.106881 0.064050
  zscore(r)                                       //> res51: axle.matrix.JblasMatrixFactory.M[Double] = -1.336735 -1.398099 -1.40
                                                  //| 8866
                                                  //| 0.268549 0.514685 0.810841
                                                  //| 1.068186 0.883413 0.598025
  
  val (u, s) = pca(r)                             //> u  : axle.matrix.JblasMatrixFactory.M[Double] = -0.586208 0.659696 0.470279
                                                  //| 
                                                  //| -0.613874 0.017121 -0.789218
                                                  //| -0.528695 -0.751338 0.394934
                                                  //| s  : axle.matrix.JblasMatrixFactory.M[Double] = 0.038842
                                                  //| 0.001087
                                                  //| 0.000000


  // Horizontal and vertical concatenation
  
  x +|+ y                                         //> res52: axle.matrix.JblasMatrixFactory.M[Double] = 4.000000 7.300000
                                                  //| 5.100000 8.400000
                                                  //| 6.200000 9.500000
  
  x +/+ y                                         //> res53: axle.matrix.JblasMatrixFactory.M[Double] = 4.000000
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

  o.multiplyMatrix(o2)                            //> res54: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 6.000000 6.0000
                                                  //| 00 6.000000
                                                  //| 6.000000 6.000000 6.000000
                                                  //| 6.000000 6.000000 6.000000

  o + o2                                          //> res55: axle.matrix.JblasMatrixFactory.M[Double] = 3.000000 3.000000 3.00000
                                                  //| 0
                                                  //| 3.000000 3.000000 3.000000
                                                  //| 3.000000 3.000000 3.000000

  // Boolean operators
  val half = ones[Double](3, 3) / 2.0             //> half  : axle.matrix.JblasMatrixFactory.M[Double] = 0.500000 0.500000 0.5000
                                                  //| 00
                                                  //| 0.500000 0.500000 0.500000
                                                  //| 0.500000 0.500000 0.500000

  r lt half                                       //> res56: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false true fal
                                                  //| se
                                                  //| false false false
                                                  //| false false false
  
  r le half                                       //> res57: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false true fal
                                                  //| se
                                                  //| false false false
                                                  //| false false false
  
  r gt half                                       //> res58: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true false tru
                                                  //| e
                                                  //| true true true
                                                  //| true true true
  
  r ge half                                       //> res59: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true false tru
                                                  //| e
                                                  //| true true true
                                                  //| true true true
  
  r eq half                                       //> res60: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false fa
                                                  //| lse
                                                  //| false false false
                                                  //| false false false
  
  r ne half                                       //> res61: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| true true true
                                                  //| true true true
   
  (r lt half) or (r gt half)                      //> res62: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| true true true
                                                  //| true true true
  
  (r lt half) and (r gt half)                     //> res63: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false fa
                                                  //| lse
                                                  //| false false false
                                                  //| false false false
  
  (r lt half) xor (r gt half)                     //> res64: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| true true true
                                                  //| true true true
  
  (r lt half) not                                 //> res65: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true false tru
                                                  //| e
                                                  //| true true true
                                                  //| true true true

  // Higher order methods


  m.map(_ + 1)                                    //> res66: axle.matrix.JblasMatrixFactory.M[Int] = 2 6 10 14 18
                                                  //| 3 7 11 15 19
                                                  //| 4 8 12 16 20
                                                  //| 5 9 13 17 21

  m.map(_ * 10)                                   //> res67: axle.matrix.JblasMatrixFactory.M[Int] = 10 50 90 130 170
                                                  //| 20 60 100 140 180
                                                  //| 30 70 110 150 190
                                                  //| 40 80 120 160 200

  m.foldLeft(zeros[Int](4, 1))(_ + _)             //> res68: axle.matrix.JblasMatrixFactory.M[Int] = 45
                                                  //| 50
                                                  //| 55
                                                  //| 60

  m.foldLeft(ones[Int](4, 1))(_ mulPointwise _)   //> res69: axle.matrix.JblasMatrixFactory.M[Int] = 9945
                                                  //| 30240
                                                  //| 65835
                                                  //| 122880

  m.foldTop(zeros[Int](1, 5))(_ + _)              //> res70: axle.matrix.JblasMatrixFactory.M[Int] = 10 26 42 58 74

  m.foldTop(ones[Int](1, 5))(_ mulPointwise _)    //> res71: axle.matrix.JblasMatrixFactory.M[Int] = 24 1680 11880 43680 116280

}
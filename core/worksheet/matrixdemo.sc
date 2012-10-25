object matrixdemo {

 println("Matrix demo")                           //> Matrix demo

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

  val r = rand[Double](3, 3)                      //> r  : axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.997153 0.833041 
                                                  //| 0.938390
                                                  //| 0.297144 0.287289 0.241956
                                                  //| 0.434082 0.152285 0.892456

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
  r.max                                           //> res32: Double = 0.9971529841940523
  r.min                                           //> res33: Double = 0.15228489751361618
  r.rowMaxs                                       //> res34: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.997153
                                                  //| 0.297144
                                                  //| 0.892456
  r.rowMins                                       //> res35: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.833041
                                                  //| 0.241956
                                                  //| 0.152285
  r.columnMaxs                                    //> res36: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.997153 0.8330
                                                  //| 41 0.938390
  r.columnMins                                    //> res37: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.297144 0.1522
                                                  //| 85 0.241956

  rowRange(r)                                     //> res38: axle.matrix.JblasMatrixFactory.M[Double] = 0.164112
                                                  //| 0.055187
                                                  //| 0.740171
  columnRange(r)                                  //> res39: axle.matrix.JblasMatrixFactory.M[Double] = 0.700009 0.680756 0.69643
                                                  //| 4

  r.sortRows                                      //> res40: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.833041 0.9383
                                                  //| 90 0.997153
                                                  //| 0.241956 0.287289 0.297144
                                                  //| 0.152285 0.434082 0.892456

  r.sortColumns                                   //> res41: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.297144 0.1522
                                                  //| 85 0.241956
                                                  //| 0.434082 0.287289 0.892456
                                                  //| 0.997153 0.833041 0.938390

  r.sortRows.sortColumns                          //> res42: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.152285 0.2872
                                                  //| 89 0.297144
                                                  //| 0.241956 0.434082 0.892456
                                                  //| 0.833041 0.938390 0.997153

  // Statistics

  r.rowMeans                                      //> res43: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.922861
                                                  //| 0.275463
                                                  //| 0.492941
  r.columnMeans                                   //> res44: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.576126 0.4242
                                                  //| 05 0.690934
  
  median(r)                                       //> res45: axle.matrix.JblasMatrixFactory.M[Double] = 0.434082 0.287289 0.89245
                                                  //| 6
  sumsq(r)                                        //> res46: axle.matrix.JblasMatrixFactory.M[Double] = 1.271036 0.799683 1.73559
                                                  //| 7
  std(r)                                          //> res47: axle.matrix.JblasMatrixFactory.M[Double] = 0.302914 0.294298 0.31802
                                                  //| 9
  cov(r)                                          //> res48: axle.matrix.JblasMatrixFactory.M[Double] = 0.091757 0.082984 0.06693
                                                  //| 9
                                                  //| 0.082984 0.086611 0.035948
                                                  //| 0.066939 0.035948 0.101142
  centerRows(r)                                   //> res49: axle.matrix.JblasMatrixFactory.M[Double] = 0.074292 -0.089820 0.0155
                                                  //| 29
                                                  //| 0.021681 0.011826 -0.033507
                                                  //| -0.058859 -0.340656 0.399515
  centerColumns(r)                                //> res50: axle.matrix.JblasMatrixFactory.M[Double] = 0.421027 0.408836 0.24745
                                                  //| 6
                                                  //| -0.278983 -0.136916 -0.448978
                                                  //| -0.142044 -0.271920 0.201522
  zscore(r)                                       //> res51: axle.matrix.JblasMatrixFactory.M[Double] = 1.389920 1.389192 0.77809
                                                  //| 3
                                                  //| -0.920995 -0.465230 -1.411753
                                                  //| -0.468925 -0.923963 0.633660
  
  val (u, s) = pca(r)                             //> u  : axle.matrix.JblasMatrixFactory.M[Double] = -0.642064 -0.165697 -0.7485
                                                  //| 31
                                                  //| -0.549603 -0.581227 0.600093
                                                  //| -0.534500 0.796693 0.282118
                                                  //| s  : axle.matrix.JblasMatrixFactory.M[Double] = 0.218516
                                                  //| 0.060995
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

  r lt half                                       //> res56: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false fa
                                                  //| lse
                                                  //| true true true
                                                  //| true true false
  
  r le half                                       //> res57: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false fa
                                                  //| lse
                                                  //| true true true
                                                  //| true true false
  
  r gt half                                       //> res58: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| false false false
                                                  //| false false true
  
  r ge half                                       //> res59: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| false false false
                                                  //| false false true
  
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
  
  (r lt half) not                                 //> res65: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| false false false
                                                  //| false false true

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
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

  val r = rand[Double](3, 3)                      //> r  : axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.652756 0.865275 
                                                  //| 0.514742
                                                  //| 0.303693 0.011935 0.801281
                                                  //| 0.943349 0.744068 0.506211

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

  val fiveByFive = matrix(5, 5, (1 to 25).toArray)//> fiveByFive  : axle.matrix.JblasMatrixFactory.JblasMatrix[Int] = 1 6 11 16 21
                                                  //| 
                                                  //| 2 7 12 17 22
                                                  //| 3 8 13 18 23
                                                  //| 4 9 14 19 24
                                                  //| 5 10 15 20 25
  fiveByFive(1 to 3, 2 to 4)                      //> res17: axle.matrix.JblasMatrixFactory.M[Int] = 12 17 22
                                                  //| 13 18 23
                                                  //| 14 19 24

  fiveByFive(0.until(5,2), 0.until(5,2))          //> res18: axle.matrix.JblasMatrixFactory.M[Int] = 1 11 21
                                                  //| 3 13 23
                                                  //| 5 15 25

  // Other operations

  x.negate                                        //> res19: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = -4.000000
                                                  //| -5.100000
                                                  //| -6.200000
  x.transpose                                     //> res20: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000 5.10000
                                                  //| 0 6.200000
  x.ceil                                          //> res21: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000
                                                  //| 6.000000
                                                  //| 7.000000
  x.floor                                         //> res22: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 4.000000
                                                  //| 5.000000
                                                  //| 6.000000
  x.log                                           //> res23: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 1.386294
                                                  //| 1.629241
                                                  //| 1.824549
  x.log10                                         //> res24: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.602060
                                                  //| 0.707570
                                                  //| 0.792392
  x.pow(2.0)                                      //> res25: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 16.000000
                                                  //| 26.010000
                                                  //| 38.440000
  x.addScalar(1.1)                                //> res26: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 5.100000
                                                  //| 6.200000
                                                  //| 7.300000
  x.subtractScalar(0.2)                           //> res27: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 3.800000
                                                  //| 4.900000
                                                  //| 6.000000
  x.multiplyScalar(10.0)                          //> res28: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 40.000000
                                                  //| 51.000000
                                                  //| 62.000000
  x.divideScalar(100.0)                           //> res29: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.040000
                                                  //| 0.051000
                                                  //| 0.062000
  r.max                                           //> res30: Double = 0.9433486809752604
  r.min                                           //> res31: Double = 0.011934905411016605
  r.rowMaxs                                       //> res32: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.865275
                                                  //| 0.801281
                                                  //| 0.943349
  r.rowMins                                       //> res33: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.514742
                                                  //| 0.011935
                                                  //| 0.506211
  r.columnMaxs                                    //> res34: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.943349 0.8652
                                                  //| 75 0.801281
  r.columnMins                                    //> res35: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.303693 0.0119
                                                  //| 35 0.506211

  rowRange(r)                                     //> res36: axle.matrix.JblasMatrixFactory.M[Double] = 0.350533
                                                  //| 0.789346
                                                  //| 0.437138
  columnRange(r)                                  //> res37: axle.matrix.JblasMatrixFactory.M[Double] = 0.639656 0.853340 0.29507
                                                  //| 0

  r.sortRows                                      //> res38: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.514742 0.6527
                                                  //| 56 0.865275
                                                  //| 0.011935 0.303693 0.801281
                                                  //| 0.506211 0.744068 0.943349

  r.sortColumns                                   //> res39: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.303693 0.0119
                                                  //| 35 0.506211
                                                  //| 0.652756 0.744068 0.514742
                                                  //| 0.943349 0.865275 0.801281

  r.sortRows.sortColumns                          //> res40: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.011935 0.3036
                                                  //| 93 0.801281
                                                  //| 0.506211 0.652756 0.865275
                                                  //| 0.514742 0.744068 0.943349

  // Statistics

  r.rowMeans                                      //> res41: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.677591
                                                  //| 0.372303
                                                  //| 0.731209
  r.columnMeans                                   //> res42: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 0.633266 0.5404
                                                  //| 26 0.607412
  
  median(r)                                       //> res43: axle.matrix.JblasMatrixFactory.M[Double] = 0.652756 0.744068 0.51474
                                                  //| 2
  sumsq(r)                                        //> res44: axle.matrix.JblasMatrixFactory.M[Double] = 1.408226 1.302481 1.16326
                                                  //| 1
  std(r)                                          //> res45: axle.matrix.JblasMatrixFactory.M[Double] = 0.261502 0.376961 0.13713
                                                  //| 1
  cov(r)                                          //> res46: axle.matrix.JblasMatrixFactory.M[Double] = 0.068383 0.081218 -0.0323
                                                  //| 60
                                                  //| 0.081218 0.142100 -0.051057
                                                  //| -0.032360 -0.051057 0.018805
  centerRows(r)                                   //> res47: axle.matrix.JblasMatrixFactory.M[Double] = -0.024835 0.492972 -0.216
                                                  //| 467
                                                  //| -0.373898 -0.360368 0.070072
                                                  //| 0.265758 0.371765 -0.224998
  centerColumns(r)                                //> res48: axle.matrix.JblasMatrixFactory.M[Double] = 0.019490 0.324849 -0.0926
                                                  //| 69
                                                  //| -0.329573 -0.528491 0.193870
                                                  //| 0.310083 0.203642 -0.101200
  zscore(r)                                       //> res49: axle.matrix.JblasMatrixFactory.M[Double] = 0.074531 0.861757 -0.6757
                                                  //| 72
                                                  //| -1.260308 -1.401976 1.413757
                                                  //| 1.185777 0.540219 -0.737985
  
  val (u, s) = pca(r)                             //> u  : axle.matrix.JblasMatrixFactory.M[Double] = -0.516644 0.844981 0.138149
                                                  //| 
                                                  //| -0.803078 -0.534189 0.264020
                                                  //| 0.296890 0.025460 0.954572
                                                  //| s  : axle.matrix.JblasMatrixFactory.M[Double] = 0.213225
                                                  //| 0.016063
                                                  //| 0.000000


  // Horizontal and vertical concatenation
  
  x +|+ y                                         //> res50: axle.matrix.JblasMatrixFactory.M[Double] = 4.000000 7.300000
                                                  //| 5.100000 8.400000
                                                  //| 6.200000 9.500000
  
  x +/+ y                                         //> res51: axle.matrix.JblasMatrixFactory.M[Double] = 4.000000
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

  o.multiplyMatrix(o2)                            //> res52: axle.matrix.JblasMatrixFactory.JblasMatrix[Double] = 6.000000 6.0000
                                                  //| 00 6.000000
                                                  //| 6.000000 6.000000 6.000000
                                                  //| 6.000000 6.000000 6.000000

  o + o2                                          //> res53: axle.matrix.JblasMatrixFactory.M[Double] = 3.000000 3.000000 3.00000
                                                  //| 0
                                                  //| 3.000000 3.000000 3.000000
                                                  //| 3.000000 3.000000 3.000000

  // Boolean operators
  val half = ones[Double](3, 3) / 2.0             //> half  : axle.matrix.JblasMatrixFactory.M[Double] = 0.500000 0.500000 0.5000
                                                  //| 00
                                                  //| 0.500000 0.500000 0.500000
                                                  //| 0.500000 0.500000 0.500000

  r lt half                                       //> res54: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false fa
                                                  //| lse
                                                  //| true true false
                                                  //| false false false
  
  r le half                                       //> res55: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false fa
                                                  //| lse
                                                  //| true true false
                                                  //| false false false
  
  r gt half                                       //> res56: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| false false true
                                                  //| true true true
  
  r ge half                                       //> res57: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| false false true
                                                  //| true true true
  
  r eq half                                       //> res58: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false fa
                                                  //| lse
                                                  //| false false false
                                                  //| false false false
  
  r ne half                                       //> res59: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| true true true
                                                  //| true true true
   
  (r lt half) or (r gt half)                      //> res60: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| true true true
                                                  //| true true true
  
  (r lt half) and (r gt half)                     //> res61: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = false false fa
                                                  //| lse
                                                  //| false false false
                                                  //| false false false
  
  (r lt half) xor (r gt half)                     //> res62: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| true true true
                                                  //| true true true
  
  (r lt half) not                                 //> res63: axle.matrix.JblasMatrixFactory.JblasMatrix[Boolean] = true true true
                                                  //| 
                                                  //| false false true
                                                  //| true true true

  // Higher order methods


  m.map(_ + 1)                                    //> res64: axle.matrix.JblasMatrixFactory.M[Int] = 2 6 10 14 18
                                                  //| 3 7 11 15 19
                                                  //| 4 8 12 16 20
                                                  //| 5 9 13 17 21

  m.map(_ * 10)                                   //> res65: axle.matrix.JblasMatrixFactory.M[Int] = 10 50 90 130 170
                                                  //| 20 60 100 140 180
                                                  //| 30 70 110 150 190
                                                  //| 40 80 120 160 200

  m.foldLeft(zeros[Int](4, 1))(_ + _)             //> res66: axle.matrix.JblasMatrixFactory.M[Int] = 45
                                                  //| 50
                                                  //| 55
                                                  //| 60

  m.foldLeft(ones[Int](4, 1))(_ mulPointwise _)   //> res67: axle.matrix.JblasMatrixFactory.M[Int] = 9945
                                                  //| 30240
                                                  //| 65835
                                                  //| 122880

  m.foldTop(zeros[Int](1, 5))(_ + _)              //> res68: axle.matrix.JblasMatrixFactory.M[Int] = 10 26 42 58 74

  m.foldTop(ones[Int](1, 5))(_ mulPointwise _)    //> res69: axle.matrix.JblasMatrixFactory.M[Int] = 24 1680 11880 43680 116280

}
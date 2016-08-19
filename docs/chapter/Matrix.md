Matrix
======

Witnesses for the jblas jars including LinearAlgebra.

Establish implicit LinearAlgebra witness
----------------------------------------

```scala
scala> import axle._
import axle._

scala> import axle.jblas._
import axle.jblas._

scala> import axle.syntax.linearalgebra.matrixOps
import axle.syntax.linearalgebra.matrixOps

scala> import spire.implicits.DoubleAlgebra
import spire.implicits.DoubleAlgebra

scala> implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
laJblasDouble: axle.algebra.LinearAlgebra[org.jblas.DoubleMatrix,Int,Int,Double] = axle.jblas.package$$anon$12@950f3a6

scala> import laJblasDouble._
import laJblasDouble._
```

Creating Matrices
-----------------

```scala
scala> string(ones(2, 3))
res0: String =
1.000000 1.000000 1.000000
1.000000 1.000000 1.000000

scala> string(ones(1, 4))
res1: String = 1.000000 1.000000 1.000000 1.000000

scala> string(ones(4, 1))
res2: String =
1.000000
1.000000
1.000000
1.000000
```

Creating matrices from arrays
-----------------------------

```scala
scala> string(matrix(2, 2, List(1.1, 2.2, 3.3, 4.4).toArray))
res3: String =
1.100000 3.300000
2.200000 4.400000

scala> string(matrix(2, 2, List(1.1, 2.2, 3.3, 4.4).toArray).t)
res4: String =
1.100000 2.200000
3.300000 4.400000

scala> val m = matrix(4, 5, (1 to 20).map(_.toDouble).toArray)
m: org.jblas.DoubleMatrix = [1.000000, 5.000000, 9.000000, 13.000000, 17.000000; 2.000000, 6.000000, 10.000000, 14.000000, 18.000000; 3.000000, 7.000000, 11.000000, 15.000000, 19.000000; 4.000000, 8.000000, 12.000000, 16.000000, 20.000000]

scala> string(m)
res5: String =
1.000000 5.000000 9.000000 13.000000 17.000000
2.000000 6.000000 10.000000 14.000000 18.000000
3.000000 7.000000 11.000000 15.000000 19.000000
4.000000 8.000000 12.000000 16.000000 20.000000
```

Random matrices
---------------

```scala
scala> val r = rand(3, 3)
r: org.jblas.DoubleMatrix = [0.471153, 0.335687, 0.809630; 0.593703, 0.392600, 0.109539; 0.180983, 0.914234, 0.818691]

scala> string(r)
res6: String =
0.471153 0.335687 0.809630
0.593703 0.392600 0.109539
0.180983 0.914234 0.818691
```

Matrices defined by functions
-----------------------------

```scala
scala> string(matrix(4, 5, (r, c) => r / (c + 1d)))
res7: String =
0.000000 0.000000 0.000000 0.000000 0.000000
1.000000 0.500000 0.333333 0.250000 0.200000
2.000000 1.000000 0.666667 0.500000 0.400000
3.000000 1.500000 1.000000 0.750000 0.600000

scala> string(matrix(4, 5, 1d,
     |   (r: Int) => r + 0.5,
     |   (c: Int) => c + 0.6,
     |   (r: Int, c: Int, diag: Double, left: Double, right: Double) => diag))
res8: String =
1.000000 1.600000 2.600000 3.600000 4.600000
1.500000 1.000000 1.600000 2.600000 3.600000
2.500000 1.500000 1.000000 1.600000 2.600000
3.500000 2.500000 1.500000 1.000000 1.600000
```

Metadata
--------

```scala
scala> val x = matrix(3, 1, Vector(4.0, 5.1, 6.2).toArray)
x: org.jblas.DoubleMatrix = [4.000000; 5.100000; 6.200000]

scala> string(x)
res9: String =
4.000000
5.100000
6.200000

scala> val y = matrix(3, 1, Vector(7.3, 8.4, 9.5).toArray)
y: org.jblas.DoubleMatrix = [7.300000; 8.400000; 9.500000]

scala> string(y)
res10: String =
7.300000
8.400000
9.500000

scala> x.isEmpty
res11: Boolean = false

scala> x.isRowVector
res12: Boolean = false

scala> x.isColumnVector
res13: Boolean = true

scala> x.isSquare
res14: Boolean = false

scala> x.isScalar
res15: Boolean = false

scala> x.rows
res16: Int = 3

scala> x.columns
res17: Int = 1

scala> x.length
res18: Int = 3
```

Accessing columns, rows, and elements
-------------------------------------

```scala
scala> x.column(0)
res19: org.jblas.DoubleMatrix = [4.000000; 5.100000; 6.200000]

scala> x.row(1)
res20: org.jblas.DoubleMatrix = [5.100000]

scala> x.get(2, 0)
res21: Double = 6.2

scala> val fiveByFive = matrix(5, 5, (1 to 25).map(_.toDouble).toArray)
fiveByFive: org.jblas.DoubleMatrix = [1.000000, 6.000000, 11.000000, 16.000000, 21.000000; 2.000000, 7.000000, 12.000000, 17.000000, 22.000000; 3.000000, 8.000000, 13.000000, 18.000000, 23.000000; 4.000000, 9.000000, 14.000000, 19.000000, 24.000000; 5.000000, 10.000000, 15.000000, 20.000000, 25.000000]

scala> fiveByFive.slice(1 to 3, 2 to 4)
res22: org.jblas.DoubleMatrix = [12.000000, 17.000000, 22.000000; 13.000000, 18.000000, 23.000000; 14.000000, 19.000000, 24.000000]

scala> fiveByFive.slice(0.until(5,2), 0.until(5,2))
res23: org.jblas.DoubleMatrix = [1.000000, 11.000000, 21.000000; 3.000000, 13.000000, 23.000000; 5.000000, 15.000000, 25.000000]
```

Other mathematical operations
-----------------------------

```scala
scala> x.negate
res24: org.jblas.DoubleMatrix = [-4.000000; -5.100000; -6.200000]

scala> x.transpose
res25: org.jblas.DoubleMatrix = [4.000000, 5.100000, 6.200000]

scala> // x.ceil
     | // x.floor
     | // x.log
     | // x.log10
     | 
     | x.pow(2d)
res31: org.jblas.DoubleMatrix = [16.000000; 26.010000; 38.440000]

scala> x.addScalar(1.1)
res32: org.jblas.DoubleMatrix = [5.100000; 6.200000; 7.300000]

scala> x.subtractScalar(0.2)
res33: org.jblas.DoubleMatrix = [3.800000; 4.900000; 6.000000]

scala> // x.multiplyScalar(10d)
     | 
     | x.divideScalar(100d)
res36: org.jblas.DoubleMatrix = [0.040000; 0.051000; 0.062000]

scala> r.max
res37: Double = 0.9142339430630595

scala> r.min
res38: Double = 0.10953926057371133

scala> r.rowMaxs
res39: org.jblas.DoubleMatrix = [0.809630; 0.593703; 0.914234]

scala> r.rowMins
res40: org.jblas.DoubleMatrix = [0.335687; 0.109539; 0.180983]

scala> r.columnMaxs
res41: org.jblas.DoubleMatrix = [0.593703, 0.914234, 0.818691]

scala> r.columnMins
res42: org.jblas.DoubleMatrix = [0.180983, 0.335687, 0.109539]

scala> rowRange(r)
res43: org.jblas.DoubleMatrix = [0.473943; 0.484164; 0.733251]

scala> columnRange(r)
res44: org.jblas.DoubleMatrix = [0.412720, 0.578547, 0.709152]

scala> r.sortRows
res45: org.jblas.DoubleMatrix = [0.335687, 0.471153, 0.809630; 0.109539, 0.392600, 0.593703; 0.180983, 0.818691, 0.914234]

scala> r.sortColumns
res46: org.jblas.DoubleMatrix = [0.180983, 0.335687, 0.109539; 0.471153, 0.392600, 0.809630; 0.593703, 0.914234, 0.818691]

scala> r.sortRows.sortColumns
res47: org.jblas.DoubleMatrix = [0.109539, 0.392600, 0.593703; 0.180983, 0.471153, 0.809630; 0.335687, 0.818691, 0.914234]
```

Statistics
----------

```scala
scala> r.rowMeans
res48: org.jblas.DoubleMatrix = [0.538823; 0.365281; 0.637970]

scala> r.columnMeans
res49: org.jblas.DoubleMatrix = [0.415280, 0.547507, 0.579287]

scala> // median(r)
     | 
     | sumsq(r)
res52: org.jblas.DoubleMatrix = [0.607224, 1.102644, 1.337755]

scala> std(r)
res53: org.jblas.DoubleMatrix = [0.173062, 0.260354, 0.332182]

scala> cov(r)
res54: org.jblas.DoubleMatrix = [0.001041, -0.012598, -0.017990; -0.012598, 0.007999, -0.057423; -0.017990, -0.057423, 0.019105]

scala> centerRows(r)
res55: org.jblas.DoubleMatrix = [-0.067670, -0.203137, 0.270807; 0.228422, 0.027320, -0.255742; -0.456986, 0.276264, 0.180722]

scala> centerColumns(r)
res56: org.jblas.DoubleMatrix = [0.055873, -0.211820, 0.230343; 0.178423, -0.154907, -0.469748; -0.234296, 0.366727, 0.239404]

scala> zscore(r)
res57: org.jblas.DoubleMatrix = [0.322852, -0.813586, 0.693424; 1.030977, -0.594985, -1.414126; -1.353829, 1.408571, 0.720702]

scala> val (u, s) = pca(r, 0.95)
u: org.jblas.DoubleMatrix = [0.072637, -0.371308, -0.925664; 0.660425, -0.677577, 0.323617; -0.747371, -0.634838, 0.196004]
s: org.jblas.DoubleMatrix = [0.071596; 0.052706; 0.009254]

scala> string(u)
res58: String =
0.072637 -0.371308 -0.925664
0.660425 -0.677577 0.323617
-0.747371 -0.634838 0.196004

scala> string(s)
res59: String =
0.071596
0.052706
0.009254
```

Horizontal and vertical concatenation
-------------------------------------

```scala
scala> string(x aside y)
res60: String =
4.000000 7.300000
5.100000 8.400000
6.200000 9.500000

scala> string(x atop y)
res61: String =
4.000000
5.100000
6.200000
7.300000
8.400000
9.500000
```

Addition and multiplication
---------------------------

// val o = ones(3, 3)
// val o2 = o * 2
// o.multiplyMatrix(o2)
// o + o2

Map element values
------------------

```scala
scala> implicit val endo = axle.jblas.endoFunctorDoubleMatrix[Double]
endo: axle.algebra.Endofunctor[org.jblas.DoubleMatrix,Double] = axle.jblas.package$$anon$8@7cd2454

scala> import axle.syntax.endofunctor.endofunctorOps
import axle.syntax.endofunctor.endofunctorOps

scala> val half = ones(3, 3).map(_ / 2d)
half: org.jblas.DoubleMatrix = [0.500000, 0.500000, 0.500000; 0.500000, 0.500000, 0.500000; 0.500000, 0.500000, 0.500000]
```

Boolean operators
-----------------

```scala
scala> r lt half
res62: org.jblas.DoubleMatrix = [1.000000, 1.000000, 0.000000; 0.000000, 1.000000, 1.000000; 1.000000, 0.000000, 0.000000]

scala> r le half
res63: org.jblas.DoubleMatrix = [1.000000, 1.000000, 0.000000; 0.000000, 1.000000, 1.000000; 1.000000, 0.000000, 0.000000]

scala> r gt half
res64: org.jblas.DoubleMatrix = [0.000000, 0.000000, 1.000000; 1.000000, 0.000000, 0.000000; 0.000000, 1.000000, 1.000000]

scala> r ge half
res65: org.jblas.DoubleMatrix = [0.000000, 0.000000, 1.000000; 1.000000, 0.000000, 0.000000; 0.000000, 1.000000, 1.000000]

scala> r eq half
res66: org.jblas.DoubleMatrix = [0.000000, 0.000000, 0.000000; 0.000000, 0.000000, 0.000000; 0.000000, 0.000000, 0.000000]

scala> r ne half
res67: org.jblas.DoubleMatrix = [1.000000, 1.000000, 1.000000; 1.000000, 1.000000, 1.000000; 1.000000, 1.000000, 1.000000]

scala> (r lt half) or (r gt half)
res68: org.jblas.DoubleMatrix = [1.000000, 1.000000, 1.000000; 1.000000, 1.000000, 1.000000; 1.000000, 1.000000, 1.000000]

scala> (r lt half) and (r gt half)
res69: org.jblas.DoubleMatrix = [0.000000, 0.000000, 0.000000; 0.000000, 0.000000, 0.000000; 0.000000, 0.000000, 0.000000]

scala> (r lt half) xor (r gt half)
res70: org.jblas.DoubleMatrix = [1.000000, 1.000000, 1.000000; 1.000000, 1.000000, 1.000000; 1.000000, 1.000000, 1.000000]

scala> (r lt half) not
res71: org.jblas.DoubleMatrix = [0.000000, 0.000000, 1.000000; 1.000000, 0.000000, 0.000000; 0.000000, 1.000000, 1.000000]
```

Higher order methods
--------------------

```scala
scala> m.map(_ + 1)
res72: org.jblas.DoubleMatrix = [2.000000, 6.000000, 10.000000, 14.000000, 18.000000; 3.000000, 7.000000, 11.000000, 15.000000, 19.000000; 4.000000, 8.000000, 12.000000, 16.000000, 20.000000; 5.000000, 9.000000, 13.000000, 17.000000, 21.000000]

scala> m.map(_ * 10)
res73: org.jblas.DoubleMatrix = [10.000000, 50.000000, 90.000000, 130.000000, 170.000000; 20.000000, 60.000000, 100.000000, 140.000000, 180.000000; 30.000000, 70.000000, 110.000000, 150.000000, 190.000000; 40.000000, 80.000000, 120.000000, 160.000000, 200.000000]

scala> // m.foldLeft(zeros(4, 1))(_ + _)
     | 
     | m.foldLeft(ones(4, 1))(_ mulPointwise _)
res76: org.jblas.DoubleMatrix = [9945.000000; 30240.000000; 65835.000000; 122880.000000]

scala> // m.foldTop(zeros(1, 5))(_ + _)
     | 
     | m.foldTop(ones(1, 5))(_ mulPointwise _)
res79: org.jblas.DoubleMatrix = [24.000000, 1680.000000, 11880.000000, 43680.000000, 116280.000000]
```

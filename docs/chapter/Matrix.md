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
laJblasDouble: axle.algebra.LinearAlgebra[org.jblas.DoubleMatrix,Int,Int,Double] = axle.jblas.package$$anon$12@4b4ae52

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

scala> val m = matrix(4, 5, (1 to 20).map(_.toDouble).toArray)\nstring(m)
<console>:24: error: value \ is not a member of org.jblas.DoubleMatrix
       val m = matrix(4, 5, (1 to 20).map(_.toDouble).toArray)\nstring(m)
                                                              ^
<console>:24: error: not found: value nstring
       val m = matrix(4, 5, (1 to 20).map(_.toDouble).toArray)\nstring(m)
                                                               ^
```

Random matrices
---------------

```scala
scala> val r = rand(3, 3)\nstring(r)
<console>:24: error: value \ is not a member of org.jblas.DoubleMatrix
       val r = rand(3, 3)\nstring(r)
                         ^
<console>:24: error: not found: value nstring
       val r = rand(3, 3)\nstring(r)
                          ^
```

Matrices defined by functions
-----------------------------

```scala
scala> string(matrix(4, 5, (r, c) => r / (c + 1d)))
res5: String =
0.000000 0.000000 0.000000 0.000000 0.000000
1.000000 0.500000 0.333333 0.250000 0.200000
2.000000 1.000000 0.666667 0.500000 0.400000
3.000000 1.500000 1.000000 0.750000 0.600000

scala> string(matrix(4, 5, 1d,
     |   (r: Int) => r + 0.5,
     |   (c: Int) => c + 0.6,
     |   (r: Int, c: Int, diag: Double, left: Double, right: Double) => diag))
res6: String =
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
res7: String =
4.000000
5.100000
6.200000

scala> val y = matrix(3, 1, Vector(7.3, 8.4, 9.5).toArray)
y: org.jblas.DoubleMatrix = [7.300000; 8.400000; 9.500000]

scala> string(y)
res8: String =
7.300000
8.400000
9.500000

scala> x.isEmpty
res9: Boolean = false

scala> x.isRowVector
res10: Boolean = false

scala> x.isColumnVector
res11: Boolean = true

scala> x.isSquare
res12: Boolean = false

scala> x.isScalar
res13: Boolean = false

scala> x.rows
res14: Int = 3

scala> x.columns
res15: Int = 1

scala> x.length
res16: Int = 3
```

Accessing columns, rows, and elements
-------------------------------------

```scala
scala> x.column(0)
res17: org.jblas.DoubleMatrix = [4.000000; 5.100000; 6.200000]

scala> x.row(1)
res18: org.jblas.DoubleMatrix = [5.100000]

scala> x.get(2, 0)
res19: Double = 6.2

scala> val fiveByFive = matrix(5, 5, (1 to 25).map(_.toDouble).toArray)
fiveByFive: org.jblas.DoubleMatrix = [1.000000, 6.000000, 11.000000, 16.000000, 21.000000; 2.000000, 7.000000, 12.000000, 17.000000, 22.000000; 3.000000, 8.000000, 13.000000, 18.000000, 23.000000; 4.000000, 9.000000, 14.000000, 19.000000, 24.000000; 5.000000, 10.000000, 15.000000, 20.000000, 25.000000]

scala> fiveByFive.slice(1 to 3, 2 to 4)
res20: org.jblas.DoubleMatrix = [12.000000, 17.000000, 22.000000; 13.000000, 18.000000, 23.000000; 14.000000, 19.000000, 24.000000]

scala> fiveByFive.slice(0.until(5,2), 0.until(5,2))
res21: org.jblas.DoubleMatrix = [1.000000, 11.000000, 21.000000; 3.000000, 13.000000, 23.000000; 5.000000, 15.000000, 25.000000]
```

Other mathematical operations
-----------------------------

```scala
scala> x.negate
res22: org.jblas.DoubleMatrix = [-4.000000; -5.100000; -6.200000]

scala> x.transpose
res23: org.jblas.DoubleMatrix = [4.000000, 5.100000, 6.200000]

scala> // x.ceil
     | // x.floor
     | // x.log
     | // x.log10
     | 
     | x.pow(2d)
res29: org.jblas.DoubleMatrix = [16.000000; 26.010000; 38.440000]

scala> x.addScalar(1.1)
res30: org.jblas.DoubleMatrix = [5.100000; 6.200000; 7.300000]

scala> x.subtractScalar(0.2)
res31: org.jblas.DoubleMatrix = [3.800000; 4.900000; 6.000000]

scala> // x.multiplyScalar(10d)
     | 
     | x.divideScalar(100d)
res34: org.jblas.DoubleMatrix = [0.040000; 0.051000; 0.062000]

scala> r.max
<console>:25: error: not found: value r
       r.max
       ^

scala> r.min
<console>:25: error: not found: value r
       r.min
       ^

scala> r.rowMaxs
<console>:25: error: not found: value r
       r.rowMaxs
       ^

scala> r.rowMins
<console>:25: error: not found: value r
       r.rowMins
       ^

scala> r.columnMaxs
<console>:25: error: not found: value r
       r.columnMaxs
       ^

scala> r.columnMins
<console>:25: error: not found: value r
       r.columnMins
       ^

scala> rowRange(r)
<console>:25: error: not found: value r
       rowRange(r)
                ^

scala> columnRange(r)
<console>:25: error: not found: value r
       columnRange(r)
                   ^

scala> r.sortRows
<console>:25: error: not found: value r
       r.sortRows
       ^

scala> r.sortColumns
<console>:25: error: not found: value r
       r.sortColumns
       ^

scala> r.sortRows.sortColumns
<console>:25: error: not found: value r
       r.sortRows.sortColumns
       ^
```

Statistics
----------

```scala
scala> r.rowMeans
<console>:25: error: not found: value r
       r.rowMeans
       ^
scala> r.columnMeans
<console>:25: error: not found: value r
       r.columnMeans
       ^
scala> // median(r)
     | 
     | sumsq(r)
<console>:27: error: not found: value r
       sumsq(r)
             ^
     | 
     | std(r)
<console>:28: error: not found: value r
       std(r)
           ^
     | 
     | cov(r)
<console>:29: error: not found: value r
       cov(r)
           ^
     | 
     | centerRows(r)
<console>:30: error: not found: value r
       centerRows(r)
                  ^
     | 
     | centerColumns(r)
<console>:31: error: not found: value r
       centerColumns(r)
                     ^
     | 
     | zscore(r)
<console>:32: error: not found: value r
       zscore(r)
              ^
     | 
     | val (u, s) = pca(r, 0.95)\nstring(u)
<console>:32: error: not found: value r
       val (u, s) = pca(r, 0.95)\nstring(u)
                        ^
<console>:32: error: not found: value nstring
       val (u, s) = pca(r, 0.95)\nstring(u)
                                 ^
<console>:32: error: recursive value x$1 needs type
       val (u, s) = pca(r, 0.95)\nstring(u)
            ^
     | 
     | string(s)
<console>:34: error: not found: value s
       string(s)
              ^
```

Horizontal and vertical concatenation
-------------------------------------

```scala
     | string(x aside y)
res64: String =
4.000000 7.300000
5.100000 8.400000
6.200000 9.500000

scala> string(x atop y)
res65: String =
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
endo: axle.algebra.Endofunctor[org.jblas.DoubleMatrix,Double] = axle.jblas.package$$anon$8@6a153d46

scala> import axle.syntax.endofunctor.endofunctorOps
import axle.syntax.endofunctor.endofunctorOps

scala> val half = ones(3, 3).map(_ / 2d)
half: org.jblas.DoubleMatrix = [0.500000, 0.500000, 0.500000; 0.500000, 0.500000, 0.500000; 0.500000, 0.500000, 0.500000]
```

Boolean operators
-----------------

```scala
scala> r lt half
<console>:28: error: not found: value r
       r lt half
       ^
scala> r le half
<console>:28: error: not found: value r
       r le half
       ^
scala> r gt half
<console>:28: error: not found: value r
       r gt half
       ^
scala> r ge half
<console>:28: error: not found: value r
       r ge half
       ^
scala> r eq half
<console>:28: error: not found: value r
       r eq half
       ^
scala> r ne half
<console>:28: error: not found: value r
       r ne half
       ^
scala> (r lt half) or (r gt half)
<console>:28: error: not found: value r
       (r lt half) or (r gt half)
        ^
<console>:28: error: not found: value r
       (r lt half) or (r gt half)
                       ^
scala> (r lt half) and (r gt half)
<console>:28: error: not found: value r
       (r lt half) and (r gt half)
        ^
<console>:28: error: not found: value r
       (r lt half) and (r gt half)
                        ^
scala> (r lt half) xor (r gt half)
<console>:28: error: not found: value r
       (r lt half) xor (r gt half)
        ^
<console>:28: error: not found: value r
       (r lt half) xor (r gt half)
                        ^
scala> (r lt half) not
<console>:28: error: not found: value r
       (r lt half) not
        ^
```

Higher order methods
--------------------

```scala
scala> m.map(_ + 1)
<console>:27: error: not found: value m
       m.map(_ + 1)
       ^
scala> m.map(_ * 10)
<console>:27: error: not found: value m
       m.map(_ * 10)
       ^
scala> // m.foldLeft(zeros(4, 1))(_ + _)
     | 
     | m.foldLeft(ones(4, 1))(_ mulPointwise _)
<console>:29: error: not found: value m
       m.foldLeft(ones(4, 1))(_ mulPointwise _)
       ^
     | 
     | // m.foldTop(zeros(1, 5))(_ + _)
     | 
     | m.foldTop(ones(1, 5))(_ mulPointwise _)
<console>:32: error: not found: value m
       m.foldTop(ones(1, 5))(_ mulPointwise _)
       ^
```

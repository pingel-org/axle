---
layout: page
title: Matrix
permalink: /tutorial/matrix/
---

Witnesses for the jblas jars including LinearAlgebra.

Establish implicit LinearAlgebra witness
----------------------------------------

```tut:book
import axle._
import axle.jblas._
import axle.syntax.linearalgebra.matrixOps
import spire.implicits.DoubleAlgebra

implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
import laJblasDouble._
```

Creating Matrices
-----------------

```tut:book
string(ones(2, 3))

string(ones(1, 4))

string(ones(4, 1))
```

Creating matrices from arrays
-----------------------------

```tut:book
string(matrix(2, 2, List(1.1, 2.2, 3.3, 4.4).toArray))

string(matrix(2, 2, List(1.1, 2.2, 3.3, 4.4).toArray).t)

val m = matrix(4, 5, (1 to 20).map(_.toDouble).toArray)
string(m)
```

Random matrices
---------------

```tut:book
val r = rand(3, 3)
string(r)
```

Matrices defined by functions
-----------------------------

```tut:book
string(matrix(4, 5, (r, c) => r / (c + 1d)))

string(matrix(4, 5, 1d,
  (r: Int) => r + 0.5,
  (c: Int) => c + 0.6,
  (r: Int, c: Int, diag: Double, left: Double, right: Double) => diag))
```

Metadata
--------

```tut:book
val x = matrix(3, 1, Vector(4.0, 5.1, 6.2).toArray)
string(x)

val y = matrix(3, 1, Vector(7.3, 8.4, 9.5).toArray)
string(y)

x.isEmpty

x.isRowVector

x.isColumnVector

x.isSquare

x.isScalar

x.rows

x.columns

x.length
```

Accessing columns, rows, and elements
-------------------------------------

```tut:book
x.column(0)

x.row(1)

x.get(2, 0)

val fiveByFive = matrix(5, 5, (1 to 25).map(_.toDouble).toArray)

fiveByFive.slice(1 to 3, 2 to 4)

fiveByFive.slice(0.until(5,2), 0.until(5,2))
```

Other mathematical operations
-----------------------------

```tut:book
x.negate

x.transpose

// x.ceil
// x.floor
// x.log
// x.log10

x.pow(2d)

x.addScalar(1.1)

x.subtractScalar(0.2)

// x.multiplyScalar(10d)

x.divideScalar(100d)

r.max

r.min

r.rowMaxs

r.rowMins

r.columnMaxs

r.columnMins

rowRange(r)

columnRange(r)

r.sortRows

r.sortColumns

r.sortRows.sortColumns
```

Statistics
----------

```tut:book
r.rowMeans

r.columnMeans

// median(r)

sumsq(r)

std(r)

cov(r)

centerRows(r)

centerColumns(r)

zscore(r)

val (u, s) = pca(r, 0.95)
string(u)

string(s)
```

Horizontal and vertical concatenation
-------------------------------------

```tut:book
string(x aside y)

string(x atop y)
```

Addition and multiplication
---------------------------

// val o = ones(3, 3)
// val o2 = o * 2
// o.multiplyMatrix(o2)
// o + o2

Map element values
------------------

```tut:book
implicit val endo = axle.jblas.endoFunctorDoubleMatrix[Double]
import axle.syntax.endofunctor.endofunctorOps

val half = ones(3, 3).map(_ / 2d)
```

Boolean operators
-----------------

```tut:book
r lt half

r le half

r gt half

r ge half

r eq half

r ne half

(r lt half) or (r gt half)

(r lt half) and (r gt half)

(r lt half) xor (r gt half)

(r lt half) not
```

Higher order methods
--------------------

```tut:book
m.map(_ + 1)

m.map(_ * 10)

// m.foldLeft(zeros(4, 1))(_ + _)

m.foldLeft(ones(4, 1))(_ mulPointwise _)

// m.foldTop(zeros(1, 5))(_ + _)

m.foldTop(ones(1, 5))(_ mulPointwise _)
```

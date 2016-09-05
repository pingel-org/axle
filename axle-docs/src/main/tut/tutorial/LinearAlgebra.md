---
layout: page
title: Linear Algebra
permalink: /tutorial/linear_algebra/
---

A `LinearAlgebra` typeclass.

The `axle-jblas` spoke provides witnesses for JBLAS matrices.

The default jblas matrix `toString` isn't very readable,
so this tutorial wraps most results in the Axle `string` function,
invoking Axle's `Show` witness for those matrices.

Imports and implicits
---------------------

Import JBLAS and Axle's `LinearAlgebra` witness for it.

```tut:silent
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
string(x.column(0))

string(x.row(1))

x.get(2, 0)

val fiveByFive = matrix(5, 5, (1 to 25).map(_.toDouble).toArray)

string(fiveByFive)

string(fiveByFive.slice(1 to 3, 2 to 4))

string(fiveByFive.slice(0.until(5,2), 0.until(5,2)))
```

Negate, Transpose, Power
------------------------

```tut:book
string(x.negate)

string(x.transpose)

// x.log
// x.log10

string(x.pow(2d))
```

Mins, Maxs, Ranges, and Sorts
-----------------------------

```
r.max

r.min

// r.ceil
// r.floor

string(r.rowMaxs)

string(r.rowMins)

string(r.columnMaxs)

string(r.columnMins)

string(rowRange(r))

string(columnRange(r))

string(r.sortRows)

string(r.sortColumns)

string(r.sortRows.sortColumns)
```

Statistics
----------

```tut:book
string(r.rowMeans)

string(r.columnMeans)

// median(r)

string(sumsq(r))

string(std(r))

string(cov(r))

string(centerRows(r))

string(centerColumns(r))

string(zscore(r))
```

Principal Component Analysis
----------------------------

```tut:book
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

Addition and subtraction
------------------------

```tut:book
val x = ones(2, 3)

string(x)
```

Matrix addition

```tut:book
import spire.implicits.additiveSemigroupOps

string(x + x)
```

Scalar addition (JBLAS method)

```tut:book
string(x.addScalar(1.1))
```

Matrix subtraction

```tut:book
import spire.implicits.additiveGroupOps

string(x - x)
```

Scalar subtraction (JBLAS method)

```tut:book
string(x.subtractScalar(0.2))
```

Scalar multiplication

```tut:book
import spire.implicits.moduleOps

string(x :* 3d)
```

Matrix multiplication

```tut:book
import spire.implicits.multiplicativeSemigroupOps

string(x * x.transpose)
```

Scalar division (JBLAS method)

```tut:book
string(x.divideScalar(100d))
```

Map element values
------------------

```tut:book
implicit val endo = axle.jblas.endoFunctorDoubleMatrix[Double]
import axle.syntax.endofunctor.endofunctorOps

val half = ones(3, 3).map(_ / 2d)

string(half)
```

Boolean operators
-----------------

```tut:book
string(r lt half)

string(r le half)

string(r gt half)

string(r ge half)

string(r eq half)

string(r ne half)

string((r lt half) or (r gt half))

string((r lt half) and (r gt half))

string((r lt half) xor (r gt half))

string((r lt half) not)
```

Higher order methods
--------------------

```tut:book
string(m.map(_ + 1))

string(m.map(_ * 10))

// m.foldLeft(zeros(4, 1))(_ + _)

string(m.foldLeft(ones(4, 1))(_ mulPointwise _))

// m.foldTop(zeros(1, 5))(_ + _)

string(m.foldTop(ones(1, 5))(_ mulPointwise _))
```

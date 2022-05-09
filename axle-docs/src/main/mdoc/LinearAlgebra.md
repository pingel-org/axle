# Linear Algebra

A `LinearAlgebra` typeclass.

The `axle-jblas` spoke provides witnesses for JBLAS matrices.

The default jblas matrix `toString` isn't very readable,
so this tutorial wraps most results in the Axle `string` function,
invoking the `cats.Show` witness for those matrices.

## Imports and implicits

Import JBLAS and Axle's `LinearAlgebra` witness for it.

```scala mdoc:silent:reset
import cats.implicits._

import spire.algebra.Field
import spire.algebra.NRoot

import axle._
import axle.jblas._
import axle.syntax.linearalgebra.matrixOps

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
import laJblasDouble._
```

## Creating Matrices

```scala mdoc
ones(2, 3).show

ones(1, 4).show

ones(4, 1).show
```

## Creating matrices from arrays

```scala mdoc
fromColumnMajorArray(2, 2, List(1.1, 2.2, 3.3, 4.4).toArray).show

fromColumnMajorArray(2, 2, List(1.1, 2.2, 3.3, 4.4).toArray).t.show

val m = fromColumnMajorArray(4, 5, (1 to 20).map(_.toDouble).toArray)
m.show
```

## Random matrices

```scala mdoc
val r = rand(3, 3)

r.show
```

## Matrices defined by functions

```scala mdoc
matrix(4, 5, (r, c) => r / (c + 1d)).show

matrix(4, 5, 1d,
  (r: Int) => r + 0.5,
  (c: Int) => c + 0.6,
  (r: Int, c: Int, diag: Double, left: Double, right: Double) => diag).show
```

## Metadata

```scala mdoc
val x = fromColumnMajorArray(3, 1, Vector(4.0, 5.1, 6.2).toArray)
x.show

val y = fromColumnMajorArray(3, 1, Vector(7.3, 8.4, 9.5).toArray)
y.show

x.isEmpty

x.isRowVector

x.isColumnVector

x.isSquare

x.isScalar

x.rows

x.columns

x.length
```

## Accessing columns, rows, and elements

```scala mdoc
x.column(0).show

x.row(1).show

x.get(2, 0)

val fiveByFive = fromColumnMajorArray(5, 5, (1 to 25).map(_.toDouble).toArray)

fiveByFive.show

fiveByFive.slice(1 to 3, 2 to 4).show

fiveByFive.slice(0.until(5,2), 0.until(5,2)).show
```

## Negate, Transpose, Power

```scala mdoc
x.negate.show

x.transpose.show

// x.log
// x.log10

x.pow(2d).show
```

## Mins, Maxs, Ranges, and Sorts

```scala mdoc
r.max

r.min

// r.ceil
// r.floor

r.rowMaxs.show

r.rowMins.show

r.columnMaxs.show

r.columnMins.show

rowRange(r).show

columnRange(r).show

r.sortRows.show

r.sortColumns.show

r.sortRows.sortColumns.show
```

## Statistics

```scala mdoc
r.rowMeans.show

r.columnMeans.show

// median(r)

sumsq(r).show

std(r).show

cov(r).show

centerRows(r).show

centerColumns(r).show

zscore(r).show
```

## Principal Component Analysis

```scala mdoc
val (u, s) = pca(r)

u.show

s.show
```

## Horizontal and vertical concatenation

```scala mdoc
(x aside y).show

(x atop y).show
```

## Addition and subtraction

```scala mdoc
val z = ones(2, 3)

z.show
```

Matrix addition

```scala mdoc
import spire.implicits.additiveSemigroupOps

(z + z).show
```

Scalar addition (JBLAS method)

```scala mdoc
z.addScalar(1.1).show
```

Matrix subtraction

```scala mdoc
import spire.implicits.additiveGroupOps

(z - z).show
```

Scalar subtraction (JBLAS method)

```scala mdoc
z.subtractScalar(0.2).show
```

## Multiplication and Division

Scalar multiplication

```scala mdoc
z.multiplyScalar(3d).show
```

Matrix multiplication

```scala mdoc
import spire.implicits.multiplicativeSemigroupOps

(z * z.transpose).show
```

Scalar division (JBLAS method)

```scala mdoc
z.divideScalar(100d).show
```

## Map element values

```scala mdoc
implicit val endo = axle.jblas.endoFunctorDoubleMatrix[Double]
import axle.syntax.endofunctor.endofunctorOps

val half = ones(3, 3).map(_ / 2d)

half.show
```

## Boolean operators

```scala mdoc
(r lt half).show

(r le half).show

(r gt half).show

(r ge half).show

(r eq half).show

(r ne half).show

((r lt half) or (r gt half)).show

((r lt half) and (r gt half)).show

((r lt half) xor (r gt half)).show

((r lt half) not).show
```

## Higher order methods

```scala mdoc
(m.map(_ + 1)).show

(m.map(_ * 10)).show

// m.foldLeft(zeros(4, 1))(_ + _)

(m.foldLeft(ones(4, 1))(_ mulPointwise _)).show

// m.foldTop(zeros(1, 5))(_ + _)

(m.foldTop(ones(1, 5))(_ mulPointwise _)).show
```

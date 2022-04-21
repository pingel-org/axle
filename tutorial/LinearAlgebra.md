---
layout: page
title: Linear Algebra
permalink: /tutorial/linear_algebra/
---

A `LinearAlgebra` typeclass.

The `axle-jblas` spoke provides witnesses for JBLAS matrices.

The default jblas matrix `toString` isn't very readable,
so this tutorial wraps most results in the Axle `string` function,
invoking the `cats.Show` witness for those matrices.

## Imports and implicits

Import JBLAS and Axle's `LinearAlgebra` witness for it.

```scala
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

```scala
ones(2, 3).show
// res0: String = """1.000000 1.000000 1.000000
// 1.000000 1.000000 1.000000"""

ones(1, 4).show
// res1: String = "1.000000 1.000000 1.000000 1.000000"

ones(4, 1).show
// res2: String = """1.000000
// 1.000000
// 1.000000
// 1.000000"""
```

## Creating matrices from arrays

```scala
fromColumnMajorArray(2, 2, List(1.1, 2.2, 3.3, 4.4).toArray).show
// res3: String = """1.100000 3.300000
// 2.200000 4.400000"""

fromColumnMajorArray(2, 2, List(1.1, 2.2, 3.3, 4.4).toArray).t.show
// res4: String = """1.100000 2.200000
// 3.300000 4.400000"""

val m = fromColumnMajorArray(4, 5, (1 to 20).map(_.toDouble).toArray)
// m: org.jblas.DoubleMatrix = [1.000000, 5.000000, 9.000000, 13.000000, 17.000000; 2.000000, 6.000000, 10.000000, 14.000000, 18.000000; 3.000000, 7.000000, 11.000000, 15.000000, 19.000000; 4.000000, 8.000000, 12.000000, 16.000000, 20.000000]
m.show
// res5: String = """1.000000 5.000000 9.000000 13.000000 17.000000
// 2.000000 6.000000 10.000000 14.000000 18.000000
// 3.000000 7.000000 11.000000 15.000000 19.000000
// 4.000000 8.000000 12.000000 16.000000 20.000000"""
```

## Random matrices

```scala
val r = rand(3, 3)
// r: org.jblas.DoubleMatrix = [0.952109, 0.970346, 0.444361; 0.789029, 0.794303, 0.097557; 0.955493, 0.460526, 0.275987]

r.show
// res6: String = """0.952109 0.970346 0.444361
// 0.789029 0.794303 0.097557
// 0.955493 0.460526 0.275987"""
```

## Matrices defined by functions

```scala
matrix(4, 5, (r, c) => r / (c + 1d)).show
// res7: String = """0.000000 0.000000 0.000000 0.000000 0.000000
// 1.000000 0.500000 0.333333 0.250000 0.200000
// 2.000000 1.000000 0.666667 0.500000 0.400000
// 3.000000 1.500000 1.000000 0.750000 0.600000"""

matrix(4, 5, 1d,
  (r: Int) => r + 0.5,
  (c: Int) => c + 0.6,
  (r: Int, c: Int, diag: Double, left: Double, right: Double) => diag).show
// res8: String = """1.000000 1.600000 2.600000 3.600000 4.600000
// 1.500000 1.000000 1.600000 2.600000 3.600000
// 2.500000 1.500000 1.000000 1.600000 2.600000
// 3.500000 2.500000 1.500000 1.000000 1.600000"""
```

## Metadata

```scala
val x = fromColumnMajorArray(3, 1, Vector(4.0, 5.1, 6.2).toArray)
// x: org.jblas.DoubleMatrix = [4.000000; 5.100000; 6.200000]
x.show
// res9: String = """4.000000
// 5.100000
// 6.200000"""

val y = fromColumnMajorArray(3, 1, Vector(7.3, 8.4, 9.5).toArray)
// y: org.jblas.DoubleMatrix = [7.300000; 8.400000; 9.500000]
y.show
// res10: String = """7.300000
// 8.400000
// 9.500000"""

x.isEmpty
// res11: Boolean = false

x.isRowVector
// res12: Boolean = false

x.isColumnVector
// res13: Boolean = true

x.isSquare
// res14: Boolean = false

x.isScalar
// res15: Boolean = false

x.rows
// res16: Int = 3

x.columns
// res17: Int = 1

x.length
// res18: Int = 3
```

## Accessing columns, rows, and elements

```scala
x.column(0).show
// res19: String = """4.000000
// 5.100000
// 6.200000"""

x.row(1).show
// res20: String = "5.100000"

x.get(2, 0)
// res21: Double = 6.2

val fiveByFive = fromColumnMajorArray(5, 5, (1 to 25).map(_.toDouble).toArray)
// fiveByFive: org.jblas.DoubleMatrix = [1.000000, 6.000000, 11.000000, 16.000000, 21.000000; 2.000000, 7.000000, 12.000000, 17.000000, 22.000000; 3.000000, 8.000000, 13.000000, 18.000000, 23.000000; 4.000000, 9.000000, 14.000000, 19.000000, 24.000000; 5.000000, 10.000000, 15.000000, 20.000000, 25.000000]

fiveByFive.show
// res22: String = """1.000000 6.000000 11.000000 16.000000 21.000000
// 2.000000 7.000000 12.000000 17.000000 22.000000
// 3.000000 8.000000 13.000000 18.000000 23.000000
// 4.000000 9.000000 14.000000 19.000000 24.000000
// 5.000000 10.000000 15.000000 20.000000 25.000000"""

fiveByFive.slice(1 to 3, 2 to 4).show
// res23: String = """12.000000 17.000000 22.000000
// 13.000000 18.000000 23.000000
// 14.000000 19.000000 24.000000"""

fiveByFive.slice(0.until(5,2), 0.until(5,2)).show
// res24: String = """1.000000 11.000000 21.000000
// 3.000000 13.000000 23.000000
// 5.000000 15.000000 25.000000"""
```

## Negate, Transpose, Power

```scala
x.negate.show
// res25: String = """-4.000000
// -5.100000
// -6.200000"""

x.transpose.show
// res26: String = "4.000000 5.100000 6.200000"

// x.log
// x.log10

x.pow(2d).show
// res27: String = """16.000000
// 26.010000
// 38.440000"""
```

## Mins, Maxs, Ranges, and Sorts

```scala
r.max
// res28: Double = 0.9703456897769025

r.min
// res29: Double = 0.09755717892841309

// r.ceil
// r.floor

r.rowMaxs.show
// res30: String = """0.970346
// 0.794303
// 0.955493"""

r.rowMins.show
// res31: String = """0.444361
// 0.097557
// 0.275987"""

r.columnMaxs.show
// res32: String = "0.955493 0.970346 0.444361"

r.columnMins.show
// res33: String = "0.789029 0.460526 0.097557"

rowRange(r).show
// res34: String = """0.525984
// 0.696746
// 0.679506"""

columnRange(r).show
// res35: String = "0.166465 0.509820 0.346804"

r.sortRows.show
// res36: String = """0.444361 0.952109 0.970346
// 0.097557 0.789029 0.794303
// 0.275987 0.460526 0.955493"""

r.sortColumns.show
// res37: String = """0.789029 0.460526 0.097557
// 0.952109 0.794303 0.275987
// 0.955493 0.970346 0.444361"""

r.sortRows.sortColumns.show
// res38: String = """0.097557 0.460526 0.794303
// 0.275987 0.789029 0.955493
// 0.444361 0.952109 0.970346"""
```

## Statistics

```scala
r.rowMeans.show
// res39: String = """0.788939
// 0.560296
// 0.564002"""

r.columnMeans.show
// res40: String = "0.898877 0.741725 0.272635"

// median(r)

sumsq(r).show
// res41: String = "2.442046 1.784572 0.283143"

std(r).show
// res42: String = "0.077687 0.211428 0.141602"

cov(r).show
// res43: String = """0.000945 -0.008371 0.003241
// -0.008371 0.000921 0.016411
// 0.003241 0.016411 0.000004"""

centerRows(r).show
// res44: String = """0.163170 0.181407 -0.344577
// 0.228732 0.234007 -0.462739
// 0.391491 -0.103476 -0.288015"""

centerColumns(r).show
// res45: String = """0.053232 0.228621 0.171726
// -0.109848 0.052578 -0.175078
// 0.056616 -0.281199 0.003352"""

zscore(r).show
// res46: String = """0.685215 1.081321 1.212737
// -1.413990 0.248681 -1.236410
// 0.728775 -1.330001 0.023673"""
```

## Principal Component Analysis

```scala
val (u, s) = pca(r)
// u: org.jblas.DoubleMatrix = [-0.379672, -0.243112, -0.892606; -0.672520, 0.735083, 0.085849; 0.635268, 0.632890, -0.442589]
// s: org.jblas.DoubleMatrix = [0.019306; 0.017819; 0.003357]

u.show
// res47: String = """-0.379672 -0.243112 -0.892606
// -0.672520 0.735083 0.085849
// 0.635268 0.632890 -0.442589"""

s.show
// res48: String = """0.019306
// 0.017819
// 0.003357"""
```

## Horizontal and vertical concatenation

```scala
(x aside y).show
// res49: String = """4.000000 7.300000
// 5.100000 8.400000
// 6.200000 9.500000"""

(x atop y).show
// res50: String = """4.000000
// 5.100000
// 6.200000
// 7.300000
// 8.400000
// 9.500000"""
```

## Addition and subtraction

```scala
val z = ones(2, 3)
// z: org.jblas.DoubleMatrix = [1.000000, 1.000000, 1.000000; 1.000000, 1.000000, 1.000000]

z.show
// res51: String = """1.000000 1.000000 1.000000
// 1.000000 1.000000 1.000000"""
```

Matrix addition

```scala
import spire.implicits.additiveSemigroupOps

(z + z).show
// res52: String = """2.000000 2.000000 2.000000
// 2.000000 2.000000 2.000000"""
```

Scalar addition (JBLAS method)

```scala
z.addScalar(1.1).show
// res53: String = """2.100000 2.100000 2.100000
// 2.100000 2.100000 2.100000"""
```

Matrix subtraction

```scala
import spire.implicits.additiveGroupOps

(z - z).show
// res54: String = """0.000000 0.000000 0.000000
// 0.000000 0.000000 0.000000"""
```

Scalar subtraction (JBLAS method)

```scala
z.subtractScalar(0.2).show
// res55: String = """0.800000 0.800000 0.800000
// 0.800000 0.800000 0.800000"""
```

## Multiplication and Division

Scalar multiplication

```scala
z.multiplyScalar(3d).show
// res56: String = """3.000000 3.000000 3.000000
// 3.000000 3.000000 3.000000"""
```

Matrix multiplication

```scala
import spire.implicits.multiplicativeSemigroupOps

(z * z.transpose).show
// res57: String = """3.000000 3.000000
// 3.000000 3.000000"""
```

Scalar division (JBLAS method)

```scala
z.divideScalar(100d).show
// res58: String = """0.010000 0.010000 0.010000
// 0.010000 0.010000 0.010000"""
```

## Map element values

```scala
implicit val endo = axle.jblas.endoFunctorDoubleMatrix[Double]
// endo: algebra.Endofunctor[org.jblas.DoubleMatrix, Double] = axle.jblas.package$$anon$1@72599fa9
import axle.syntax.endofunctor.endofunctorOps

val half = ones(3, 3).map(_ / 2d)
// half: org.jblas.DoubleMatrix = [0.500000, 0.500000, 0.500000; 0.500000, 0.500000, 0.500000; 0.500000, 0.500000, 0.500000]

half.show
// res59: String = """0.500000 0.500000 0.500000
// 0.500000 0.500000 0.500000
// 0.500000 0.500000 0.500000"""
```

## Boolean operators

```scala
(r lt half).show
// res60: String = """0.000000 0.000000 1.000000
// 0.000000 0.000000 1.000000
// 0.000000 1.000000 1.000000"""

(r le half).show
// res61: String = """0.000000 0.000000 1.000000
// 0.000000 0.000000 1.000000
// 0.000000 1.000000 1.000000"""

(r gt half).show
// res62: String = """1.000000 1.000000 0.000000
// 1.000000 1.000000 0.000000
// 1.000000 0.000000 0.000000"""

(r ge half).show
// res63: String = """1.000000 1.000000 0.000000
// 1.000000 1.000000 0.000000
// 1.000000 0.000000 0.000000"""

(r eq half).show
// res64: String = """0.000000 0.000000 0.000000
// 0.000000 0.000000 0.000000
// 0.000000 0.000000 0.000000"""

(r ne half).show
// res65: String = """1.000000 1.000000 1.000000
// 1.000000 1.000000 1.000000
// 1.000000 1.000000 1.000000"""

((r lt half) or (r gt half)).show
// res66: String = """1.000000 1.000000 1.000000
// 1.000000 1.000000 1.000000
// 1.000000 1.000000 1.000000"""

((r lt half) and (r gt half)).show
// res67: String = """0.000000 0.000000 0.000000
// 0.000000 0.000000 0.000000
// 0.000000 0.000000 0.000000"""

((r lt half) xor (r gt half)).show
// res68: String = """1.000000 1.000000 1.000000
// 1.000000 1.000000 1.000000
// 1.000000 1.000000 1.000000"""

((r lt half) not).show
// res69: String = """1.000000 1.000000 0.000000
// 1.000000 1.000000 0.000000
// 1.000000 0.000000 0.000000"""
```

## Higher order methods

```scala
(m.map(_ + 1)).show
// res70: String = """2.000000 6.000000 10.000000 14.000000 18.000000
// 3.000000 7.000000 11.000000 15.000000 19.000000
// 4.000000 8.000000 12.000000 16.000000 20.000000
// 5.000000 9.000000 13.000000 17.000000 21.000000"""

(m.map(_ * 10)).show
// res71: String = """10.000000 50.000000 90.000000 130.000000 170.000000
// 20.000000 60.000000 100.000000 140.000000 180.000000
// 30.000000 70.000000 110.000000 150.000000 190.000000
// 40.000000 80.000000 120.000000 160.000000 200.000000"""

// m.foldLeft(zeros(4, 1))(_ + _)

(m.foldLeft(ones(4, 1))(_ mulPointwise _)).show
// res72: String = """9945.000000
// 30240.000000
// 65835.000000
// 122880.000000"""

// m.foldTop(zeros(1, 5))(_ + _)

(m.foldTop(ones(1, 5))(_ mulPointwise _)).show
// res73: String = "24.000000 1680.000000 11880.000000 43680.000000 116280.000000"
```

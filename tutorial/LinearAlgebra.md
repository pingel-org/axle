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
// r: org.jblas.DoubleMatrix = [0.679785, 0.797263, 0.391990; 0.171334, 0.850370, 0.175463; 0.515816, 0.755148, 0.915551]

r.show
// res6: String = """0.679785 0.797263 0.391990
// 0.171334 0.850370 0.175463
// 0.515816 0.755148 0.915551"""
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
// res28: Double = 0.915551357710952

r.min
// res29: Double = 0.17133407806780654

// r.ceil
// r.floor

r.rowMaxs.show
// res30: String = """0.797263
// 0.850370
// 0.915551"""

r.rowMins.show
// res31: String = """0.391990
// 0.171334
// 0.515816"""

r.columnMaxs.show
// res32: String = "0.679785 0.850370 0.915551"

r.columnMins.show
// res33: String = "0.171334 0.755148 0.175463"

rowRange(r).show
// res34: String = """0.405273
// 0.679036
// 0.399735"""

columnRange(r).show
// res35: String = "0.508451 0.095222 0.740088"

r.sortRows.show
// res36: String = """0.391990 0.679785 0.797263
// 0.171334 0.175463 0.850370
// 0.515816 0.755148 0.915551"""

r.sortColumns.show
// res37: String = """0.171334 0.755148 0.175463
// 0.515816 0.797263 0.391990
// 0.679785 0.850370 0.915551"""

r.sortRows.sortColumns.show
// res38: String = """0.171334 0.175463 0.797263
// 0.391990 0.679785 0.850370
// 0.515816 0.755148 0.915551"""
```

## Statistics

```scala
r.rowMeans.show
// res39: String = """0.623013
// 0.399056
// 0.728838"""

r.columnMeans.show
// res40: String = "0.455645 0.800927 0.494335"

// median(r)

sumsq(r).show
// res41: String = "0.757529 1.929006 1.022678"

std(r).show
// res42: String = "0.211890 0.038961 0.310686"

cov(r).show
// res43: String = """0.016746 0.000347 -0.002053
// 0.000347 0.000815 0.004866
// -0.002053 0.004866 0.059141"""

centerRows(r).show
// res44: String = """0.056772 0.174251 -0.231023
// -0.227722 0.451314 -0.223593
// -0.213022 0.026309 0.186713"""

centerColumns(r).show
// res45: String = """0.224140 -0.003664 -0.102345
// -0.284311 0.049443 -0.318872
// 0.060171 -0.045779 0.421217"""

zscore(r).show
// res46: String = """1.057813 -0.094032 -0.329415
// -1.341786 1.269051 -1.026348
// 0.283973 -1.175018 1.355764"""
```

## Principal Component Analysis

```scala
val (u, s) = pca(r)
// u: org.jblas.DoubleMatrix = [0.046978, 0.998395, 0.031645; -0.082072, 0.035431, -0.995996; -0.995519, 0.044193, 0.083604]
// s: org.jblas.DoubleMatrix = [0.059639; 0.016668; 0.000395]

u.show
// res47: String = """0.046978 0.998395 0.031645
// -0.082072 0.035431 -0.995996
// -0.995519 0.044193 0.083604"""

s.show
// res48: String = """0.059639
// 0.016668
// 0.000395"""
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
// endo: algebra.Endofunctor[org.jblas.DoubleMatrix, Double] = axle.jblas.package$$anon$1@2c502dc1
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
// 1.000000 0.000000 1.000000
// 0.000000 0.000000 0.000000"""

(r le half).show
// res61: String = """0.000000 0.000000 1.000000
// 1.000000 0.000000 1.000000
// 0.000000 0.000000 0.000000"""

(r gt half).show
// res62: String = """1.000000 1.000000 0.000000
// 0.000000 1.000000 0.000000
// 1.000000 1.000000 1.000000"""

(r ge half).show
// res63: String = """1.000000 1.000000 0.000000
// 0.000000 1.000000 0.000000
// 1.000000 1.000000 1.000000"""

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
// 0.000000 1.000000 0.000000
// 1.000000 1.000000 1.000000"""
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

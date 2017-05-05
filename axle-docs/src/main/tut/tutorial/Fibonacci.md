---
layout: page
title: Fibonacci
permalink: /tutorial/fibonacci/
---


```tut:silent
import axle._
```

# Linear using `foldLeft`:

```tut:book
fib(10)
```

# Recursive

```tut:book
recfib(10)
```

Some alternatives that are not in Axle include

# Recursive with memoization

```tut:book
val memo = collection.mutable.Map(0 -> 0L, 1 -> 1L)

def fibonacciRecursivelyWithMemo(n: Int): Long = {
  if (memo.contains(n)) {
    memo(n)
  } else {
    val result = fibonacciRecursivelyWithMemo(n - 2) + fibonacciRecursivelyWithMemo(n - 1)
    memo += n -> result
    result
  }
}

fibonacciRecursivelyWithMemo(10)
```

# Recursive squaring

A less well-known approach to obtain sub-linear time.

(TODO: use Axle 2x2 matrices rather than this case class.)

```tut:book

import org.jblas.DoubleMatrix
import axle._
import axle.jblas._
import axle.syntax.linearalgebra.matrixOps
import spire.implicits.DoubleAlgebra
import spire.implicits.multiplicativeSemigroupOps

implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
import laJblasDouble._

val fibMatrix1 = fromColumnMajorArray(2, 2, List(1d, 1d, 1d, 0d).toArray)

val matrixMemo = collection.mutable.Map(1 -> fibMatrix1)

def nthFibMatrix(n: Int): DoubleMatrix = {
  if (matrixMemo.contains(n)) {
    matrixMemo(n)
  } else if (n % 2 == 0) {
    val result = nthFibMatrix(n / 2) * nthFibMatrix(n / 2)
    matrixMemo += n -> result
    result
  } else {
    val result = nthFibMatrix((n - 1) / 2) * nthFibMatrix((n - 1) / 2) * fibMatrix1
    matrixMemo += n -> result
    result
  }
}

def fibonacciFast(n: Int): Long = n match {
  case 0 => 0L
  case _ => nthFibMatrix(n).get(0, 1).toLong
}

fibonacciFast(78)
```

Note: Beyond 78 inaccuracies creep in due to the limitations of the `Double` number type.

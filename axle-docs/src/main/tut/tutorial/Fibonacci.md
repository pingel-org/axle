---
layout: page
title: Fibonacci
permalink: /tutorial/fibonacci/
---


```tut:silent
import axle._
```

## Linear using `foldLeft`

```tut:book
fib(10)
```

## Recursive

```tut:book
recfib(10)
```

Some alternatives that are not in Axle include

## Recursive with memoization

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

## Recursive squaring

A less well-known approach to obtain sub-linear time.

Imports

```tut:silent
import axle._
import axle.jblas._
import axle.syntax.linearalgebra.matrixOps
import spire.implicits.DoubleAlgebra
import spire.algebra.MultiplicativeSemigroup
import axle.algebra._
```

Define `nthFibMatrix` using recursive squaring:

```tut:book
def nthFibMatrix[M, V](
  n: Int,
  memo: collection.mutable.Map[Int, M], m1: M)(
  implicit la: LinearAlgebra[M, Int, Int, V],
  ms: MultiplicativeSemigroup[M]): M = {

  import spire.implicits.multiplicativeSemigroupOps

  if (memo.contains(n)) {
    memo(n)
  } else if (n % 2 == 0) {
    val half = nthFibMatrix(n / 2, memo, m1)
    val result = half * half
    memo += n -> result
    result
  } else {
    val half = nthFibMatrix((n - 1) / 2, memo, m1)
    val result = half * half * m1
    memo += n -> result
    result
  }
}
```

Wrapper utilizing `nthFibMatrix` using jblas matrices and `axle.jblas`:

```tut:book
def fibonacciFast(n: Int): Long = {

  import org.jblas.DoubleMatrix
  implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
  import laJblasDouble._

  val m1 = fromColumnMajorArray(2, 2, List(1d, 1d, 1d, 0d).toArray)

  val memo = collection.mutable.Map(1 -> m1)

  n match {
    case 0 => 0L
    case _ => nthFibMatrix(n, memo, m1).get(0, 1).toLong
  }
}
```

Demo:

```tut:book
fibonacciFast(78)
```

Note: Beyond 78 inaccuracies creep in due to the limitations of the `Double` number type.

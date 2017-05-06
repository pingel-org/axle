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

Define general-purpose sub-linear strategy for exponentiation called "recursive squaring"

```tut:book
def exponentiateByRecursiveSquaring[B](base: B, pow: Int)(implicit multB: MultiplicativeSemigroup[B]): B = {

  import spire.implicits.multiplicativeSemigroupOps

  if (pow == 1) {
    base
  } else if (pow % 2 == 0) {
    val half = exponentiateByRecursiveSquaring(base, pow / 2)
    half * half
  } else {
    val half = exponentiateByRecursiveSquaring(base, (pow - 1) / 2)
    half * half * base
  }
}
```

Define `nthFibMatrix` using `exponentiateByRecursiveSquaring` with the special base matrix:

```tut:book
def fibonacciFast(n: Int): Long = {

  import org.jblas.DoubleMatrix
  implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
  import laJblasDouble._

  val base = fromColumnMajorArray(2, 2, List(1d, 1d, 1d, 0d).toArray)

  n match {
    case 0 => 0L
    case _ => exponentiateByRecursiveSquaring(base, n).get(0, 1).toLong
  }
}
```

Demo:

```tut:book
fibonacciFast(78)
```

Note: Beyond 78 inaccuracies creep in due to the limitations of the `Double` number type.

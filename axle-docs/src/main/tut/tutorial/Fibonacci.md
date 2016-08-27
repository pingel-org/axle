---
layout: page
title: Fibonacci
permalink: /tutorial/fibonacci/
---


```tut:silent
import axle._
```

Linear implemntation using `foldLeft`:

```tut:book
fib(10)
```

Recursive implementation:

```tut:book
recfib(10)
```

Some alternatives that are not in Axle include

Recursive with memoization:

```tut:book
val memo = collection.mutable.Map(0 -> 0L, 1 -> 1L)

def fibonacciRecursivelyWithMemo(n: Int): Long =
  if (memo.contains(n))
    memo(n)
  else {
    val result = fibonacciRecursivelyWithMemo(n - 2) + fibonacciRecursivelyWithMemo(n - 1)
    memo += n -> result
    result
  }

fibonacciRecursivelyWithMemo(10)
```

Or the seldom used sub-linear approach using matrices and "recursive squaring"

(TODO: use Axle 2x2 matrices rather than this case class.)

```tut:book
case class FibMatrix(_00: Long, _01: Long, _10: Long, _11: Long) {
  def *(right: FibMatrix) = FibMatrix(
    _00 * right._00 + _01 * right._10, _00 * right._01 + _01 * right._11,
    _10 * right._00 + _11 * right._10, _10 * right._01 + _11 * right._11
  )
}

val fibMatrix1 = FibMatrix(1, 1, 1, 0)

val matrixMemo = collection.mutable.Map(1 -> fibMatrix1)

def nthFibMatrix(n: Int): FibMatrix =
  if (matrixMemo.contains(n))
    matrixMemo(n)
  else if (n % 2 == 0) {
    val result = nthFibMatrix(n / 2) * nthFibMatrix(n / 2)
    matrixMemo += n -> result
    result
  } else {
    val result = nthFibMatrix((n - 1) / 2) * nthFibMatrix((n - 1) / 2) * fibMatrix1
    matrixMemo += n -> result
    result
  }

def fibonacciFast(n: Int) = n match {
  case 0 => 0
  case _ => nthFibMatrix(n)._01
}

fibonacciFast(100)
```

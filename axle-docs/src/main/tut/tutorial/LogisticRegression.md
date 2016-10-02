---
layout: page
title: Logistic Regression
permalink: /tutorial/logistic_regression/
---

`axle.ml.LogisticRegression` makes use of `axle.algebra.LinearAlgebra`.

See the wikipedia page on [Logistic Regression](https://en.wikipedia.org/wiki/Logistic_regression)

Predict Test Pass Probability as a Function of Hours Studied
------------------------------------------------------------

```tut:book
case class Student(hoursStudied: Double, testPassed: Boolean)

val data = List(
  Student(0.50, false),
  Student(0.75, false),
  Student(1.00, false),
  Student(1.25, false),
  Student(1.50, false),
  Student(1.75, false),
  Student(1.75, true),
  Student(2.00, false),
  Student(2.25, true),
  Student(2.50, false),
  Student(2.75, true),
  Student(3.00, false),
  Student(3.25, true),
  Student(3.50, false),
  Student(4.00, true),
  Student(4.25, true),
  Student(4.50, true),
  Student(4.75, true),
  Student(5.00, true),
  Student(5.50, true)
)
```

Create a test pass probability function using logistic regression.

```tut:book
import axle.jblas._
import spire.implicits.DoubleAlgebra
implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]

import axle.ml.LogisticRegression

val featureExtractor = (s: Student) => (s.hoursStudied :: Nil)

val objectiveExtractor = (s: Student) => s.testPassed

val pTestPass = LogisticRegression(
  data,
  1,
  featureExtractor,
  objectiveExtractor,
  0.1,
  10)
```

Use the estimator

```tut:book
testPassProbability(2d :: Nil)
```

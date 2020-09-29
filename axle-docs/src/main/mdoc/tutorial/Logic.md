---
layout: page
title: Logic
permalink: /tutorial/logic/
---

## Conjunctive Normal Form Converter

Imports

```scala mdoc:silent
import cats.implicits._
import axle.logic.FirstOrderPredicateLogic._
```

Example CNF conversion

```scala mdoc
import axle.logic.example.SamplePredicates._

val z = Symbol("z")

val s = ∃(z ∈ Z, (A(z) ∧ G(z)) ⇔ (B(z) ∨ H(z)))

val (cnf, skolemMap) = conjunctiveNormalForm(s)
```

```scala mdoc
cnf.show

skolemMap
```

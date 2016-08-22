---
layout: page
title: Logic
permalink: /tutorial/logic/
---

Conjunctive Normal Form Converter
---------------------------------

Imports

```tut:silent
import axle.string
import axle.logic._
import FirstOrderPredicateLogic._
```

Example CNF conversion

```tut:book
import SamplePredicates._

val s = ∃('z ∈ Z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))

val (cnf, skolemMap) = conjunctiveNormalForm(s)
```

```tut:book
string(cnf)

skolemMap
```

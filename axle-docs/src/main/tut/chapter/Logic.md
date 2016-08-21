---
layout: page
title: Logic
permalink: /chapter/logic/
---

Conjunctive Normal Form Converter
---------------------------------

Imports

```tut:book
import axle.string
import axle.logic._
import FirstOrderPredicateLogic._
import SamplePredicates._
```

Example CNF conversion

```tut:book
val s = ∃('z ∈ Z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))

val (cnf, skolemMap) = conjunctiveNormalForm(s)
```

```tut:book
string(cnf)

skolemMap
```


Logic
=====

Conjunctive Normal Form Converter
---------------------------------

Imports

```tut
import axle.string
import axle.logic._
import FirstOrderPredicateLogic._
import SamplePredicates._
```

Example CNF conversion

```tut
val s = ∃('z ∈ Z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))

val (cnf, skolemMap) = conjunctiveNormalForm(s)
```

```tut
string(cnf)

skolemMap
```

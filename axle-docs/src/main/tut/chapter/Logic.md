
Logic
=====

Conjunctive Normal Form Converter
---------------------------------

Imports

```book
import axle.string
import axle.logic._
import FirstOrderPredicateLogic._
import SamplePredicates._
```

Example CNF conversion

```book
val s = ∃('z ∈ Z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))

val (cnf, skolemMap) = conjunctiveNormalForm(s)
```

```book
string(cnf)

skolemMap
```

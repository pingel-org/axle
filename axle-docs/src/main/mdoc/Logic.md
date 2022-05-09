# Logic

WARNING: This stuff is quite old and needs an overhaul

## Conjunctive Normal Form Converter

Imports

```scala mdoc:silent:reset
import cats.implicits._
import axle.logic.FirstOrderPredicateLogic._
```

Example CNF conversion

```scala mdoc:silent
import axle.logic.example.SamplePredicates._

val z = Symbol("z")

val s = ∃(z ∈ Z, (A(z) ∧ G(z)) ⇔ (B(z) ∨ H(z)))

val (cnf, skolemMap) = conjunctiveNormalForm(s)
```

```scala mdoc
cnf.show

skolemMap
```

## Future Work

Redo all of this in terms of Abstract Algebra

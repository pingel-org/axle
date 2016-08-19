
Logic
=====

Conjunctive Normal Form Converter
---------------------------------

Imports

```scala
scala> import axle.string
import axle.string

scala> import axle.logic._
import axle.logic._

scala> import FirstOrderPredicateLogic._
import FirstOrderPredicateLogic._

scala> import SamplePredicates._
import SamplePredicates._
```

Example CNF conversion

```scala
scala> val s = ∃('z ∈ Z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))
s: axle.logic.FirstOrderPredicateLogic.∃ = ∃(ElementOf('z,Set(7, 8, 9)),Iff(And(<function1>,<function1>),Or(<function1>,<function1>)))

scala> val (cnf, skolemMap) = conjunctiveNormalForm(s)
cnf: axle.logic.FirstOrderPredicateLogic.Statement = And(Or(¬(<function1>),Or(¬(<function1>),Or(<function1>,<function1>))),And(Or(And(¬(<function1>),¬(<function1>)),<function1>),Or(And(¬(<function1>),¬(<function1>)),<function1>)))
skolemMap: Map[Symbol,Set[Symbol]] = Map('sk2 -> Set(), 'sk6 -> Set(), 'sk7 -> Set(), 'sk3 -> Set(), 'sk4 -> Set(), 'sk1 -> Set(), 'sk0 -> Set(), 'sk5 -> Set())
```

```scala
scala> string(cnf)
res0: String = ((¬A('sk0) ∨ (¬G('sk1) ∨ (B('sk2) ∨ H('sk3)))) ∧ (((¬B('sk4) ∧ ¬H('sk5)) ∨ A('sk6)) ∧ ((¬B('sk4) ∧ ¬H('sk5)) ∨ G('sk7))))

scala> skolemMap
res1: Map[Symbol,Set[Symbol]] = Map('sk2 -> Set(), 'sk6 -> Set(), 'sk7 -> Set(), 'sk3 -> Set(), 'sk4 -> Set(), 'sk1 -> Set(), 'sk0 -> Set(), 'sk5 -> Set())
```

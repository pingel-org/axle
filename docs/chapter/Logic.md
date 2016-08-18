
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

scala> import FOPL._
<console>:16: error: not found: value FOPL
       import FOPL._
              ^

scala> import SamplePredicates._
import SamplePredicates._
```

Example CNF conversion

```scala
scala> val s = ∃('z ∈ Z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))
<console>:19: error: not found: value ∃
       val s = ∃('z ∈ Z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))
               ^
<console>:19: error: value ∈ is not a member of Symbol
       val s = ∃('z ∈ Z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))
                    ^
scala> val (cnf, skolemMap) = conjunctiveNormalForm(s)
<console>:19: error: not found: value conjunctiveNormalForm
       val (cnf, skolemMap) = conjunctiveNormalForm(s)
                              ^
<console>:19: error: not found: value s
       val (cnf, skolemMap) = conjunctiveNormalForm(s)
                                                    ^
```

```scala
scala> string(cnf)
<console>:20: error: not found: value cnf
       string(cnf)
              ^
scala> skolemMap
<console>:20: error: not found: value skolemMap
       skolemMap
       ^
```

---
layout: page
title: Logic
permalink: /tutorial/logic/
---

## Conjunctive Normal Form Converter

Imports

```scala
import cats.implicits._
import axle.logic.FirstOrderPredicateLogic._
```

Example CNF conversion

```scala
import axle.logic.example.SamplePredicates._

val z = Symbol("z")
// z: Symbol = 'z

val s = ∃(z ∈ Z, (A(z) ∧ G(z)) ⇔ (B(z) ∨ H(z)))
// s: ∃ = ∃(
//   symbolSet = ElementOf(symbol = 'z, set = Set(7, 8, 9)),
//   statement = Iff(
//     left = And(left = A(symbols = List('z)), right = G(symbols = List('z))),
//     right = Or(left = B(symbols = List('z)), right = H(symbols = List('z)))
//   )
// )

val (cnf, skolemMap) = conjunctiveNormalForm(s)
// cnf: Statement = And(
//   left = Or(
//     left = ¬(statement = <function1>),
//     right = Or(
//       left = ¬(statement = <function1>),
//       right = Or(left = <function1>, right = <function1>)
//     )
//   ),
//   right = And(
//     left = Or(
//       left = And(
//         left = ¬(statement = <function1>),
//         right = ¬(statement = <function1>)
//       ),
//       right = <function1>
//     ),
//     right = Or(
//       left = And(
//         left = ¬(statement = <function1>),
//         right = ¬(statement = <function1>)
//       ),
//       right = <function1>
//     )
//   )
// )
// skolemMap: Map[Symbol, Set[Symbol]] = HashMap(
//   'sk2 -> Set(),
//   'sk6 -> Set(),
//   'sk7 -> Set(),
//   'sk3 -> Set(),
//   'sk4 -> Set(),
//   'sk1 -> Set(),
//   'sk0 -> Set(),
//   'sk5 -> Set()
// )
```

```scala
cnf.show
// res0: String = "((¬A(Symbol(sk0)) ∨ (¬G(Symbol(sk1)) ∨ (B(Symbol(sk2)) ∨ H(Symbol(sk3))))) ∧ (((¬B(Symbol(sk4)) ∧ ¬H(Symbol(sk5))) ∨ A(Symbol(sk6))) ∧ ((¬B(Symbol(sk4)) ∧ ¬H(Symbol(sk5))) ∨ G(Symbol(sk7)))))"

skolemMap
// res1: Map[Symbol, Set[Symbol]] = HashMap(
//   'sk2 -> Set(),
//   'sk6 -> Set(),
//   'sk7 -> Set(),
//   'sk3 -> Set(),
//   'sk4 -> Set(),
//   'sk1 -> Set(),
//   'sk0 -> Set(),
//   'sk5 -> Set()
// )
```

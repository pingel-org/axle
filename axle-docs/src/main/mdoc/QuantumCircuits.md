# Quantum Circuits

## QBit

```scala mdoc:silent
package axle.quantumcircuit

import cats.implicits._
import spire.math._

import axle.syntax.kolmogorov._
import axle.algebra.RegionEq

val sqrtHalf = Complex(Real(1) / sqrt(Real(2)), Real(0))

val qEven = QBit[Real](sqrtHalf, sqrtHalf)

val distribution = qEven.cpt
```

```scala mdoc
distribution.P(RegionEq(CBit0))

distribution.P(RegionEq(CBit1))
```

## Dirac Vector Notation

```scala mdoc:silent:reset
import axle.quantumcircuit._
```

```scala mdoc
|("00").>().unindex

Vector[Binary](1, 0) ⊗ Vector[Binary](1, 0)
```

## CNOT

```scala mdoc:silent:reset
import axle.quantumcircuit.QBit._
import spire.algebra.Field
implicit val fieldReal: Field[Real] = new spire.math.RealAlgebra()
val QBit0 = constant0[Real]
val QBit1 = constant1[Real]
```

```scala mdoc
QBit2.cnot(QBit2(QBit0.unindex ⊗ QBit0.unindex)).unindex

QBit2.cnot(QBit2(QBit0.unindex ⊗ QBit1.unindex)).unindex

QBit2.cnot(QBit2(QBit1.unindex ⊗ QBit0.unindex)).unindex

QBit2.cnot(QBit2(QBit1.unindex ⊗ QBit1.unindex)).unindex
```

## Future Work

### Soon

* `QBit2.factor`
* Fix and enable `DeutschOracleSpec`
* QBit CCNot

### Later

* Shor's algorithm
* Property test reversibility (& own inverse)
* Typeclass for "negate" (etc), Binary, CBit
* Typeclass for unindex
* Deutsch-Jozsa algorithm (D.O. for n-bits) (Oracle separation between EQP and P)
* Simon's periodicity problem (oracle separation between BQP and BPP)
* Grover's algorithm
* Quantum cryptographic key exchange


Algebra
=======

The { spire.link } project is a dependency of Axle.
`spire.algebra` defines typeclasses for Monoid, Group, Ring, Field, VectorSpace, etc, and
witnesses for many common numeric types as well as those defined in `spire.math`

The `axle.algebra` package defines several categories of typeclasses:

* higher-kinded: Functor, Finite, Indexed, Aggregatable
* mathematical: LinearAlgebra, LengthSpace<
* visualization: Tics, Plottable

Axioms are defined in the
<a href="https://github.com/adampingel/axle/tree/master/axle-core/src/main/scala/axle/algebra/laws">axle.algebra.laws</a> package
as <a href="http://scalacheck.org/">ScalaCheck</a> properties.

They are organized with <a href="https://github.com/typelevel/discipline">Discipline</a>.

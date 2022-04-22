---
layout: page
title: Algebra
permalink: /tutorial/algebra/
---

The [spire](http://github.com/non/spire) project is a dependency of Axle.
`spire.algebra` defines typeclasses for Monoid, Group, Ring, Field, VectorSpace, etc, and
witnesses for many common numeric types as well as those defined in `spire.math`

The `axle.algebra` package defines several categories of typeclasses:

* higher-kinded: Functor, Finite, Indexed, Aggregatable
* mathematical: LinearAlgebra, LengthSpace
* visualization: Tics, Plottable

Axioms are defined in the
[axle.algebra.laws](https://github.com/axlelang/axle/tree/master/axle-core/src/main/scala/axle/algebra/laws) package
as [ScalaCheck](http://scalacheck.org/) properties.

They are organized with [Discipline](https://github.com/typelevel/discipline).

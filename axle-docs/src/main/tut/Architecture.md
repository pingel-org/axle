---
layout: page
title: Architecture
in_header: true
permalink: /architecture/
---

Axle generally strives to follow the patterns established
by the [Typelevel](http://typelevel.org/) projects.

The functions are side-effect free.

The typeclass patterns are drawn from two traditions:
1. [Typeclassopedia](https://wiki.haskell.org/Typeclassopedia)
2. Abstract Algebra

The algorithms are increasingly defined only in terms of these typeclasses.
Concrete runtime implementations will require witnesses that map non-Axle data structures
onto the typeclass methods and laws.
Many such witnesses are provided by Axle for native Scala collections.

Witnesses are also defined for other common jars from the Java and Scala ecosystems.
Read more about these ["spokes"](/spokes/).

The code will soon compile warning-free.

The biggest outstanding issue is how to deal with Spark's context bounds
on functions like `map`.
In this case, it means that either `Functor`'s map must also take have the context bound,
even though in non-Spark cases it would go unused, or place the context bound in the
witness instantiation site.

Axle has opted for the latter until another solution can be found.
The biggest downside is the introduction of additional type parameters in typeclasses
like `Functor`.

Please get in touch if you'd like to discuss alternative approaches.

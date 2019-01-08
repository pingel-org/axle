---
layout: page
title: Architecture
in_header: true
permalink: /architecture/
---

Axle generally strives to follow the patterns established
by the [Typelevel](http://typelevel.org/) projects.

With few exceptions, the functions are side-effect free.

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

## Remaining Design Issues

See the [Road Map](/road_map/) for more details on the timing of upcoming changes.

Please get in touch if you'd like to discuss these or other questions.

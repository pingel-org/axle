---
layout: page
title: Ackermann
permalink: /tutorial/ackermann/
---

See the Wikipedia page on the [Ackermann function](http://en.wikipedia.org/wiki/Ackermann_function)

```scala
import axle.math._
```

The computational complexity is enormous.
Only for very small `m` and `n` can the function complete:

```scala
ackermann(1, 1)
// res0: Long = 3L

ackermann(3, 3)
// res1: Long = 61L
```

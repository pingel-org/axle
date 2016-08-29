---
layout: page
title: Ackermann
permalink: /tutorial/ackermann/
---

See the Wikipedia page on the [Ackermann function](http://en.wikipedia.org/wiki/Ackermann_function)

```tut:silent
import axle._
```

The computational complexity is enormous.
Only for very small `m` and `n` can the function complete:

```tut:book
ackermann(1, 1)

ackermann(3, 3)
```

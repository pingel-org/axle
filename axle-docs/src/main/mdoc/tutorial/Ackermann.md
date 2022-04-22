# Ackermann

See the Wikipedia page on the [Ackermann function](http://en.wikipedia.org/wiki/Ackermann_function)

```scala mdoc:silent
import axle.math._
```

The computational complexity is enormous.
Only for very small `m` and `n` can the function complete:

```scala mdoc
ackermann(1, 1)

ackermann(3, 3)
```

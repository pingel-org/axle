
Category Theory
===============

Introduction
------------

A very simple example with Objects, Arrows, and a Category.

```tut
import axle.category.awodey.CategoryTheory._

val ints = ⋅(Set(1, 2))
val strings = ⋅(Set("A", "B"))

println("ints ≡ strings: " + (ints ≡ strings))
println("ints ≡ ints   : " + (ints ≡ ints))

val fii = →(ints, ints) /* TODO */
val fis = →(ints, strings) /* TODO */
val fsi = →(strings, ints) /* TODO */
val fss = →(strings, strings) /* TODO */

println("fii ≡ fii: " + (fii ≡ fii))
println("fii ≡ fis: " + (fii ≡ fis))

// val setsFin1 = Category(Set(ints, strings), Set(fii, fis, fsi, fss))
val setsFin1 = Category(Set(ints), Set(fii))
```

See the wikipedia page on [Category Theory](http://en.wikipedia.org/wiki/Category_theory) for some more background.

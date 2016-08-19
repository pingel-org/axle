
Category Theory
===============

Introduction
------------

A very simple example with Objects, Arrows, and a Category.

```scala
scala> import axle.category.awodey.CategoryTheory._
<console>:12: error: object category is not a member of package axle
       import axle.category.awodey.CategoryTheory._
                   ^
scala> val ints = ⋅(Set(1, 2))
<console>:12: error: not found: value ⋅
       val ints = ⋅(Set(1, 2))
                  ^
scala> val strings = ⋅(Set("A", "B"))
<console>:12: error: not found: value ⋅
       val strings = ⋅(Set("A", "B"))
                     ^
scala> println("ints ≡ strings: " + (ints ≡ strings))
<console>:13: error: not found: value ints
       println("ints ≡ strings: " + (ints ≡ strings))
                                     ^
<console>:13: error: not found: value strings
       println("ints ≡ strings: " + (ints ≡ strings))
                                            ^
scala> println("ints ≡ ints   : " + (ints ≡ ints))
<console>:13: error: not found: value ints
       println("ints ≡ ints   : " + (ints ≡ ints))
                                     ^
<console>:13: error: not found: value ints
       println("ints ≡ ints   : " + (ints ≡ ints))
                                            ^
scala> val fii = →(ints, ints) /* TODO */
<console>:12: error: not found: value →
       val fii = →(ints, ints) /* TODO */
                 ^
<console>:12: error: not found: value ints
       val fii = →(ints, ints) /* TODO */
                   ^
<console>:12: error: not found: value ints
       val fii = →(ints, ints) /* TODO */
                         ^
scala> val fis = →(ints, strings) /* TODO */
<console>:12: error: not found: value →
       val fis = →(ints, strings) /* TODO */
                 ^
<console>:12: error: not found: value ints
       val fis = →(ints, strings) /* TODO */
                   ^
<console>:12: error: not found: value strings
       val fis = →(ints, strings) /* TODO */
                         ^
scala> val fsi = →(strings, ints) /* TODO */
<console>:12: error: not found: value →
       val fsi = →(strings, ints) /* TODO */
                 ^
<console>:12: error: not found: value strings
       val fsi = →(strings, ints) /* TODO */
                   ^
<console>:12: error: not found: value ints
       val fsi = →(strings, ints) /* TODO */
                            ^
scala> val fss = →(strings, strings) /* TODO */
<console>:12: error: not found: value →
       val fss = →(strings, strings) /* TODO */
                 ^
<console>:12: error: not found: value strings
       val fss = →(strings, strings) /* TODO */
                   ^
<console>:12: error: not found: value strings
       val fss = →(strings, strings) /* TODO */
                            ^
scala> println("fii ≡ fii: " + (fii ≡ fii))
<console>:13: error: not found: value fii
       println("fii ≡ fii: " + (fii ≡ fii))
                                ^
<console>:13: error: not found: value fii
       println("fii ≡ fii: " + (fii ≡ fii))
                                      ^
scala> println("fii ≡ fis: " + (fii ≡ fis))
<console>:13: error: not found: value fii
       println("fii ≡ fis: " + (fii ≡ fis))
                                ^
<console>:13: error: not found: value fis
       println("fii ≡ fis: " + (fii ≡ fis))
                                      ^
scala> // val setsFin1 = Category(Set(ints, strings), Set(fii, fis, fsi, fss))
     | val setsFin1 = Category(Set(ints), Set(fii))
<console>:13: error: not found: value Category
       val setsFin1 = Category(Set(ints), Set(fii))
                      ^
<console>:13: error: not found: value ints
       val setsFin1 = Category(Set(ints), Set(fii))
                                   ^
<console>:13: error: not found: value fii
       val setsFin1 = Category(Set(ints), Set(fii))
                                              ^
```

See the wikipedia page on [Category Theory](http://en.wikipedia.org/wiki/Category_theory) for some more background.


Type Classes
============

The examples in
["Learn You a Haskell for Great Good"](http://learnyouahaskell.com/)
should be expressible with a syntax as similar to Haskell as possible --
but in Scala.
Typeclasses like Functor, Applicative Functor, Monoid, and Monad are modelled explicitely.
(This is similar to the goal of the [Scalaz](https://github.com/scalaz/scalaz) project.
One exception is that little syntactic details like the order of arguments to "fmap"
are the same as in Haskell.)

Functor
-------

Includes:

```scala
import org.pingel.axle.Axle.Functor._
import org.pingel.axle.Axle.Functor2._
import org.pingel.axle.Axle.VariousFunctions._
```

REPL session to demo Functor:

```scala
scala> fmap(replicate[Int](3) _, List(1, 2, 3))
res14: List[List[Int]] = List(List(1, 1, 1), List(2, 2, 2), List(3, 3, 3))

scala> fmap(replicate[Int](3) _, Some(4).asInstanceOf[Option[Int]])
res15: Option[List[Int]] = Some(List(4, 4, 4))

scala> fmap2(replicate[String](3) _, Right("blah").asInstanceOf[Either[Nothing, String]])
res16: Either[Nothing,List[String]] = Right(List(blah, blah, blah))

scala> fmap2(replicate[String](3) _, Left("foo").asInstanceOf[Either[String, Nothing]])
res17: Either[String,List[String]] = Left(foo)

scala> fmap2({ (_: Int) * 3 }, { (_: Int) + 100 }) apply (1)
res18: Int = 303
```

Obviously this is not convenient to have to specify the arity.
I need to figure out how to supply a single fmap that works on various
higher kinds.


Applicative Functor
-------------------

```scala
TODO
```

Monoid
------

```scala
TODO
```

Monad
-----

```scala
TODO
```

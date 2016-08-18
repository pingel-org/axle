
Angluin Learner
===============

Models <a href="https://en.wikipedia.org/wiki/Dana_Angluin">Dana Angluin's</a> Language Learner.

Example
-------

Imports

```scala
import axle._
import axle.lx._
import Angluin._
import spire.implicits._
```

Setup

```scala
val mHi = Symbol("hi")
val mIm = Symbol("I'm")
val mYour = Symbol("your")
val mMother = Symbol("Mother")
val mShut = Symbol("shut")
val mUp = Symbol("up")

val Σ = Alphabet(Set(mHi, mIm, mYour, mMother, mShut, mUp))

val s1 = mHi :: mIm :: mYour :: mMother :: Nil
val s2 = mShut :: mUp :: Nil
val ℒ = Language(s1 :: s2 :: Nil)

val T = Text(s1 :: ♯ :: ♯ :: s2 :: ♯ :: s2 :: s2 :: Nil)

val ɸ = MemorizingLearner()
```

Usage

```scala
scala> ɸ.guesses(T)
res4: Iterator[axle.lx.Angluin.Grammar] = non-empty iterator

scala>   .find(_.ℒ === ℒ)
<console>:2: error: illegal start of definition
  .find(_.ℒ === ℒ)
  ^

scala>   .map(finalGuess => "well done, ɸ")
<console>:2: error: illegal start of definition
  .map(finalGuess => "well done, ɸ")
  ^

scala>   .getOrElse("ɸ never made a correct guess")
<console>:2: error: illegal start of definition
  .getOrElse("ɸ never made a correct guess")
  ^

scala> T
res5: axle.lx.Angluin.Text = Text(List(List(Symbol(hi), Symbol(I'm), Symbol(your), Symbol(Mother)), List(), List(), List(Symbol(shut), Symbol(up)), List(), List(Symbol(shut), Symbol(up)), List(Symbol(shut), Symbol(up))))

scala> ℒ
res6: axle.lx.Angluin.Language = Language(List(List(Symbol(hi), Symbol(I'm), Symbol(your), Symbol(Mother)), List(Symbol(shut), Symbol(up))))

scala> T.isFor(ℒ)
res7: Boolean = false
```

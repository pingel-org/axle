
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
scala> ɸ.guesses(T).
     |   find(_.ℒ === ℒ).
     |   map(finalGuess => "well done, ɸ").
     |   getOrElse("ɸ never made a correct guess")
res4: String = well done, ɸ

scala> T
res5: axle.lx.Angluin.Text = Text(List(List(Symbol(hi), Symbol(I'm), Symbol(your), Symbol(Mother)), List(), List(), List(Symbol(shut), Symbol(up)), List(), List(Symbol(shut), Symbol(up)), List(Symbol(shut), Symbol(up))))

scala> ℒ
res6: axle.lx.Angluin.Language = Language(List(List(Symbol(hi), Symbol(I'm), Symbol(your), Symbol(Mother)), List(Symbol(shut), Symbol(up))))

scala> T.isFor(ℒ)
res7: Boolean = false
```

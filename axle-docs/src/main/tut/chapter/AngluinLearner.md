
Angluin Learner
===============

Models <a href="https://en.wikipedia.org/wiki/Dana_Angluin">Dana Angluin's</a> Language Learner.

Example
-------

Imports

```book:silent
import axle._
import axle.lx._
import Angluin._
import spire.implicits._
```

Setup

```book:silent
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

```book
ɸ.guesses(T).
  find(_.ℒ === ℒ).
  map(finalGuess => "well done, ɸ").
  getOrElse("ɸ never made a correct guess")

T

ℒ

T.isFor(ℒ)
```

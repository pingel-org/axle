---
layout: page
title: Angluin Learner
permalink: /tutorial/angluin_learner/
---

Models [Dana Angluin](https://en.wikipedia.org/wiki/Dana_Angluin)'s Language Learner.

## Example

Imports

```scala mdoc:silent
import axle._
import axle.lx._
import Angluin._
```

Setup

```scala mdoc:silent
val mHi = Symbol("hi")
val mIm = Symbol("I'm")
val mYour = Symbol("your")
val mMother = Symbol("Mother")
val mShut = Symbol("shut")
val mUp = Symbol("up")

val Σ = Alphabet(Set(mHi, mIm, mYour, mMother, mShut, mUp))

val s1 = Expression(mHi :: mIm :: mYour :: mMother :: Nil)
val s2 = Expression(mShut :: mUp :: Nil)
val ℒ = Language(Set(s1, s2))

val T = Text(s1 :: ♯ :: ♯ :: s2 :: ♯ :: s2 :: s2 :: Nil)

val ɸ = memorizingLearner
```

Usage

```scala mdoc
import axle.algebra.lastOption

val outcome = lastOption(ɸ.guesses(T))

outcome.get.ℒ

ℒ

T

T.isFor(ℒ)
```

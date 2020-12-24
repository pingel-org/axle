---
layout: page
title: Gold Paradigm
permalink: /tutorial/gold_paradigm/
---

Models the Gold Paradigm.

## Example

Imports

```scala mdoc:silent
import axle._
import axle.lx._
import GoldParadigm._
```

Setup

```scala mdoc:silent
val mHi = Morpheme("hi")
val mIm = Morpheme("I'm")
val mYour = Morpheme("your")
val mMother = Morpheme("Mother")
val mShut = Morpheme("shut")
val mUp = Morpheme("up")

val Σ = Vocabulary(Set(mHi, mIm, mYour, mMother, mShut, mUp))

val s1 = Expression(mHi :: mIm :: mYour :: mMother :: Nil)
val s2 = Expression(mShut :: mUp :: Nil)

val ℒ = Language(Set(s1, s2))

val T = Text(s1 :: ♯ :: ♯ :: s2 :: ♯ :: s2 :: s2 :: Nil)

val ɸ = memorizingLearner
```

Usage

```scala mdoc
import axle.algebra.lastOption

lastOption(ɸ.guesses(T)).get

ℒ

T

T.isFor(ℒ)
```

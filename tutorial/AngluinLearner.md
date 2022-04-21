---
layout: page
title: Angluin Learner
permalink: /tutorial/angluin_learner/
---

Models [Dana Angluin](https://en.wikipedia.org/wiki/Dana_Angluin)'s Language Learner.

## Example

Imports

```scala
import axle._
import axle.lx._
import Angluin._
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

val s1 = Expression(mHi :: mIm :: mYour :: mMother :: Nil)
val s2 = Expression(mShut :: mUp :: Nil)
val ℒ = Language(Set(s1, s2))

val T = Text(s1 :: ♯ :: ♯ :: s2 :: ♯ :: s2 :: s2 :: Nil)

val ɸ = memorizingLearner
```

Usage

```scala
import axle.algebra.lastOption

val outcome = lastOption(ɸ.guesses(T))
// outcome: Option[Grammar] = Some(
//   value = HardCodedGrammar(
//     ℒ = Language(
//       sequences = Set(
//         Expression(symbols = List('hi, 'I'm, 'your, 'Mother)),
//         Expression(symbols = List('shut, 'up))
//       )
//     )
//   )
// )

outcome.get.ℒ
// res0: Language = Language(
//   sequences = Set(
//     Expression(symbols = List('hi, 'I'm, 'your, 'Mother)),
//     Expression(symbols = List('shut, 'up))
//   )
// )

ℒ
// res1: Language = Language(
//   sequences = Set(
//     Expression(symbols = List('hi, 'I'm, 'your, 'Mother)),
//     Expression(symbols = List('shut, 'up))
//   )
// )

T
// res2: Text = Iterable(
//   Expression(symbols = List('hi, 'I'm, 'your, 'Mother)),
//   Expression(symbols = List()),
//   Expression(symbols = List()),
//   Expression(symbols = List('shut, 'up)),
//   Expression(symbols = List()),
//   Expression(symbols = List('shut, 'up)),
//   Expression(symbols = List('shut, 'up))
// )

T.isFor(ℒ)
// res3: Boolean = true
```

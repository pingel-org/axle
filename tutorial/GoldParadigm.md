---
layout: page
title: Gold Paradigm
permalink: /tutorial/gold_paradigm/
---

Models the Gold Paradigm.

## Example

Imports

```scala
import axle._
import axle.lx._
import GoldParadigm._
```

Setup

```scala
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

```scala
import axle.algebra.lastOption

lastOption(ɸ.guesses(T)).get
// res0: Grammar = HardCodedGrammar(
//   ℒ = Language(
//     sequences = Set(
//       Expression(
//         morphemes = List(
//           Morpheme(s = "hi"),
//           Morpheme(s = "I'm"),
//           Morpheme(s = "your"),
//           Morpheme(s = "Mother")
//         )
//       ),
//       Expression(morphemes = List(Morpheme(s = "shut"), Morpheme(s = "up")))
//     )
//   )
// )

ℒ
// res1: Language = Language(
//   sequences = Set(
//     Expression(
//       morphemes = List(
//         Morpheme(s = "hi"),
//         Morpheme(s = "I'm"),
//         Morpheme(s = "your"),
//         Morpheme(s = "Mother")
//       )
//     ),
//     Expression(morphemes = List(Morpheme(s = "shut"), Morpheme(s = "up")))
//   )
// )

T
// res2: Text = Text(
//   expressions = List(
//     Expression(
//       morphemes = List(
//         Morpheme(s = "hi"),
//         Morpheme(s = "I'm"),
//         Morpheme(s = "your"),
//         Morpheme(s = "Mother")
//       )
//     ),
//     Expression(morphemes = List()),
//     Expression(morphemes = List()),
//     Expression(morphemes = List(Morpheme(s = "shut"), Morpheme(s = "up"))),
//     Expression(morphemes = List()),
//     Expression(morphemes = List(Morpheme(s = "shut"), Morpheme(s = "up"))),
//     Expression(morphemes = List(Morpheme(s = "shut"), Morpheme(s = "up")))
//   )
// )

T.isFor(ℒ)
// res3: Boolean = true
```

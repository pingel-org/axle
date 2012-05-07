package org.pingel.angluin

import org.specs2.mutable._

import scala.collection._

class AngluinSpecification extends Specification {

  "Angluin's Language Learner" should {

    "work" in {

      var Σ = mutable.Set[Symbol]()

      val mHi = Symbol("hi", Σ)
      val mIm = Symbol("I'm", Σ)
      val mYour = Symbol("your", Σ)
      val mMother = Symbol("Mother", Σ)
      val mShut = Symbol("shut", Σ)
      val mUp = Symbol("up", Σ)

      val s1 = MutableExpression(List(mHi, mIm, mYour, mMother))
      val s2 = MutableExpression(List(mShut, mUp))
      val ℒ = Language(List(s1, s2))

      val T = Text(List(s1, ▦(), ▦(), s2, ▦(), s2, s2))

      println("Text T = " + T)
      println("Language ℒ = " + ℒ)
      println()

      if (T.isFor(ℒ)) {
        println("T is for ℒ")
      } else {
        println("T is not for ℒ")
      }
      println()

      var ɸ = MemorizingLearner(T)

      var guess: Grammar = null

      while (ɸ.hasNextExpression()) {
        guess = ɸ.processNextExpression()
        if (guess != null) {
          val guessedLanguage = guess.getℒ
          println("ɸ.processNextExpression().ℒ = " + guessedLanguage)
          if (guessedLanguage.equals(ℒ)) {
            println("ɸ identified the language using the text")
            exit(0)
          } else {
            println("ɸ's guess was not correct\n")
          }
        }
      }

      if (guess == null) {
        println("ɸ never made a guess")
      }

      1 must be equalTo (1)

    }

  }

}
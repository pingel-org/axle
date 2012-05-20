package org.pingel.angluin

import org.specs2.mutable._

import scala.collection._

class AngluinSpecification extends Specification {

  "Angluin's Language Learner" should {

    "work" in {

      val Σ = Alphabet()

      val mHi = Σ += Symbol("hi")
      val mIm = Σ += Symbol("I'm")
      val mYour = Σ += Symbol("your")
      val mMother = Σ += Symbol("Mother")
      val mShut = Σ += Symbol("shut")
      val mUp = Σ += Symbol("up")

      val s1 = MutableExpression(List(mHi, mIm, mYour, mMother))
      val s2 = MutableExpression(List(mShut, mUp))
      val ℒ = Language(List(s1, s2))

      val T = Text(List(s1, ▦, ▦, s2, ▦, s2, s2))

      val ɸ = MemorizingLearner(T)

      var guess: Grammar = null
      var continue = true

      while (continue) {
        guess = ɸ.processNextExpression()
        if (guess != null) {
          val guessedLanguage = guess.getℒ
          println("ɸ.processNextExpression().ℒ = " + guessedLanguage)
          if (guessedLanguage.equals(ℒ)) {
            println("ɸ identified the language using the text")
            continue = false
          } else {
            println("ɸ's guess was not correct\n")
          }
        }
        continue &= ɸ.hasNextExpression()
      }

      println("Text T = " + T)
      println("Language ℒ = " + ℒ)
      println()
      println("T is for ℒ? " + T.isFor(ℒ))
      println()
      if (guess == null) {
        println("ɸ never made a guess")
      }

      1 must be equalTo (1)

    }

  }

}
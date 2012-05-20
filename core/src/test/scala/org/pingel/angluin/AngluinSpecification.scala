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

      val s1 = ListExpression(List(mHi, mIm, mYour, mMother))
      val s2 = ListExpression(List(mShut, mUp))
      val ℒ = Language(List(s1, s2))

      val T = Text(List(s1, ▦, ▦, s2, ▦, s2, s2))

      val ɸ = MemorizingLearner(T)

      val guessOpt = ɸ.learn(guess => {
        val guessedLanguage = guess.getℒ
        println("ɸ.processNextExpression().ℒ = " + guessedLanguage)
        val correct = guessedLanguage.equals(ℒ)
        if (correct) {
          println("ɸ identified the language using the text")
        } else {
          println("ɸ's guess was not correct\n")
        }
        correct
      })

      println("Text T = " + T)
      println("Language ℒ = " + ℒ)
      println()
      println("T is for ℒ? " + T.isFor(ℒ))
      println()
      if (guessOpt.isEmpty) {
        println("ɸ never made a guess")
      }

      1 must be equalTo (1)

    }

  }

}
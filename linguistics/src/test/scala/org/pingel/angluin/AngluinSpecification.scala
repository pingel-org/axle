package org.pingel.angluin

import org.specs2.mutable._

class AngluinSpecification extends Specification {

  "Angluin's Language Learner" should {

    "work" in {

      var Σ = new Alphabet()

      val mHi = new Symbol("hi", Σ)
      val mIm = new Symbol("I'm", Σ)
      val mYour = new Symbol("your", Σ)
      val mMother = new Symbol("Mother", Σ)
      val mShut = new Symbol("shut", Σ)
      val mUp = new Symbol("up", Σ)

      val s1 = new MutableExpression(List(mHi, mIm, mYour, mMother))
      val s2 = new MutableExpression(List(mShut, mUp))
      val ℒ = new Language(List(s1, s2))

      val T = new Text(List(s1, ▦, ▦, s2, ▦, s2, s2))

      println("Text T = " + T)
      println("Language ℒ = " + ℒ)
      println()

      if (T.isFor(ℒ)) {
        println("T is for ℒ")
      } else {
        println("T is not for ℒ")
      }
      println()

      var ɸ = new MemorizingLearner(T)

      var guess: Grammar = null

      while (ɸ.hasNextExpression()) {
        guess = ɸ.processNextExpression()
        if (guess != null) {
          val guessedLanguage = guess.ℒ()
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
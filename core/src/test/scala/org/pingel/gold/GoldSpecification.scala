package org.pingel.gold

import org.specs2.mutable._

class GoldSpecification extends Specification {

  "Gold Paradigm" should {
    
    "work" in {

      val Σ = new Vocabulary()

      val mHi = Σ.morpheme("hi")
      val mIm = Σ.morpheme("I'm")
      val mYour = Σ.morpheme("your")
      val mMother = Σ.morpheme("Mother")
      val mShut = Σ.morpheme("shut")
      val mUp = Σ.morpheme("up")

      val ℒ = new Language()

      val s1 = ℒ.expression(mHi :: mIm :: mYour :: mMother :: Nil)
      val s2 = ℒ.expression(mShut :: mUp :: Nil)

      val T = new Text(s1 :: ▦ :: ▦ :: s2 :: ▦ :: s2 :: s2 :: Nil)

      val ɸ = new MemorizingLearner(T)
      var guess: Grammar = null
      while (ɸ.hasNextExpression()) {
        guess = ɸ.processNextExpression()
        if (guess != null) {
          var guessedLanguage = guess.getL
          println("ɸ.processNextExpression().L = " + guessedLanguage)
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

      println("Language ℒ = " + ℒ)
      println("Text T = " + T)
      println()
      println("T is for ℒ? " + T.isFor(ℒ))
      println()

      1 must be equalTo (1)
    
    }
  }
}
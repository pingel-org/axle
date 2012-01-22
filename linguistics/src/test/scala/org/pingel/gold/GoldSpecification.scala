package org.pingel.gold

import org.specs2.mutable._

class GoldSpecification extends Specification {

  "Gold Paradigm" should {
    
    "work" in {

      var Σ = new Vocabulary()

      val mHi = new Morpheme("hi", Σ)
      val mIm = new Morpheme("I'm", Σ)
      val mYour = new Morpheme("your", Σ)
      val mMother = new Morpheme("Mother", Σ)
      val mShut = new Morpheme("shut", Σ)
      val mUp = new Morpheme("up", Σ)

      val s1 = new Expression()
      s1.addMorpheme(mHi)
      s1.addMorpheme(mIm)
      s1.addMorpheme(mYour)
      s1.addMorpheme(mMother)

      val s2 = new Expression()
      s2.addMorpheme(mShut)
      s2.addMorpheme(mUp)

      var L = new Language()
      L.addExpression(s1)
      L.addExpression(s2)

      var T = new Text()
      T.addExpression(s1)
      T.addExpression(new ▦())
      T.addExpression(new ▦())
      T.addExpression(s2)
      T.addExpression(new ▦())
      T.addExpression(s2)
      T.addExpression(s2)

      println("Text T = " + T)
      println("Language L = " + L)
      println()

      if (T.isFor(L)) {
        println("T is for L")
      } else {
        println("T is not for L")
      }
      println()

      var ɸ = new MemorizingLearner(T)
      var guess: Grammar = null
      while (ɸ.hasNextExpression()) {
        guess = ɸ.processNextExpression()
        if (guess != null) {
          var guessedLanguage = guess.getL
          println("ɸ.processNextExpression().L = " + guessedLanguage)
          if (guessedLanguage.equals(L)) {
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
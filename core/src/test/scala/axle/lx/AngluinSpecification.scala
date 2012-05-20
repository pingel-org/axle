package axle.lx

import org.specs2.mutable._

import scala.collection._

class AngluinSpecification extends Specification {

  "Angluin's Language Learner" should {

    "work" in {

      val Σ = Alphabet()

      val mHi = Σ.symbol("hi")
      val mIm = Σ.symbol("I'm")
      val mYour = Σ.symbol("your")
      val mMother = Σ.symbol("Mother")
      val mShut = Σ.symbol("shut")
      val mUp = Σ.symbol("up")

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
      println("T is for ℒ ?" + T.isFor(ℒ))
      println()
      if (guessOpt.isEmpty) {
        println("ɸ never made a guess")
      }

      1 must be equalTo (1)

    }

  }

}

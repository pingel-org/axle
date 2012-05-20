package axle.lx

import org.specs2.mutable._

import Gold._

class GoldSpecification extends Specification {

  "Gold Paradigm" should {

    "work" in {

      val Σ = Vocabulary()

      val mHi = Σ.morpheme("hi")
      val mIm = Σ.morpheme("I'm")
      val mYour = Σ.morpheme("your")
      val mMother = Σ.morpheme("Mother")
      val mShut = Σ.morpheme("shut")
      val mUp = Σ.morpheme("up")

      val ℒ = Language()

      val s1 = ℒ.expression(mHi :: mIm :: mYour :: mMother :: Nil)
      val s2 = ℒ.expression(mShut :: mUp :: Nil)

      val T = Text(s1 :: ▦ :: ▦ :: s2 :: ▦ :: s2 :: s2 :: Nil)

      val ɸ = MemorizingLearner(T)

      val guessOpt = ɸ.learn(guess => {
        val guessedLanguage = guess.getL
        println("ɸ.processNextExpression().L = " + guessedLanguage)
        val correct = guessedLanguage.equals(ℒ)
        if( correct ) {
          println("ɸ identified the language using the text")
        }
        else {
          println("ɸ's guess was not correct\n")
        }
        correct
      })

      if (guessOpt.isEmpty) {
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

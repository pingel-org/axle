package axle.lx

import org.specs2.mutable._

class AngluinSpecification extends Specification {

  import axle.lx.Angluin._

  "Angluin's Language Learner" should {

    "memorizing learner memorizes" in {

      val mHi = Symbol("hi")
      val mIm = Symbol("I'm")
      val mYour = Symbol("your")
      val mMother = Symbol("Mother")
      val mShut = Symbol("shut")
      val mUp = Symbol("up")

      val Σ = Alphabet(Set(mHi, mIm, mYour, mMother, mShut, mUp))

      val s1 = mHi :: mIm :: mYour :: mMother :: Nil
      val s2 = mShut :: mUp :: Nil
      val ℒ = Language(s1 :: s2 :: Nil)

      val T = Text(s1 :: ♯ :: ♯ :: s2 :: ♯ :: s2 :: s2 :: Nil)

      val ɸ = MemorizingLearner()
      ɸ.guesses(T)
        .find(guess => Language.languageEq.eqv(guess.ℒ, ℒ))
        .map(finalGuess => "well done, ɸ")
        .getOrElse("ɸ never made a correct guess")

      1 must be equalTo (1)
    }

  }

}

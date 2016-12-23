package axle.lx

import org.scalatest._

class AngluinSpecification extends FunSuite with Matchers {

  import axle.lx.Angluin._

  test("Angluin's Language Learner: memorizing learner memorizes") {

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
    val outcome = ɸ.guesses(T)
      .find(guess => Language.languageEq.eqv(guess.ℒ, ℒ))
      .map(finalGuess => "well done, ɸ")
      .getOrElse("ɸ never made a correct guess")

    outcome should be("well done, ɸ")
  }

}

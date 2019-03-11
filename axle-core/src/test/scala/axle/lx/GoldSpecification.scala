package axle.lx

import org.scalatest._
import Gold._

class GoldSpecification extends FunSuite with Matchers {

  test("Gold Paradigm: memorizing learner memorizes") {

    val mHi = Morpheme("hi")
    val mIm = Morpheme("I'm")
    val mYour = Morpheme("your")
    val mMother = Morpheme("Mother")
    val mShut = Morpheme("shut")
    val mUp = Morpheme("up")

    // val Σ = Vocabulary(Set(mHi, mIm, mYour, mMother, mShut, mUp))

    val s1 = mHi :: mIm :: mYour :: mMother :: Nil
    val s2 = mShut :: mUp :: Nil

    val ℒ = Language(Set(s1, s2))

    val T = Text(s1 :: ♯ :: ♯ :: s2 :: ♯ :: s2 :: s2 :: Nil)

    val ɸ = MemorizingLearner()
    val outcome = ɸ.guesses(T)
      .find(_.ℒ === ℒ)
      .map(finalGuess => "well done, ɸ")
      .getOrElse("ɸ never made a correct guess")

    // T.isFor(ℒ)

    outcome should be("ɸ never made a correct guess")
  }
}

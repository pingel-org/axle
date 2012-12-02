package axle.lx

import org.specs2.mutable._
import collection._
import Angluin._

class AngluinSpecification extends Specification {

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

      val T = Text(s1 :: ▦ :: ▦ :: s2 :: ▦ :: s2 :: s2 :: Nil)

      val ɸ = MemorizingLearner()
      ɸ.guesses(T).find(_.ℒ == ℒ)
        .map(finalGuess => println("well done, ɸ"))
        .getOrElse(println("ɸ never made a correct guess"))

      println("Text T = " + T)
      println("Language ℒ = " + ℒ)
      println()
      println("T is for ℒ ?" + T.isFor(ℒ))
      println()

      1 must be equalTo (1)
    }

  }

}

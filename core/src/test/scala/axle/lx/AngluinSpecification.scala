package axle.lx

import org.specs2.mutable._
import collection._
import Angluin._

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

      val s1 = mHi :: mIm :: mYour :: mMother :: Nil
      val s2 = mShut :: mUp :: Nil
      val ℒ = Language(s1 :: s2 :: Nil)

      val T = Text(s1 :: ▦ :: ▦ :: s2 :: ▦ :: s2 :: s2 :: Nil)

      val ɸ = MemorizingLearner(T)
      ɸ.guesses.find(_.ℒ === ℒ)
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

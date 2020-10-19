package axle.lx

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import cats.implicits._

import axle.algebra._
import axle.lx.Angluin._

class AngluinSpecification extends AnyFunSuite with Matchers {

  val mHi = Symbol("hi")
  val mIm = Symbol("I'm")
  val mYour = Symbol("your")
  val mMother = Symbol("Mother")
  val mShut = Symbol("shut")
  val mUp = Symbol("up")

  val Σ = Alphabet(Set(mHi, mIm, mYour, mMother, mShut, mUp))

  val s1 = Expression(mHi :: mIm :: mYour :: mMother :: Nil)
  val s2 = Expression(mShut :: mUp :: Nil)

  val ℒ = Language(Set(s1, s2))

  val T = Text(s1 :: ♯ :: ♯ :: s2 :: ♯ :: s2 :: s2 :: Nil)

  test("Alphabet") {
    Σ.symbols.size should be(6)
  }

  test("Show[Text]") {
    T.show.length should be(139)
  }

  test("Text.isFor(Language)") {
    T.content should be(ℒ)
    T.isFor(ℒ) should be(true)
  }

  test("silent learner never guesses") {

    val ɸ = silentLearner

    val outcome = lastOption(ɸ.guesses(T))

    outcome should be(None)
  }

  test("hard-coded learner hard-codes") {

    val ɸ = hardCodedLearner(HardCodedGrammar(ℒ))

    val outcome = lastOption(ɸ.guesses(T))

    outcome.get.ℒ should be(ℒ)
  }

  test("memorizing learner memorizes") {

    val ɸ = memorizingLearner

    val outcome = lastOption(ɸ.guesses(T))

    outcome.get.ℒ should be(ℒ)
  }

}

package axle.ml

import org.scalatest._

import cats.implicits._
import spire.math.Rational
import axle.stats.Variable
// import axle.stats.rationalProbabilityDist

class NaiveBayesSpecification extends FunSuite with Matchers {

  case class Tennis(outlook: String, temperature: String, humidity: String, wind: String, play: Boolean)

  val data =
    Tennis("Sunny", "Hot", "High", "Weak", false) ::
      Tennis("Sunny", "Hot", "High", "Strong", false) ::
      Tennis("Overcast", "Hot", "High", "Weak", true) ::
      Tennis("Rain", "Mild", "High", "Weak", true) ::
      Tennis("Rain", "Cool", "Normal", "Weak", true) ::
      Tennis("Rain", "Cool", "Normal", "Strong", false) ::
      Tennis("Overcast", "Cool", "Normal", "Strong", true) ::
      Tennis("Sunny", "Mild", "High", "Weak", false) ::
      Tennis("Sunny", "Cool", "Normal", "Weak", true) ::
      Tennis("Rain", "Mild", "Normal", "Weak", true) ::
      Tennis("Sunny", "Mild", "Normal", "Strong", true) ::
      Tennis("Overcast", "Mild", "High", "Strong", true) ::
      Tennis("Overcast", "Hot", "Normal", "Weak", true) ::
      Tennis("Rain", "Mild", "High", "Strong", false) :: Nil

  test("naive bayes tennis classifier: predict play in dataset #1") {

    import cats.implicits._
    val classifier1 = NaiveBayesClassifier[Tennis, String, Boolean, List, Rational](
      data,
      List(
        (Variable[String]("Outlook") -> Vector("Sunny", "Overcast", "Rain")),
        (Variable[String]("Temperature") -> Vector("Hot", "Mild", "Cool")),
        (Variable[String]("Humidity") -> Vector("High", "Normal", "Low")),
        (Variable[String]("Wind") -> Vector("Weak", "Strong"))),
      (Variable[Boolean]("Play") -> Vector(true, false)),
      (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
      (t: Tennis) => t.play)

    val performance1 = ClassifierPerformance[Rational, Tennis, List](
      data,
      classifier1,
      _.play)

    performance1.tp should be(9)
    performance1.fp should be(1)
    performance1.tn should be(4)
    performance1.fn should be(0)
    performance1.precision should be(Rational(9, 10))
    performance1.recall should be(Rational(1))
    performance1.accuracy should be(Rational(13, 14))
    performance1.specificity should be(Rational(4, 5))
    performance1.f1Score should be(Rational(18, 19))
  }

  // http://www.dhgarrette.com/nlpclass/assignments/a2classification.html

  val data2 =
    Tennis("Sunny", "Cool", "High", "Strong", false) ::
      Tennis("Overcast", "Cool", "Normal", "Weak", true) ::
      Tennis("Sunny", "Hot", "Normal", "Weak", true) ::
      Tennis("Rain", "Hot", "High", "Strong", false) ::
      Tennis("Sunny", "Cool", "Normal", "Weak", true) ::
      Tennis("Overcast", "Hot", "High", "Strong", false) ::
      Tennis("Sunny", "Mild", "High", "Weak", true) ::
      Tennis("Overcast", "Mild", "Normal", "Strong", true) ::
      Tennis("Rain", "Cool", "Normal", "Strong", false) ::
      Tennis("Overcast", "Cool", "Normal", "Strong", true) ::
      Tennis("Rain", "Hot", "Normal", "Weak", true) ::
      Tennis("Sunny", "Cool", "High", "Weak", true) ::
      Tennis("Rain", "Hot", "Normal", "Strong", false) :: Nil

//   * Total: 13
//   *
//   * Outlook    : Sunny  5, Overcast 4, Rain 4
//   * Temperature: Hot    5, Mild     2, Cool 6
//   * Humidity   : High   5, Normal   8
//   * Wind       : Strong 7, Weak     6
//   * Play       : true   8, false    5
//   *
//   * All predictions of 'Play' by Naive Bayes are correct

  test("naive bayes tennis classifier: predict play in dataset #2") {

    val classifier2 = NaiveBayesClassifier[Tennis, String, Boolean, List, Rational](
      data2,
      List(
        (Variable[String]("Outlook") -> Vector("Sunny", "Overcast", "Rain")),
        (Variable[String]("Temperature") -> Vector("Hot", "Mild", "Cool")),
        (Variable[String]("Humidity") -> Vector("High", "Normal", "Low")),
        (Variable[String]("Wind") -> Vector("Weak", "Strong"))),
      (Variable[Boolean]("Play") -> Vector(true, false)),
      (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
      (t: Tennis) => t.play)

    val performance2 = ClassifierPerformance[Rational, Tennis, List](
      data, // Note: not the same as the training dataset
      classifier2,
      _.play)

    performance2.tp should be(9)
    performance2.fp should be(3)
    performance2.tn should be(2)
    performance2.fn should be(0)
    performance2.precision should be(Rational(3, 4))
    performance2.recall should be(Rational(1))
    performance2.specificity should be(Rational(2, 5))
    performance2.accuracy should be(Rational(11, 14))
    performance2.f1Score should be(Rational(6, 7))
    performance2.show should include("F1")
  }

}

package axle.ml

import spire.math._
import axle.stats.UnknownDistribution0
import axle.stats._
import spire.algebra._
import spire.math._
import spire.implicits._
import axle._
import org.specs2.mutable._

object NaiveBayesSpecification extends Specification {

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

  "classifier f1 score" should {
    "work" in {

      val classifier = NaiveBayesClassifier(
        data,
        List(
          UnknownDistribution0[String, Rational](Vector("Sunny", "Overcast", "Rain"), "Outlook"),
          UnknownDistribution0[String, Rational](Vector("Hot", "Mild", "Cool"), "Temperature"),
          UnknownDistribution0[String, Rational](Vector("High", "Normal", "Low"), "Humidity"),
          UnknownDistribution0[String, Rational](Vector("Weak", "Strong"), "Wind")),
        UnknownDistribution0[Boolean, Rational](Vector(true, false), "Play"),
        (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
        (t: Tennis) => t.play)

      // data map { datum => datum.toString + "\t" + classifier(datum) } mkString ("\n")

      val performance = ClassifierPerformance[Rational, Tennis, List](
        data,
        classifier,
        _.play)

      //show(performance)
      //Precision   9/10
      //Recall      9/13
      //Specificity 0
      //Accuracy    9/14
      //F1 Score    18/23        

      performance.precision must be equalTo Rational(9, 10)
      performance.f1Score must be equalTo Rational(18, 23)
    }
  }

}
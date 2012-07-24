
/**
 * Useful links:
 *
 * http://en.wikipedia.org/wiki/Naive_Bayes_classifier
 *
 */

object nbO {

  import axle.ml.NaiveBayesClassifier

  case class Tennis(outlook: String, temperature: String, humidity: String, wind: String, play: Boolean)

  val data = Tennis("Sunny", "Hot", "High", "Weak", false) ::
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

  val classifier = new NaiveBayesClassifier(
    data,
    List(
      "Outlook" -> List("Sunny", "Overcast", "Rain"),
      "Temperature" -> List("Hot", "Mild", "Cool"),
      "Humidity" -> List("High", "Normal", "Low"),
      "Wind" -> List("Weak", "Strong")
    ),
    (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
    (t: Tennis) => t.play.toString)

  for (datum <- data) {
    println(datum + "\t" + classifier.predict(datum))
  }

  val (precision, recall, specificity, accuracy) = classifier.performance(data.iterator, "true")

  println("precision  : " + precision)
  println("recall     : " + recall)
  println("specificity: " + specificity)
  println("accuracy   : " + accuracy)

}

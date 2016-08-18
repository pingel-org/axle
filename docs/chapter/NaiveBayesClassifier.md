Naïve Bayes
===========

Tennis
------

The classic tennis example:

```scala
scala> case class Tennis(outlook: String, temperature: String, humidity: String, wind: String, play: Boolean)
defined class Tennis

scala> val events = Tennis("Sunny", "Hot", "High", "Weak", false) ::
<console>:14: error: value :: is not a member of Tennis
       val events = Tennis("Sunny", "Hot", "High", "Weak", false) ::
                                                                  ^

scala>     Tennis("Sunny", "Hot", "High", "Strong", false) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Sunny", "Hot", "High", "Strong", false) ::
                                                           ^

scala>     Tennis("Overcast", "Hot", "High", "Weak", true) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Overcast", "Hot", "High", "Weak", true) ::
                                                           ^

scala>     Tennis("Rain", "Mild", "High", "Weak", true) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Rain", "Mild", "High", "Weak", true) ::
                                                        ^

scala>     Tennis("Rain", "Cool", "Normal", "Weak", true) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Rain", "Cool", "Normal", "Weak", true) ::
                                                          ^

scala>     Tennis("Rain", "Cool", "Normal", "Strong", false) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Rain", "Cool", "Normal", "Strong", false) ::
                                                             ^

scala>     Tennis("Overcast", "Cool", "Normal", "Strong", true) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Overcast", "Cool", "Normal", "Strong", true) ::
                                                                ^

scala>     Tennis("Sunny", "Mild", "High", "Weak", false) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Sunny", "Mild", "High", "Weak", false) ::
                                                          ^

scala>     Tennis("Sunny", "Cool", "Normal", "Weak", true) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Sunny", "Cool", "Normal", "Weak", true) ::
                                                           ^

scala>     Tennis("Rain", "Mild", "Normal", "Weak", true) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Rain", "Mild", "Normal", "Weak", true) ::
                                                          ^

scala>     Tennis("Sunny", "Mild", "Normal", "Strong", true) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Sunny", "Mild", "Normal", "Strong", true) ::
                                                             ^

scala>     Tennis("Overcast", "Mild", "High", "Strong", true) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Overcast", "Mild", "High", "Strong", true) ::
                                                              ^

scala>     Tennis("Overcast", "Hot", "Normal", "Weak", true) ::
<console>:15: error: value :: is not a member of Tennis
           Tennis("Overcast", "Hot", "Normal", "Weak", true) ::
                                                             ^

scala>     Tennis("Rain", "Mild", "High", "Strong", false) :: Nil
res12: List[Tennis] = List(Tennis(Rain,Mild,High,Strong,false))
```

Build a classifier to predict the Boolean feature 'play' given all the other features of the observations

```scala
scala> import axle._
import axle._

scala> import axle.stats._
import axle.stats._

scala> import axle.ml.NaiveBayesClassifier
import axle.ml.NaiveBayesClassifier

scala> import spire.algebra._
import spire.algebra._

scala> import spire.math._
import spire.math._
```

```scala
scala> val classifier = NaiveBayesClassifier[Tennis, String, Boolean, List[Tennis], List[Boolean], Rational](
     |   events,
     |   List(
     |       UnknownDistribution0[String, Rational](Vector("Sunny", "Overcast", "Rain"), "Outlook"),
     |       UnknownDistribution0[String, Rational](Vector("Hot", "Mild", "Cool"), "Temperature"),
     |       UnknownDistribution0[String, Rational](Vector("High", "Normal", "Low"), "Humidity"),
     |       UnknownDistribution0[String, Rational](Vector("Weak", "Strong"), "Wind")
     |   ),
     |   UnknownDistribution0[Boolean, Rational](Vector(true, false), "Play"),
     |   (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
     |   (t: Tennis) => t.play)
<console>:28: error: not found: value events
         events,
         ^
     | 
     | events map { datum => datum.toString + "\t" + classifier(datum) } mkString("\n")
```

Measure the classifier's performance

```scala
     | import axle.ml.ClassifierPerformance
<console>:14: error: ')' expected but 'import' found.
import axle.ml.ClassifierPerformance
^
     | 
     | val perf = ClassifierPerformance[Rational, Tennis, List[Tennis], List[(Rational, Rational, Rational, Rational)]](events, classifier, _.play)
<console>:15: error: ')' expected but 'val' found.
val perf = ClassifierPerformance[Rational, Tennis, List[Tennis], List[(Rational, Rational, Rational, Rational)]](events, classifier, _.play)
^
     | 
     | string(perf)
```

See <a href="http://en.wikipedia.org/wiki/Precision_and_recall">Precision and Recall</a>
for the definition of the performance metrics.

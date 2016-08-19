Naïve Bayes
===========

Tennis
------

The classic tennis example:

```scala
scala> case class Tennis(outlook: String, temperature: String, humidity: String, wind: String, play: Boolean)
defined class Tennis

scala> val events = List(
     |   Tennis("Sunny", "Hot", "High", "Weak", false),
     |   Tennis("Sunny", "Hot", "High", "Strong", false),
     |   Tennis("Overcast", "Hot", "High", "Weak", true),
     |   Tennis("Rain", "Mild", "High", "Weak", true),
     |   Tennis("Rain", "Cool", "Normal", "Weak", true),
     |   Tennis("Rain", "Cool", "Normal", "Strong", false),
     |   Tennis("Overcast", "Cool", "Normal", "Strong", true),
     |   Tennis("Sunny", "Mild", "High", "Weak", false),
     |   Tennis("Sunny", "Cool", "Normal", "Weak", true),
     |   Tennis("Rain", "Mild", "Normal", "Weak", true),
     |   Tennis("Sunny", "Mild", "Normal", "Strong", true),
     |   Tennis("Overcast", "Mild", "High", "Strong", true),
     |   Tennis("Overcast", "Hot", "Normal", "Weak", true),
     |   Tennis("Rain", "Mild", "High", "Strong", false))
events: List[Tennis] = List(Tennis(Sunny,Hot,High,Weak,false), Tennis(Sunny,Hot,High,Strong,false), Tennis(Overcast,Hot,High,Weak,true), Tennis(Rain,Mild,High,Weak,true), Tennis(Rain,Cool,Normal,Weak,true), Tennis(Rain,Cool,Normal,Strong,false), Tennis(Overcast,Cool,Normal,Strong,true), Tennis(Sunny,Mild,High,Weak,false), Tennis(Sunny,Cool,Normal,Weak,true), Tennis(Rain,Mild,Normal,Weak,true), Tennis(Sunny,Mild,Normal,Strong,true), Tennis(Overcast,Mild,High,Strong,true), Tennis(Overcast,Hot,Normal,Weak,true), Tennis(Rain,Mild,High,Strong,false))
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
classifier: axle.ml.NaiveBayesClassifier[Tennis,String,Boolean,List[Tennis],List[Boolean],spire.math.Rational] = <function1>

scala> events map { datum => datum.toString + "\t" + classifier(datum) } mkString("\n")
res0: String =
Tennis(Sunny,Hot,High,Weak,false)	false
Tennis(Sunny,Hot,High,Strong,false)	false
Tennis(Overcast,Hot,High,Weak,true)	true
Tennis(Rain,Mild,High,Weak,true)	true
Tennis(Rain,Cool,Normal,Weak,true)	true
Tennis(Rain,Cool,Normal,Strong,false)	true
Tennis(Overcast,Cool,Normal,Strong,true)	true
Tennis(Sunny,Mild,High,Weak,false)	false
Tennis(Sunny,Cool,Normal,Weak,true)	true
Tennis(Rain,Mild,Normal,Weak,true)	true
Tennis(Sunny,Mild,Normal,Strong,true)	true
Tennis(Overcast,Mild,High,Strong,true)	true
Tennis(Overcast,Hot,Normal,Weak,true)	true
Tennis(Rain,Mild,High,Strong,false)	false
```

Measure the classifier's performance

```scala
scala> import axle.ml.ClassifierPerformance
import axle.ml.ClassifierPerformance

scala> val perf = ClassifierPerformance[Rational, Tennis, List[Tennis], List[(Rational, Rational, Rational, Rational)]](events, classifier, _.play)
perf: axle.ml.ClassifierPerformance[spire.math.Rational,Tennis,List[Tennis],List[(spire.math.Rational, spire.math.Rational, spire.math.Rational, spire.math.Rational)]] = ClassifierPerformance(List(Tennis(Sunny,Hot,High,Weak,false), Tennis(Sunny,Hot,High,Strong,false), Tennis(Overcast,Hot,High,Weak,true), Tennis(Rain,Mild,High,Weak,true), Tennis(Rain,Cool,Normal,Weak,true), Tennis(Rain,Cool,Normal,Strong,false), Tennis(Overcast,Cool,Normal,Strong,true), Tennis(Sunny,Mild,High,Weak,false), Tennis(Sunny,Cool,Normal,Weak,true), Tennis(Rain,Mild,Normal,Weak,true), Tennis(Sunny,Mild,Normal,Strong,true), Tennis(Overcast,Mild,High,Strong,true), Tennis(Overcast,Hot,Normal,Weak,true), Tennis(Rain,Mild,High,Strong,false)),<function1>,<function1>)

scala> string(perf)
res1: String =
"Precision   9/10
Recall      1
Specificity 4/5
Accuracy    13/14
F1 Score    18/19
"
```

See <a href="http://en.wikipedia.org/wiki/Precision_and_recall">Precision and Recall</a>
for the definition of the performance metrics.

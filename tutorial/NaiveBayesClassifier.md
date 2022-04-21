---
layout: page
title: Naïve Bayes
permalink: /tutorial/naive_bayes/
---

## Tennis Example

```scala
case class Tennis(outlook: String, temperature: String, humidity: String, wind: String, play: Boolean)

val events = List(
  Tennis("Sunny", "Hot", "High", "Weak", false),
  Tennis("Sunny", "Hot", "High", "Strong", false),
  Tennis("Overcast", "Hot", "High", "Weak", true),
  Tennis("Rain", "Mild", "High", "Weak", true),
  Tennis("Rain", "Cool", "Normal", "Weak", true),
  Tennis("Rain", "Cool", "Normal", "Strong", false),
  Tennis("Overcast", "Cool", "Normal", "Strong", true),
  Tennis("Sunny", "Mild", "High", "Weak", false),
  Tennis("Sunny", "Cool", "Normal", "Weak", true),
  Tennis("Rain", "Mild", "Normal", "Weak", true),
  Tennis("Sunny", "Mild", "Normal", "Strong", true),
  Tennis("Overcast", "Mild", "High", "Strong", true),
  Tennis("Overcast", "Hot", "Normal", "Weak", true),
  Tennis("Rain", "Mild", "High", "Strong", false))
// events: List[Tennis] = List(
//   Tennis(
//     outlook = "Sunny",
//     temperature = "Hot",
//     humidity = "High",
//     wind = "Weak",
//     play = false
//   ),
//   Tennis(
//     outlook = "Sunny",
//     temperature = "Hot",
//     humidity = "High",
//     wind = "Strong",
//     play = false
//   ),
//   Tennis(
//     outlook = "Overcast",
//     temperature = "Hot",
//     humidity = "High",
//     wind = "Weak",
//     play = true
//   ),
//   Tennis(
//     outlook = "Rain",
//     temperature = "Mild",
//     humidity = "High",
//     wind = "Weak",
//     play = true
//   ),
//   Tennis(
//     outlook = "Rain",
//     temperature = "Cool",
//     humidity = "Normal",
//     wind = "Weak",
//     play = true
//   ),
//   Tennis(
//     outlook = "Rain",
//     temperature = "Cool",
//     humidity = "Normal",
//     wind = "Strong",
//     play = false
//   ),
//   Tennis(
//     outlook = "Overcast",
//     temperature = "Cool",
//     humidity = "Normal",
//     wind = "Strong",
//     play = true
// ...
```

Build a classifier to predict the Boolean feature 'play' given all the other features of the observations

```scala
import cats.implicits._

import spire.math._

import axle._
import axle.probability._
import axle.ml.NaiveBayesClassifier
```

```scala
val classifier = NaiveBayesClassifier[Tennis, String, Boolean, List, Rational](
  events,
  List(
    (Variable[String]("Outlook") -> Vector("Sunny", "Overcast", "Rain")),
    (Variable[String]("Temperature") -> Vector("Hot", "Mild", "Cool")),
    (Variable[String]("Humidity") -> Vector("High", "Normal", "Low")),
    (Variable[String]("Wind") -> Vector("Weak", "Strong"))),
  (Variable[Boolean]("Play") -> Vector(true, false)),
  (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
  (t: Tennis) => t.play)
// classifier: NaiveBayesClassifier[Tennis, String, Boolean, List, Rational] = NaiveBayesClassifier(
//   data = List(
//     Tennis(
//       outlook = "Sunny",
//       temperature = "Hot",
//       humidity = "High",
//       wind = "Weak",
//       play = false
//     ),
//     Tennis(
//       outlook = "Sunny",
//       temperature = "Hot",
//       humidity = "High",
//       wind = "Strong",
//       play = false
//     ),
//     Tennis(
//       outlook = "Overcast",
//       temperature = "Hot",
//       humidity = "High",
//       wind = "Weak",
//       play = true
//     ),
//     Tennis(
//       outlook = "Rain",
//       temperature = "Mild",
//       humidity = "High",
//       wind = "Weak",
//       play = true
//     ),
//     Tennis(
//       outlook = "Rain",
//       temperature = "Cool",
//       humidity = "Normal",
//       wind = "Weak",
//       play = true
//     ),
//     Tennis(
//       outlook = "Rain",
//       temperature = "Cool",
//       humidity = "Normal",
//       wind = "Strong",
//       play = false
//     ),
//     Tennis(
//       outlook = "Overcast",
//       temperature = "Cool",
//       humidity = "Normal",
//       wind = "Strong",
// ...
```

Use the classifier to predict:

```scala
events map { datum => datum.toString + "\t" + classifier(datum) } mkString("\n")
// res0: String = """Tennis(Sunny,Hot,High,Weak,false)	false
// Tennis(Sunny,Hot,High,Strong,false)	false
// Tennis(Overcast,Hot,High,Weak,true)	true
// Tennis(Rain,Mild,High,Weak,true)	true
// Tennis(Rain,Cool,Normal,Weak,true)	true
// Tennis(Rain,Cool,Normal,Strong,false)	true
// Tennis(Overcast,Cool,Normal,Strong,true)	true
// Tennis(Sunny,Mild,High,Weak,false)	false
// Tennis(Sunny,Cool,Normal,Weak,true)	true
// Tennis(Rain,Mild,Normal,Weak,true)	true
// Tennis(Sunny,Mild,Normal,Strong,true)	true
// Tennis(Overcast,Mild,High,Strong,true)	true
// Tennis(Overcast,Hot,Normal,Weak,true)	true
// Tennis(Rain,Mild,High,Strong,false)	false"""
```

Measure the classifier's performance

```scala
import axle.ml.ClassifierPerformance

val perf = ClassifierPerformance[Rational, Tennis, List](events, classifier, _.play)
// perf: ClassifierPerformance[Rational, Tennis, List] = ClassifierPerformance(
//   data = List(
//     Tennis(
//       outlook = "Sunny",
//       temperature = "Hot",
//       humidity = "High",
//       wind = "Weak",
//       play = false
//     ),
//     Tennis(
//       outlook = "Sunny",
//       temperature = "Hot",
//       humidity = "High",
//       wind = "Strong",
//       play = false
//     ),
//     Tennis(
//       outlook = "Overcast",
//       temperature = "Hot",
//       humidity = "High",
//       wind = "Weak",
//       play = true
//     ),
//     Tennis(
//       outlook = "Rain",
//       temperature = "Mild",
//       humidity = "High",
//       wind = "Weak",
//       play = true
//     ),
//     Tennis(
//       outlook = "Rain",
//       temperature = "Cool",
//       humidity = "Normal",
//       wind = "Weak",
//       play = true
//     ),
//     Tennis(
//       outlook = "Rain",
//       temperature = "Cool",
//       humidity = "Normal",
//       wind = "Strong",
//       play = false
//     ),
//     Tennis(
//       outlook = "Overcast",
//       temperature = "Cool",
//       humidity = "Normal",
//       wind = "Strong",
// ...

perf.show
// res1: String = """Precision   9/10
// Recall      1
// Specificity 4/5
// Accuracy    13/14
// F1 Score    18/19
// """
```

See [Precision and Recall](http://en.wikipedia.org/wiki/Precision_and_recall)
for the definition of the performance metrics.

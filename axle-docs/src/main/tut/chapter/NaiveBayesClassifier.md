---
layout: page
title: Naïve Bayes
permalink: /chapter/naive_bayes/
---

Tennis Example
--------------

```tut:book
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
```

Build a classifier to predict the Boolean feature 'play' given all the other features of the observations

```tut:book
import axle._
import axle.stats._
import axle.ml.NaiveBayesClassifier
import spire.algebra._
import spire.math._
```

```tut:book
val classifier = NaiveBayesClassifier[Tennis, String, Boolean, List[Tennis], List[Boolean], Rational](
  events,
  List(
      UnknownDistribution0[String, Rational](Vector("Sunny", "Overcast", "Rain"), "Outlook"),
      UnknownDistribution0[String, Rational](Vector("Hot", "Mild", "Cool"), "Temperature"),
      UnknownDistribution0[String, Rational](Vector("High", "Normal", "Low"), "Humidity"),
      UnknownDistribution0[String, Rational](Vector("Weak", "Strong"), "Wind")
  ),
  UnknownDistribution0[Boolean, Rational](Vector(true, false), "Play"),
  (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
  (t: Tennis) => t.play)

events map { datum => datum.toString + "\t" + classifier(datum) } mkString("\n")
```

Measure the classifier's performance

```tut:book
import axle.ml.ClassifierPerformance

val perf = ClassifierPerformance[Rational, Tennis, List[Tennis], List[(Rational, Rational, Rational, Rational)]](events, classifier, _.play)

string(perf)
```

See [Precision and Recall](http://en.wikipedia.org/wiki/Precision_and_recall)
for the definition of the performance metrics.

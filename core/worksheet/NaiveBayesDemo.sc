
object NaiveBayesDemo {

  println("Naive Bayes Demo")                     //> Naive Bayes Demo

  // http://en.wikipedia.org/wiki/Naive_Bayes_classifier

  import axle.stats._
  import axle.ml.NaiveBayesClassifier

  case class Tennis(outlook: String, temperature: String, humidity: String, wind: String, play: Boolean)

  val data = Vector(
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
                                                  //> data  : scala.collection.immutable.Vector[NaiveBayesDemo.Tennis] = Vector(T
                                                  //| ennis(Sunny,Hot,High,Weak,false), Tennis(Sunny,Hot,High,Strong,false), Tenn
                                                  //| is(Overcast,Hot,High,Weak,true), Tennis(Rain,Mild,High,Weak,true), Tennis(R
                                                  //| ain,Cool,Normal,Weak,true), Tennis(Rain,Cool,Normal,Strong,false), Tennis(O
                                                  //| vercast,Cool,Normal,Strong,true), Tennis(Sunny,Mild,High,Weak,false), Tenni
                                                  //| s(Sunny,Cool,Normal,Weak,true), Tennis(Rain,Mild,Normal,Weak,true), Tennis(
                                                  //| Sunny,Mild,Normal,Strong,true), Tennis(Overcast,Mild,High,Strong,true), Ten
                                                  //| nis(Overcast,Hot,Normal,Weak,true), Tennis(Rain,Mild,High,Strong,false))

  val classifier = NaiveBayesClassifier(
    data,
    pFs = RandomVariable0("Outlook", Some(Vector("Sunny", "Overcast", "Rain"))) ::
      RandomVariable0("Temperature", Some(Vector("Hot", "Mild", "Cool"))) ::
      RandomVariable0("Humidity", Some(Vector("High", "Normal", "Low"))) ::
      RandomVariable0("Wind", Some(Vector("Weak", "Strong"))) :: Nil,
    pC = RandomVariable0("Play", Some(Vector(true, false))),
    featureExtractor = (t: Tennis) => t.outlook :: t.temperature :: t.humidity :: t.wind :: Nil,
    classExtractor = (t: Tennis) => t.play)       //> classifier  : axle.ml.NaiveBayesClassifier[NaiveBayesDemo.Tennis,String,Boo
                                                  //| lean] = axle.ml.NaiveBayesClassifier@52f6438d

  for (datum <- data) {
    println(datum + "\t" + classifier.predict(datum))
                                                  //> Tennis(Sunny,Hot,High,Weak,false)	false
                                                  //| Tennis(Sunny,Hot,High,Strong,false)	false
                                                  //| Tennis(Overcast,Hot,High,Weak,true)	true
                                                  //| Tennis(Rain,Mild,High,Weak,true)	true
                                                  //| Tennis(Rain,Cool,Normal,Weak,true)	true
                                                  //| Tennis(Rain,Cool,Normal,Strong,false)	true
                                                  //| Tennis(Overcast,Cool,Normal,Strong,true)	true
                                                  //| Tennis(Sunny,Mild,High,Weak,false)	false
                                                  //| Tennis(Sunny,Cool,Normal,Weak,true)	true
                                                  //| Tennis(Rain,Mild,Normal,Weak,true)	true
                                                  //| Tennis(Sunny,Mild,Normal,Strong,true)	true
                                                  //| Tennis(Overcast,Mild,High,Strong,true)	true
                                                  //| Tennis(Overcast,Hot,Normal,Weak,true)	true
                                                  //| Tennis(Rain,Mild,High,Strong,false)	false
  }

  val (precision, recall, specificity, accuracy) = classifier.performance(data.iterator, true)
                                                  //> precision  : Double = 0.9
                                                  //| recall  : Double = 0.6923076923076923
                                                  //| specificity  : Double = 0.0
                                                  //| accuracy  : Double = 0.6428571428571429

  println("precision  : " + precision)            //> precision  : 0.9
  println("recall     : " + recall)               //> recall     : 0.6923076923076923
  println("specificity: " + specificity)          //> specificity: 0.0
  println("accuracy   : " + accuracy)             //> accuracy   : 0.6428571428571429

  // P( (Fs(0) eq "Rain") | (C eq false))()

}
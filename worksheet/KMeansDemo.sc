object KMeansDemo {

  println("KMeans Clustering Demo")               //> KMeans Clustering Demo

  import util.Random
  import math.{ Pi, cos, sin }
  import axle.ml._

  case class Foo(x: Double, y: Double, label: String)

  // σ2

  def randomFoo(center: Foo, sigmaSquared: Double) = {
    val distance = Random.nextGaussian() * sigmaSquared
    val angle = 2 * Pi * Random.nextDouble
    Foo(center.x + distance * cos(angle), center.y + distance * sin(angle), center.label)
  }                                               //> randomFoo: (center: KMeansDemo.Foo, sigmaSquared: Double)KMeansDemo.Foo

  val data = Random.shuffle(
    (0 until 20).map(i => randomFoo(Foo(15, 15, "A"), 2.0)) ++
      (0 until 30).map(i => randomFoo(Foo(5, 15, "B"), 2.0)) ++
      (0 until 25).map(i => randomFoo(Foo(15, 5, "C"), 2.0)) ++
      (0 until 25).map(i => randomFoo(Foo(12, 6, "D"), 2.0)) ++
      (0 until 25).map(i => randomFoo(Foo(4, 10, "E"), 2.0)) ++
      (0 until 25).map(i => randomFoo(Foo(8, 3, "F"), 2.0)))
                                                  //> data  : <error> = Vector(Foo(8.390042465743758,3.3797245641215237,F), Foo(15
                                                  //| .046579051835192,5.573753672335533,C), Foo(15.304525596037482,5.630004465194
                                                  //| 903,C), Foo(16.007045267880887,2.9
                                                  //| Output exceeds cutoff limit.

/*
  val classifier = KMeans(
    data,
    N = 2,
    featureExtractor = (p: Foo) => List(p.x, p.y),
    constructor = (features: Seq[Double]) => Foo(features(0), features(1), ""),
    K = 6,
    iterations = 100)

  classifier.confusionMatrix(data, (f: Foo) => f.label)

  import axle.visualize._
  
  val plot = Plot(classifier.distanceLogSeries(),
    connect = true,
    drawKey = true,
    title = Some("KMeans Mean Centroid Distances"),
    xAxis = 0.0,
    xAxisLabel = Some("step"),
    yAxis = 0,
    yAxisLabel = Some("average distance to centroid"))
 */
  
  //show(classifier)
  //show(plot)

}
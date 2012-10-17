object First {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  import collection._
  import axle.ml.LinearRegression._
  import axle.visualize._
  import axle.visualize.Plottable._

  case class RealtyListing(size: Double, bedrooms: Int, floors: Int, age: Int, price: Double)

  val data = RealtyListing(2104, 5, 1, 45, 460.0) ::
   RealtyListing(1416, 3, 2, 40, 232.0) ::
    RealtyListing(1534, 3, 2, 30, 315.0) ::
     RealtyListing(852, 2, 1, 36, 178.0) :: Nil   //> data  : List[First.RealtyListing] = List(RealtyListing(2104.0,5,1,45,460.0),
                                                  //|  RealtyListing(1416.0,3,2,40,232.0), RealtyListing(1534.0,3,2,30,315.0), Rea
                                                  //| ltyListing(852.0,2,1,36,178.0))
 
  val estimator = regression(
    data,
    4,
    (rl: RealtyListing) => (rl.size :: rl.bedrooms.toDouble :: rl.floors.toDouble :: rl.age.toDouble :: Nil),
    (rl: RealtyListing) => rl.price,
    0.1,
    100)                                          //> java.lang.UnsatisfiedLinkError: Couldn't find the resource libjblas_arch_fla
                                                  //| vor.jnilib/
                                                  //| /bin/
                                                  //| /lib/static/Mac OS X/x86_64/
                                                  //| /lib/static/Mac OS X/x86_64/
                                                  //| /lib/dynamic/Mac OS X/x86_64/
                                                  //| /lib/dynamic/Mac OS X/x86_64/
                                                  //| /lib/Mac OS X/x86_64/
                                                  //| /Mac OS X/x86_64/
                                                  //| .
                                                  //| 	at org.jblas.util.LibraryLoader.loadLibrary(LibraryLoader.java:100)
                                                  //| 	at org.jblas.util.ArchFlavor.<clinit>(ArchFlavor.java:50)
                                                  //| 	at org.jblas.util.LibraryLoader.loadLibrary(LibraryLoader.java:75)
                                                  //| 	at org.jblas.NativeBlas.<clinit>(NativeBlas.java:84)
                                                  //| 	at org.jblas.SimpleBlas.gesv(SimpleBlas.java:263)
                                                  //| 	at org.jblas.Solve.solve(Solve.java:48)
                                                  //| 	at axle.matrix.JblasMatrixFactory$JblasMatrix$class.invert(JblasMatrixFa
                                                  //| ctory.scala:73)
                                                  //| 	at axle.matrix.JblasMatrixFactory$JblasMatrixImpl.invert(JblasMatrixFact
                                                  //| Output exceeds cutoff limit. 

  val priceGuess = estimator.estimate(RealtyListing(1416, 3, 2, 40, 0.0))

  val errorPlot = Plot(lfs = List(("error" -> estimator.errTree)),
    connect = true,
    drawKey = true,
    title = Some("Linear Regression Error"),
    xAxis = 0.0,
    xAxisLabel = Some("step"),
    yAxis = 0,
    yAxisLabel = Some("error"))

}
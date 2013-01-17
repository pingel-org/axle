object QuantaDemo {

  println("Quanta Demo")                          //> Quanta Demo

  import axle.quanta._

  import Mass._
  import Time._
  import Energy._
  import Distance._
  import Power._
  import Speed._
  import Acceleration._
  import Force._
  import Area._
  import Volume._
  import Flow._

   // Units

   // Standard Units of Measurement are defined:

   gram                                           //> res0: axle.quanta.Mass.Q = gram (g): a measure of MassQuantity
   
   foot                                           //> res1: axle.quanta.Distance.Q = foot (ft): a measure of DistanceQuantity
   
   meter                                          //> res2: axle.quanta.Distance.Q = meter (m): a measure of DistanceQuantity
   earth                                          //> res3: axle.quanta.Mass.Q = earth (?): a measure of MassQuantity

   man                                            //> res4: axle.quanta.Mass.Q = man (man): a measure of MassQuantity
   
   niagaraFalls                                   //> res5: axle.quanta.Flow.Q = Niagara Falls Flow (Niagara Falls Flow): a measur
                                                  //| e of FlowQuantity
   
   greatLakes                                     //> res6: axle.quanta.Volume.Q = Great Lakes Volume (Great Lakes Volume): a meas
                                                  //| ure of VolumeQuantity

   castleBravo                                    //> res7: axle.quanta.Energy.Q = 15 MT

   tonTNT                                         //> res8: axle.quanta.Energy.Q = ton TNT (T): a measure of EnergyQuantity
   
   tonTNT.link                                    //> res9: Option[String] = Some(http://en.wikipedia.org/wiki/TNT_equivalent)

   mustangGT                                      //> res10: axle.quanta.Power.Q = 2012 Mustang GT (2012 Mustang GT): a measure of
                                                  //|  PowerQuantity

   mustangGT.link                                 //> res11: Option[String] = Some(http://en.wikipedia.org/wiki/Ford_Mustang)


   // Construction

   "10" *: gram                                   //> res12: axle.quanta.Mass.Q = 10 g

   "3" *: lightyear                               //> res13: axle.quanta.Distance.Q = 3 ly
   
   5 *: horsepower                                //> res14: axle.quanta.Power.Q = 5 hp
   
   3.14 *: second                                 //> res15: axle.quanta.Time.Q = 3.14 s
   
   200 *: lightBulb                               //> res16: axle.quanta.Power.Q = 200 light bulb
   
   // Conversion
   
   toAndromeda in parsec                          //> res17: axle.quanta.Distance.Q = 798200.0000 pc

   hooverDam in lightBulb                         //> res18: axle.quanta.Power.Q = 41600000.0000 light bulb

   sun in earth                                   //> res19: axle.quanta.Mass.Q = 332975.340000000 ?

   earth in jupiter                               //> res20: axle.quanta.Mass.Q = 0.003146295120 ?

   earth in neptune                               //> res21: axle.quanta.Mass.Q = 0.058302336 ?
   
   earth in mars                                  //> res22: axle.quanta.Mass.Q = 9.3188160 ?
   
   earth in venus                                 //> res23: axle.quanta.Mass.Q = 1.226977440 ?

   10 *: gram in kilogram                         //> res24: axle.quanta.Mass.Q = 0.010 Kg

   // gram in mile
   
   // Math
   
   (1 *: kilogram) + (10 *: gram)                 //> res25: axle.quanta.Mass.Q = 1010.000 g

   (7 *: mile) - (123 *: foot)                    //> res26: axle.quanta.Distance.Q = 36837.00000 ft

   // newton + foot

   (5.4 *: second) * 100                          //> res27: axle.quanta.Time.Q = 540.0 s
   
   (32 *: century) / 1.1                          //> res28: axle.quanta.Time.Q = 29.09 century
   
   greatLakes.over(niagaraFalls, Time)            //> res29: axle.quanta.Time.Q = 1.0

   // greatLakes.over(niagaraFalls, Time) in year
   
}
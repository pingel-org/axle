
 
// service implementations  

trait OnOffDeviceComponentImpl extends OnOffDeviceComponent {  
  class Heater extends OnOffDevice {  
    def on = println("heater.on")  
    def off = println("heater.off")  
  }  
}  
trait SensorDeviceComponentImpl extends SensorDeviceComponent {  
  class PotSensor extends SensorDevice {  
    def isCoffeePresent = true  
  }  
}  

// service declaring two dependencies that it wants injected  

trait WarmerComponentImpl {  
  this: SensorDeviceComponent with OnOffDeviceComponent =>  
  class Warmer {  
    def trigger = {  
      if (sensor.isCoffeePresent) onOff.on  
      else onOff.off  
    }  
  }  
}  
  
// instantiate the services in a module  

object ComponentRegistry extends  
  OnOffDeviceComponentImpl with  
  SensorDeviceComponentImpl with  
  WarmerComponentImpl
{  
  val onOff = new Heater  
  val sensor = new PotSensor  
  val warmer = new Warmer  
}  
  
object Example {
  val warmer = ComponentRegistry.warmer  
  warmer.trigger
}

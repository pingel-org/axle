
object Cake2 { }

case class User(username: String, password: String)

// a dummy service that is not persisting anything  
// solely prints out info  
class UserRepository {
  def authenticate(user: User): User = {   
    println("authenticating user: " + user)  
    user  
  }
  def create(user: User) = println("creating user: " + user)
  def delete(user: User) = println("deleting user: " + user)
} 

class UserService {

  def authenticate(username: String, password: String): User =   
    userRepository.authenticate(username, password)
  
  def create(username: String, password: String) =   
    userRepository.create(new User(username, password))
  
  def delete(user: User) =
    userRepository.delete(user)
}

trait UserRepositoryComponent {  
  val userRepository = new UserRepository  
  class UserRepository {  
    def authenticate(user: User): User = {   
      println("authenticating user: " + user)  
      user  
    }  
    def create(user: User) = println("creating user: " + user)  
    def delete(user: User) = println("deleting user: " + user)  
  }  
}


trait UserServiceComponent {
  this: UserRepositoryComponent =>  
  val userService = new UserService    
  class UserService {  
    def authenticate(username: String, password: String): User =
      userRepository.authenticate(username, password)    
    def create(username: String, password: String) =
      userRepository.create(new User(username, password))
    def delete(user: User) = userRepository.delete(user)  
  }  
} 

object ComponentRegistry extends UserServiceComponent with UserRepositoryComponent   
{
  val userRepository = new UserRepository  
  val userService = new UserService  
} 

val userService = ComponentRegistry.userService  

// =======================  
// service interfaces  
trait OnOffDeviceComponent {  
  val onOff: OnOffDevice  
  trait OnOffDevice {  
    def on: Unit  
    def off: Unit  
  }  
}  
trait SensorDeviceComponent {  
  val sensor: SensorDevice  
  trait SensorDevice {  
    def isCoffeePresent: Boolean  
  }  
}  

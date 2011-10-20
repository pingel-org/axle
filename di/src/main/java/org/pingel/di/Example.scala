
object Example {
  
}

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

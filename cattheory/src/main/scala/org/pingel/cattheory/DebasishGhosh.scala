
package org.pingel.cattheory

// Type Classes in Scala: http://java.dzone.com/articles/scala-implicits-type-classes

/*
object DebasishGhosh {

  case class Address(no: Int, street: String, city: String, state: String, zip: String) { }

  trait LabelMaker[T] {
    def toLabel(value: T): String
  }

  implicit object AddressLabelMaker extends LabelMaker[Address] {
    def toLabel(address: Address): String = {
      import address._
      "[%d %s, %s, %s - %s]".format(no, street, city, state, zip)
    }
  }

  // def printLabel[T](t: T)(implicit lm: LabelMaker[T]) = lm.toLabel(t)
  def printLabel[T: LabelMaker](t: T) = implicitly[LabelMaker[T]].toLabel(t) // as of 2.8

  def main(args: Array[String]) {
    // implicit SpecialLabelMaker._
    val a = Address(100, "Monroe Street", "Denver", "CO", "80231")
    printLabel( a )
//  a.printLabel()
  }

}

object HalfwayAttemptAtMonads {

  case class MonadicOption(value: Int) { }

  trait Monad[T] {
    def flatMap(value: T): String
  }

  implicit object MonadicOptionIO extends Monad[MonadicOption] {
    def flatMap(opt: MonadicOption): String = {
      import opt._
      "[%s %d]".format("hello", value)
    }
  }

  def printLabel[T](t: T)(implicit lm: Monad[T]) = lm.flatMap(t)

  // def printLabel[T: Monad](t: T) = implicitly[Monad[T]].flatMap(t)

  def main(args: Array[String]) {
    val a = MonadicOption(100)
    printLabel( a )
  }

}

object AttemptAtMonads {

  case class MyOption(v: Int) { }

  trait Monad[T] {
    def flatMap(value: T): String
  }

  implicit object MonadicMyOption extends Monad[MyOption] {
    def flatMap(opt: MyOption): String = {
      import opt._
      "[%s %d]".format("hello", v)
    }
  }

  def printLabel[T](t: T)(implicit lm: Monad[T]) = lm.flatMap(t)

  // def printLabel[T: Monad](t: T) = implicitly[Monad[T]].flatMap(t)

  def main(args: Array[String]) {
    val a = MyOption(4)
    printLabel( a )
  }

}

object AttemptAtMonads2 {

  case class MyOption(v: Int) { }

  trait Monad[A] {
    def flatMap[B](f: A => MyOption): MyOption
  }

  implicit object MonadicMyOption extends Monad[MyOption] {
    def flatMap(opt: MyOption): String = {
      import opt._
      "[%s %d]".format("hello", v)
    }
  }

  def flatMap[T](t: T)(implicit lm: Monad[T]) = lm.flatMap({ x: T => MyOption(4)})

  // def printLabel[T: Monad](t: T) = implicitly[Monad[T]].flatMap(t)

  def main(args: Array[String]) {
    val a = MyOption(4)
    flatMap( a )
  }

}

object AttemptAtMonads3 {

  // no implicit object. Just basic library pimping

  abstract case class Monad[A] {
    def bindy[B](f: A => Monad[B]): Monad[B]
  }

  case class MonadicOption[A](opt: Option[A]) extends Monad[A] {
    override def bindy[B](f: A => Option[B]): Option[B] = opt.flatMap(f)
  }

  case class MonadicList[A](list: List[A]) extends Monad[A] {
    override def bindy[B](f: A => List[B]): List[B] = list.flatMap(f)
  }

  implicit def enMonadOption[A](opt: Option[A]) = MonadicOption(opt)

  implicit def enMonadList[A](list: List[A]) = MonadicList(list)

  val o: Monad[Int] = Some(4)

  val ob: Monad[Int] = o.bindy({ x: Int => Some(x) }) // TODO: disallow List(x)

  val l: Monad[Int] = List(1, 2, 3)

  val lb: Monad[Int] = l.bindy({ x: Int => List(x) }) // TODO: disallow Some(x)

}

*/

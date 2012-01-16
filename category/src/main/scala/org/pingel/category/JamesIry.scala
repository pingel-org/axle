

object Elephants {

/*
  trait Semigroup[+M[_]] {
    // TODO: append ?
  }

  trait Monoid[+M[_]] {

    def zero[A](): M[A]
  }

  trait Monad[+M[_]] extends Monoid[M] {

    def unit[A](a: A): M[A]
    def bind[A, B](m: M[A])(f: A => M[B]): M[B]

    def flatten2[A](m: M[M[A]]): M[A]

    // a simple alias:
    def flatMap[A, B](m: M[A])(f: A => M[B]): M[B] = bind(m)(f)
  }

  implicit object MonadicOption extends Monad[Option] {

    def unit[A](a: A) = Some(a)

    def bind[A, B](opt: Option[A])(f: A => Option[B]) = opt.flatMap(f)

    def flatten2[A](opt: Option[Option[A]]): Option[A] =
      opt match {
	case Some(inner) => inner
	case None => None
      }
  }

  implicit object MonadicList extends Monad[List] {

    def foo = "MonadicList is doing foo!!!"

    def unit[A](a: A) = List(a)

    def bind[A, B](list: List[A])(f: A => List[B]) = list.flatMap(f)
  }

  // defining a notion of "flatten" for our two container types:

  def flattenOption[A](outer: Option[Option[A]]): Option[A] = outer match {
    case Some(inner) => inner
    case None => None
  }

  def flattenList[A](outer: List[List[A]]): List[A] =
    outer.foldLeft(List[A]())({ (x: List[A], y: List[A]) => x ++ y })

  // Now let's say we have functions that convert string to int,
  // wrapped in an option

  def stringToIntOpt(string: String) : Option[Int] = string match {
    case "one" => Some(1)
    case "two" => Some(2)
    case _     => None
  }

  // and we want a version of flatten that take such a function as an argument:

  def flatMapOption[A, B](outer: Option[Option[A]],
			  f: A => Option[B]): Option[B] = outer match {
    case Some(Some(inner)) => f(inner)
    case _ => None
  }

  None.flatten2

  val x1 = Some[Option[String]](Some("two")).flatten2
  val x2 = Some[Option[String]](None).flatten2
  val x3 = None.flatten2

  val fmoss2 = flatMapOption( Some[Option[String]](Some("two")), stringToIntOpt )
  val fmos2 = flatMapOption( Some[Option[String]](None), stringToIntOpt )
  val fmon2 = flatMapOption( None, stringToIntOpt )

  // Note that we could have also made use of flatMapOpt the hard way (duplicating some of what flatMap was supposed to do for us):

  val naiveOpt =
    flatMapOption( Some[Option[String]](Some("two"))
		  .map({ _.map({ stringToIntOpt(_) }) }),
		  { (x: Option[Int]) => x }
		)

  // and do something parallel for List

  def stringToIntList(string: String) : List[Int] = string match {
    case "one" => List(1)
    case "two" => List(2)
    case _     => List[Int]()
  }

  def flatMapList[A, B](outer: List[List[A]], f: A => List[B]): List[B] =
    outer.foldLeft(List[B]())({
      (x: List[B], y: List[A]) => x ++ y.foldLeft(List[B]())( (bs: List[B], a: A) => { bs ++ f(a) })
    })

  val mm2bind = List(List("two")).bind(stringToIntList)

  val fmlmm2 = flatMapList( List(List("two")), stringToIntList )
  val fmlm2 = flatMapList( List[List[String]](Nil), stringToIntList )
  val fml2 = flatMapList( List[List[String]](), stringToIntList )

  // And once again, doing it the "hard way":

  val naiveList =
    flatMapList( List(List("two"))
		.map({ _.map({ stringToIntList(_) }) }),
		{ (x: List[Int]) => x } )

  // Let's try to factor out the commonalities between
  // Opt and List's respective flatMap implementations
  // making use of "unit" and "zero"

  // see above

  // more real-world example:

  object config {
    def fetchParam(name: String): Option[String] = Some("one")
  }

  val opString : Option[String] = config fetchParam "MaxThreads"

  val result: Option[Int] = opString.flatMap(stringToIntOpt)
*/

}

/*
object Older {

  case class MyList[A](elements: A*) extends M[A] {
  
    val xs: List[A] = elements.toList

    def foldLeft[B](zero: MyList[B])(f: (MyList[B], A) => MyList[B]) = elements.foldLeft(zero)({ (bs, a) => f(bs, a) })

    def map[B](f: A => B) = MyList[B](elements.map( a => f(a) ) : _*)

    def ++(other: MyList[A]) = MyList[A](xs ++ other.xs : _*)

    def ::(a: A) = MyList[A](a :: xs : _*) // TODO: wrong order

    def unit[T](value: T) = MyList[T](value)

    def zero[T]() = MyList[T]() // aka MyNil

    //  def flatMap[B](f: A => MyList[B]): MyList[B] = xs.foldLeft( zero[B]() )({
    //    (x: MyList[B], y: MyList[A]) => { x ++ y.foldLeft(zero[B]())( (bs: MyList[B], a: A) => { bs ++ f(a) }) } 
    //  })

  }

  case class MyNil[A]() extends MyList[A]() { }

  trait MyOption[A] extends M[A] {

    def map[B](f: A => B): MyOption[B]

    def unit[T](value: T) = MySome[T](value)

    def zero[T]() = MyNone[T]()
  }
  
  case class MyNone[A] extends MyOption[A] {

    def map[B](f: A => B) = MyNone() // of [B]

    def flatMap[B](f: A => MyOption[B]): MyOption[B] = zero()
  
  }

  case class MySome[A](value: A) extends MyOption[A] {

    def map[B](f: A => B) = MySome(f(value))

    def flatMap[B](f: A => MyOption[B]): MyOption[B] = value match {
      case MySome(inner: A) => f(inner)
      case _ => zero()
    }
  }

}
*/

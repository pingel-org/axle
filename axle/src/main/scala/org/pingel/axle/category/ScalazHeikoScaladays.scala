
import scalaz._
import Scalaz._

/*
object Equals {

	implicit def toEqual[A](a: A) = foo

}



object Ease {

	def main(): Unit = {
			typesafeEquals()
			println("successfully finished")
	}

	def typesafeEquals(): Unit = {

			val versionToYear = Map(
					"1.0" -> "2003",
					"1.0" -> "2003",
					"1.0" -> "2003")

					def isVersionFromYear(version: String, year: String) = 
						versionToYear get version map { _ === year } getOrElse false

						assert(isVersionFromYear("1.0", "2003"))
	}

	def effects(args: String *): Unit = {

		def arg(index: Int): Validation[NonEmptyList[String], Int] = 
			catching(classOf[IndexOutOfBoundsException], classOf[NumberFormatException]) either {
			  args(index).toInt
		    } match {
			  case Left(e: IndexOutOfBoundsException) => "%s. arg is missing".format(index).failNel
			  case Left(e: NumberFormatException) => "%s. arg is missing".format(index).failNel
			  case Right(i) => i.success
		    }

		def args23 = Seq(arg(2), arg(3))

		val args23seq = args23.sequence[ xxx  , B] // use type lambda
		    
		val result = (arg(0) |@| arg(1) |@| args23 ) { _ + _ + _.sum }
		print(result)
	}
}
*/

/*


== is not type safe

 */

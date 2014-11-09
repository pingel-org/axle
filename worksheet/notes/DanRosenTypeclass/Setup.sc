object Part1 {

  println("Hello, Typeclasses")                   //> Hello, Typeclasses

  // notes from Dan Rosen's "Scala Typeclasses" video
  //
  // http://www.youtube.com/watch?v=sVMES4RZF-8
  //

  object lib {

    sealed trait Expression
    case class Number(value: Int) extends Expression
    case class Plus(lhs: Expression, rhs: Expression) extends Expression
    case class Minus(lhs: Expression, rhs: Expression) extends Expression

    object ExpressionEvaluator {
      def value(expression: Expression): Int = expression match {
        case Number(value) => value
        case Plus(lhs, rhs) => value(lhs) + value(rhs)
        case Minus(lhs, rhs) => value(lhs) - value(rhs)
      }
    }

    sealed trait JsonValue
    case class JsonObject(entries: Map[String, JsonValue]) extends JsonValue
    case class JsonArray(entries: Seq[JsonValue]) extends JsonValue
    case class JsonString(value: String) extends JsonValue
    case class JsonNumber(value: BigDecimal) extends JsonValue
    case class JsonBoolean(value: Boolean) extends JsonValue
    case object JsonNull extends JsonValue

    object JsonWriter {
      def write(value: JsonValue): String = value match {
        case JsonObject(entries) => {
          val serializedEntries = for ((key, value) <- entries) yield key + ": " + write(value)
          "{ " + (serializedEntries mkString ", ") + " }"
        }
        case JsonArray(entries) => {
          val serializedEntries = entries map write
          "[ " + (serializedEntries mkString ", ") + " ]"
        }
        case JsonString(value) => "\"" + value + "\""
        case JsonNumber(value) => value.toString
        case JsonBoolean(value) => value.toString
        case JsonNull => "null"
      }

    }

    trait JsonConvertible {
      def convertToJson: JsonValue
    }

    def write(value: JsonConvertible): String = JsonWriter.write(value.convertToJson)

  }

  import lib._

  val expr: Expression = Plus(Number(1), Minus(Number(3), Number(2)))
                                                  //> expr  : Part1.lib.Expression = Plus(Number(1),Minus(Number(3),Number(2)))

  // making this would would require having Expression implement JsonConvertible
  // write(expr)

}
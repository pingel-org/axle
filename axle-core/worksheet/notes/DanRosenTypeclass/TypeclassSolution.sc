
object TypeclassSolution {

  println("Typeclass Solution")                   //> Typeclass Solution

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

    trait JsonConverter[A] {
      def convertToJson(value: A): JsonValue
    }
    
    object JsonConverter {
    
      implicit val expressionJsonConverter = new JsonConverter[Expression] {
      
        def convertToJson(expr: Expression): JsonValue = expr match {
        
          case Number(value) => JsonNumber(value)
          
          case Plus(lhs, rhs) => JsonObject(Map("op" -> JsonString("+"), "lhs" -> convertToJson(lhs), "rhs" -> convertToJson(rhs)))

          case Minus(lhs, rhs) => JsonObject(Map("op" -> JsonString("-"), "lhs" -> convertToJson(lhs), "rhs" -> convertToJson(rhs)))
        
        }
      
      }
    
    }

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

      def write[A: JsonConverter](value: A): String = {
        val conv = implicitly[JsonConverter[A]]
        write(conv.convertToJson(value))
      }

    }

  }

  import lib._

  val expr: Expression = Plus(Number(1), Minus(Number(3), Number(2)))
                                                  //> expr  : TypeclassSolution.lib.Expression = Plus(Number(1),Minus(Number(3),N
                                                  //| umber(2)))

  JsonWriter.write(expr)                          //> res0: String = { op: "+", lhs: 1, rhs: { op: "-", lhs: 3, rhs: 2 } }

}
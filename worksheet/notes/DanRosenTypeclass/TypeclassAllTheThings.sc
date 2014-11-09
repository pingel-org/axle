object TypeclassAllTheThings {

  println("Typeclass all the things")             //> Typeclass all the things

  object lib {

    trait Expression[A] {
      def value(expr: A): Int
    }

    object Expression {

      implicit val intExpr = new Expression[Int] { def value(n: Int): Int = n }

      implicit def pairExpr[T1: Expression, T2: Expression] =
        new Expression[(T1, T2)] {
          def value(pair: (T1, T2)): Int =
            implicitly[Expression[T1]].value(pair._1) + implicitly[Expression[T2]].value(pair._2)
        }

    }
    
    object ExpressionEvaluator {
      def evaluate[A: Expression](expr: A): Int = implicitly[Expression[A]].value(expr)
    }

    sealed trait JsonValue
    case class JsonObject(entries: Map[String, JsonValue]) extends JsonValue
    case class JsonArray(entries: Seq[JsonValue]) extends JsonValue
    case class JsonString(value: String) extends JsonValue
    case class JsonNumber(value: BigDecimal) extends JsonValue
    case class JsonBoolean(value: Boolean) extends JsonValue
    case object JsonNull extends JsonValue

    trait Json[A] {
      def json(value: A): JsonValue
    }

    object Json {

      implicit val intJson = new Json[Int] { def json(n: Int): JsonValue = JsonNumber(n) }

      implicit def pairJson[T1: Json, T2: Json] =
        new Json[(T1, T2)] {
          def json(pair: (T1, T2)): JsonValue = JsonObject(Map(
            "fst" -> implicitly[Json[T1]].json(pair._1),
            "snd" -> implicitly[Json[T2]].json(pair._2)))
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

      def write[A: Json](value: A): String = {
        val conv = implicitly[Json[A]]
        write(conv.json(value))
      }

    }

  }

  import lib._

  val foo = (1, (2, 3))                           //> foo  : (Int, (Int, Int)) = (1,(2,3))

  JsonWriter.write(foo)                           //> res0: String = { fst: 1, snd: { fst: 2, snd: 3 } }

  ExpressionEvaluator.evaluate(foo)               //> res1: Int = 6

}

package axle.ast

import collection._

abstract class AstNode(lineno: Int) {
  def getLineNo: Int = lineno
  def column: Int = -1
}

case class AstNodeValue(value: Option[String], lineno: Int)
  extends AstNode(lineno) {
}

case class AstNodeList(list: List[AstNode], lineno: Int)
  extends AstNode(lineno) {
  def children = list
}

case class AstNodeRule(val ruleName: String, mm: Map[String, AstNode], lineno: Int)
  extends AstNode(lineno) {
  def children = mm.values.toList
}

object AstNode {

  import net.liftweb.json.JsonParser
  import net.liftweb.json.JsonAST._
  import net.liftweb.common.Full

  def fromJson(json: String, parentLineNo: Int = 1): AstNode = _fromJson(JsonParser.parse(json), parentLineNo)

  def _fromJson(json: JValue, parentLineNo: Int): AstNode = json match {

    case JObject(fields: List[JField]) => {

      var lineno = parentLineNo
      val meta_map = mutable.Map[String, AstNode]()
      var meta_type = ""

      // TODO: use "find":
      fields.map(field =>
        field match {
          case JField("_lineno", JInt(i)) => lineno = i.intValue()
          case _ => {}
        }
      )

      fields.map(field =>
        field match {
          case JField("_lineno", _) => {}
          case JField("type", JString(s)) => meta_type = s
          case _ => meta_map += field.name -> _fromJson(field.value, lineno)
        }
      )

      AstNodeRule(meta_type, meta_map, lineno)
    }

    case JArray(jvals: List[JValue]) => AstNodeList(jvals.map(_fromJson(_, parentLineNo)), parentLineNo)

    case JNull => AstNodeValue(None, parentLineNo)

    case JNothing => AstNodeValue(None, parentLineNo) // verify this

    case JInt(i) => AstNodeValue(Some(i.toString), parentLineNo)

    case JDouble(d) => AstNodeValue(Some(d.toString), parentLineNo)

    case JString(s) => AstNodeValue(Some(s.toString), parentLineNo)

    case JBool(b) => AstNodeValue(Some(b.toString), parentLineNo)

    case z => throw new Exception("unable to fromJson: " + z)

  }

}

// List.fromIterator(map.values)

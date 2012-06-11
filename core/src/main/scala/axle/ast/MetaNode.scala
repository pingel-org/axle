
package axle.ast

abstract class MetaNode(lineno: Int) {
  def getLineNo: Int = lineno
  def column: Int = -1
}

case class MetaNodeValue(value: Option[String], lineno: Int)
  extends MetaNode(lineno) {
}

case class MetaNodeList(list: List[MetaNode], lineno: Int)
  extends MetaNode(lineno) {
  def children = list
}

case class MetaNodeRule(val ruleName: String, mm: Map[String, MetaNode], lineno: Int)
  extends MetaNode(lineno) {
  def children = mm.values.toList
}

object MetaNode {

  import net.liftweb.json.JsonParser
  import net.liftweb.json.JsonAST._
  import net.liftweb.common.Full

  def fromJson(json: String, parentLineNo: Int = 1): MetaNode = _fromJson(JsonParser.parse(json), parentLineNo)

  def _fromJson(json: JValue, parentLineNo: Int): MetaNode = json match {

    case JObject(fields: List[JField]) => {

      var lineno: Int = parentLineNo
      var meta_map: Map[String, MetaNode] = Map()
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

      MetaNodeRule(meta_type, meta_map, lineno)
    }

    case JArray(jvals: List[JValue]) => MetaNodeList(jvals.map(_fromJson(_, parentLineNo)), parentLineNo)

    case JNull => MetaNodeValue(None, parentLineNo)

    case JNothing => MetaNodeValue(None, parentLineNo) // verify this

    case JInt(i) => MetaNodeValue(Some(i.toString), parentLineNo)

    case JDouble(d) => MetaNodeValue(Some(d.toString), parentLineNo)

    case JString(s) => MetaNodeValue(Some(s.toString), parentLineNo)

    case JBool(b) => MetaNodeValue(Some(b.toString), parentLineNo)

    case z => throw new Exception("unable to fromJson: " + z)

  }

}

// List.fromIterator(map.values)

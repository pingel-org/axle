
package axle.ast

object JsonAST {

  import net.liftweb.json.JsonParser
  import net.liftweb.json.JsonAST._
  import net.liftweb.common.Full

  def fromJson(json: String, parentLineNo: Int = 1): AstNode = _fromJson(JsonParser.parse(json), parentLineNo)

  def _fromJson(json: JValue, parentLineNo: Int): AstNode = json match {

    case JObject(fields: List[JField]) => {

      val lineNo = fields.view.map((field: JField) => field match {
        case JField("_lineno", JInt(i)) => Some(i.intValue)
        case _ => None
      }).find(_.isDefined).flatMap(x => x).getOrElse(parentLineNo) // yuck

      val metaType = fields.view.map((field: JField) => field match {
        case JField("type", JString(s)) => Some(s)
        case _ => None
      }).find(_.isDefined).flatMap(x => x).getOrElse("") // yuck

      val metaMap = fields
        .filter(field => (field.name != "type") && (field.name != "_lineno"))
        .map(field => field.name -> _fromJson(field.value, lineNo))
        .toMap

      AstNodeRule(metaType, metaMap, lineNo)
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

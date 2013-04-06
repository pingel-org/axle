
package axle.ast

import scala.collection.mutable.Buffer

object Jackson {

  import java.lang.reflect.{ Type, ParameterizedType }
  import com.fasterxml.jackson.core.`type`.TypeReference
  import com.fasterxml.jackson.module.scala._
  import com.fasterxml.jackson.databind._

  val mapTypeReference = typeReference[Map[String, AnyRef]]

  private[this] def typeReference[T: Manifest] = new TypeReference[T] {
    override def getType = typeFromManifest(manifest[T])
  }

  private[this] def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) m.runtimeClass
    else new ParameterizedType {
      def getRawType = m.runtimeClass

      def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

      def getOwnerType = null
    }
  }

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  def parseJsonMap(s: String): Map[String, AnyRef] = mapper.readValue(s, mapTypeReference)

}

object JsonAST {

  import Jackson.parseJsonMap

  def fromJson(json: String, parentLineNo: Int = 1): AstNode = obj2ast(parseJsonMap(json), parentLineNo)

  def mapToRuleNode(m: Map[String, Any], parentLineNo: Int) = {

    val lineNo =
      m.get("_lineno").flatMap(Option(_)).asInstanceOf[Option[Int]]
        .getOrElse(parentLineNo)

    val metaMap = m.keySet.filter(field => (field != "type") && (field != "_lineno")).toList
      .map(field => field -> obj2ast(m(field), lineNo))
      .toMap

    AstNodeRule(
      m.get("type").asInstanceOf[Option[String]].getOrElse(""),
      metaMap,
      lineNo
    )
  }

  def obj2ast(obj: Any, parentLineNo: Int): AstNode = obj match {

    case x: Map[_, _] =>
      mapToRuleNode(x.asInstanceOf[Map[String, Any]], parentLineNo)

    case x: collection.convert.Wrappers$JMapWrapper =>
      mapToRuleNode(x.toMap.asInstanceOf[Map[String, Any]], parentLineNo)

    case arr: Array[_] => AstNodeList(arr.map(obj2ast(_, parentLineNo)).toList, parentLineNo)

    case buff: Buffer[_] => AstNodeList(buff.map(obj2ast(_, parentLineNo)).toList, parentLineNo)

    case i: Int => AstNodeValue(Some(i.toString), parentLineNo)

    case d: Double => AstNodeValue(Some(d.toString), parentLineNo)

    case s: String => AstNodeValue(Some(s.toString), parentLineNo)

    case b: Boolean => AstNodeValue(Some(b.toString), parentLineNo)

    case None => AstNodeValue(None, parentLineNo) // verify this

    case null => AstNodeValue(None, parentLineNo)

    case z @ _ => throw new Exception("unable to fromJson: " + z)

  }

}

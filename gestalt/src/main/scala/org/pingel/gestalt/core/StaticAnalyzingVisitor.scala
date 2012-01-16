
package org.pingel.gestalt.core

import scala.collection._
import org.pingel.gestalt.parser._
import org.pingel.gestalt.parser.visitor.ObjectDepthFirst

class StaticAnalyzingVisitor(lexicon: Lexicon) extends ObjectDepthFirst {

  var includes = new mutable.ListBuffer[String]()

  def getIncludes() = includes

  /**
   * f0 -> ( Statement() )*
   * f1 -> <EOF>
   */
  override def visit(n: syntaxtree.Goal, argu: Object): Object = { // done

    n.f0.accept(this, null);

    return null;
  }

  /**
   * f0 -> Transform()
   *       | GSystem()
   *       | Form()
   *       | Include()
   */
  override def visit(n: syntaxtree.Statement, argu: Object): Object = { // done

    n.f0.accept(this, null)

    return null
  }

  /**
   * f0 -> "include"
   * f1 -> Identifier()
   */
  override def visit(n: syntaxtree.Include, argu: Object): Object = {

    includes += n.f1.f0.toString()

    return null;
  }

  /**
   * f0 -> "rule"
   * f1 -> Identifier()
   * f2 -> Identifier()
   * f3 -> Identifier()
   * f4 -> "{"
   * f5 -> ( Substitution() )*
   * f6 -> "}"
   * f7 -> ( "$" Identifier() )?
   */
  override def visit(n: syntaxtree.Transform, argu: Object): Object = {

    var ruleName = new Name(n.f1.f0.toString())
    var inName = new Name(n.f2.f0.toString())
    var outName = new Name(n.f3.f0.toString())

    var map = Map[Name, Name]() // Note: was TreeMap
    n.f5.accept(this, map)

    var cost = 0.0
    if (n.f7.present) {
      val ns = n.f7.node.asInstanceOf[syntaxtree.NodeSequence]
      val costIdNode = ns.elementAt(1).asInstanceOf[syntaxtree.Identifier]
      cost = costIdNode.f0.toString.toDouble
    }

    var r = new SimpleTransform(inName, outName, map.toMap, cost) // note map conversion to immutable

    lexicon.put(ruleName, r)

    return null
  }

  /**
   * f0 -> Identifier()
   * f1 -> "/"
   * f2 -> Identifier()
   */
  override def visit(n: syntaxtree.Substitution, argu: Object): Object = {

    var map = argu.asInstanceOf[Map[Name, Name]]

    val from = new Name(n.f0.f0.toString())
    val to = new Name(n.f2.f0.toString())

    GLogger.global.fine("adding " + from + "/" + to + " to substitution map")

    map += from -> to

    null
  }

  /**
   * f0 -> "system"
   * f1 -> Identifier()
   * f2 -> Identifier()
   * f3 -> "{"
   * f4 -> ( Application() )*
   * f5 -> "}"
   * f6 -> "<"
   * f7 -> ( Identifier() )*
   * f8 -> ">"
   */
  override def visit(n: syntaxtree.GSystem, argu: Object): Object = {

    val name = Name(n.f1.f0.toString())

    val guardName = new Name(n.f2.f0.toString())

    val t = new ComplexTransform(guardName)

    //	StringLabeller labeller = GlobalStringLabeller.getLabeller(t);

    var name2node = Map[Name, TransformVertex]() // Note: was TreeMap

    val argDouble = (t, name2node)
    n.f4.accept(this, argDouble)

    var startNode = name2node(Name("in"))
    startNode.setIsStart(true)
    t.start = startNode

    if (startNode == null) {
      GLogger.global.severe("no input defined")
      System.exit(1)
    }

    var exitNames = Set[Name]()
    n.f7.accept(this, exitNames)

    var exitNodes = Set[TransformVertex]()

    var exit_it = exitNames.iterator
    while (exit_it.hasNext) {

      val eName = exit_it.next()
      val eNode = name2node(eName)
      eNode.setIsExit(true)

      exitNodes += eNode
      GLogger.global.info("marking " + eName.toString() + " as exit")

    }

    GLogger.global.info("registering transform " + name)

    lexicon.put(name, t)

    GLogger.global.info("details:\n" + t.toString())

    return null
  }

  /**
   * f0 -> "apply"
   * f1 -> Identifier()
   * f2 -> Identifier()
   * f3 -> ( Path() )?
   * f4 -> Identifier()
   */
  override def visit(n: syntaxtree.Application, argu: Object): Object = {

    val argDouble = argu.asInstanceOf[Tuple2[ComplexTransform, Map[Name, TransformVertex]]]
    val t = argDouble._1
    var name2node = argDouble._2

    val transformName = new Name(n.f1.f0.toString())
    val inNodeName = new Name(n.f2.f0.toString())

    var inNode = name2node(inNodeName)
    if (inNode == null) {
      inNode = new TransformVertex(inNodeName, false, false)
      t.getGraph().addVertex(inNode)
      name2node += inNodeName -> inNode
    }

    var traversal: Traversal = null
    if (n.f3.present()) {
      traversal = n.f3.accept(this, null).asInstanceOf[Traversal]
    }

    val outNodeName = new Name(n.f4.f0.toString())

    var outNode = name2node(outNodeName)
    if (outNode == null) {
      outNode = new TransformVertex(outNodeName, false, false)
      t.getGraph().addVertex(outNode)
      name2node += outNodeName -> outNode
    }

    val arc = new TransformEdge(transformName, traversal, inNode, outNode)
    t.getGraph().addEdge(arc)

    return null
  }

  /**
   * f0 -> "firm"
   * f1 -> Identifier()
   * f2 -> "["
   * f3 -> ( Identifier() )*
   * f4 -> "]"
   * f5 -> Expression()
   */
  override def visit(n: syntaxtree.Form, argu: Object): Object = { // done

    val name = new Name(n.f1.f0.toString())

    var scopedVars = Set()
    n.f3.accept(this, scopedVars)

    GLogger.global.info("scoped vars for form " + name + " are " + scopedVars)

    val f = n.f5.accept(this, scopedVars).asInstanceOf[Form]

    lexicon.put(name, f)

    return null
  }

  /**
   * f0 -> Identifier()
   *       | "(" Expression() Expression() ")"
   */
  override def visit(n: syntaxtree.Expression, argu: Object): Object = { // done

    var scopedVars = argu.asInstanceOf[Set[Name]]

    val top = new Name("top")

    val lambda = new Lambda()
    if (scopedVars != null) {
      for (name <- scopedVars) {
        lambda.add(name, top)
      }
    }

    if (n.f0.which == 0) {
      val id = n.f0.choice.asInstanceOf[syntaxtree.Identifier]
      val aname = new org.pingel.gestalt.core.Name(id.f0.toString())
      return new SimpleForm(aname, lambda)
    } else {
      val ns = n.f0.choice.asInstanceOf[syntaxtree.NodeSequence]
      val s1 = ns.elementAt(1).accept(this, null).asInstanceOf[Form]
      val s2 = ns.elementAt(2).accept(this, null).asInstanceOf[Form]
      return new ComplexForm(s1, s2, lambda)
    }

  }

  /**
   * f0 -> <PATH>
   */
  override def visit(n: syntaxtree.Path, argu: Object): Object = { // done
    val pathString = n.f0.toString().substring(1)
    return new Traversal(pathString)
  }

  /**
   * f0 -> <IDENTIFIER>
   */
  override def visit(n: syntaxtree.Identifier, argu: Object): Object = { // done

    if (argu != null) {
      var s = argu.asInstanceOf[Set[Name]]
      s += new Name(n.f0.toString())
    }

    return null
  }

}

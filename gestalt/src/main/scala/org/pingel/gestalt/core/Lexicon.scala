package org.pingel.gestalt.core;

import org.pingel.axle.util.Printable

import scala.collection._

case class Lexicon {
  var name2object = mutable.Map[Name, Logos]()
  var object2name = mutable.Map[Logos, Name]() // this may be buggy

  var formFactories = mutable.Set[FormFactory[Form]]()
  var transformFactories = mutable.Set[TransformFactory]()

  def put(name: Name, o: Logos): Unit = {
    GLogger.global.info("putting " + name + " of class " + o.getClass() + " = " + o.toString());

    /*
    	 Situation already = (Situation) name2object.get(name.base);
    	 if( already != null ) {
    	 System.out.println("addSymbol: there is already a node with base name " + name.base);
    	 System.exit(1);
    	 }
    	 */

    name2object += name -> o
    object2name += o -> name
  }

  def addFactories(): Unit = {
    for (form <- getTopForms()) {
      formFactories.add(new DynamicFormFactory(form))
    }

    for (t <- getTransforms()) {
      transformFactories.add(new TransformFactory(t, this))
    }

    val leftBlank = new Blank()
    val rightBlank = new Blank()
    val cf = new ComplexForm(leftBlank, rightBlank)
    leftBlank.setParent(cf)
    rightBlank.setParent(cf)
    formFactories.add(new DynamicFormFactory(cf))
  }

  def addFormFactory(ff: FormFactory[Form]): Unit = {
    formFactories.add(ff)
  }

  def getFormFactories() = formFactories

  def addTransformFactory(tf: TransformFactory): Unit = {
    transformFactories.add(tf)
  }

  def getTransformFactories() = transformFactories

  def getNames() = name2object.keySet

  def get(name: Name) = name2object(name)

  def getTransform(name: Name): Transform = {
    GLogger.global.info("getTransform(" + name + ")")
    name2object(name).asInstanceOf[Transform]
  }

  def renameTransform(from: Name, to: Name): Unit = {
    val t = name2object(from)
    name2object.remove(from)
    object2name.remove(t)
    name2object += to -> t
    object2name += t -> to
  }

  def renameForm(from: Name, to: Name): Unit = {
    val f = name2object(from)
    name2object.remove(from)
    object2name.remove(f)
    name2object += to -> f
    object2name += f -> to
  }

  def getForm(name: Name): Form = {
    GLogger.global.info("getForm(" + name + ")")
    name2object(name).asInstanceOf[Form]
  }

  def remove(logos: Logos): Unit = {
    val name = object2name(logos)
    if (name != null) {
      object2name.remove(logos)
      name2object.remove(name)
    }
  }

  def getTransforms() = {
    var result = Set[Transform]()
    for (logos <- object2name.keySet) {
      logos match {
        case t: Transform => {
          result += t
        }
        case _ => {}
      }
    }
    result
  }

  def getTopForms() = {
    var result = Set[Form]()
    for (logos <- object2name.keySet) {
      logos match {
        case f: Form => result += f
        case _ => {}
      }
    }
    result
  }

  def getNameOf(logos: Logos) = object2name(logos)

  def printToStream(p: Printable): Unit = {
    for (name <- name2object.keySet) {
      val logos = get(name)
      logos.printToStream(name, p)
      p.println()
    }
  }

}

package org.pingel.gestalt.core

import scala.collection._

case class Unifier {

    var name2form = Map[Name, Form]()

    def get(name: Name) = name2form(name)

    def put(name: Name, f: Form) = name2form += name -> f

    def toString() = "{\n" +
    		name2form.keySet.map( n => n + " -> " + name2form(n).toString()).mkString("\n") +
    		" }"

}

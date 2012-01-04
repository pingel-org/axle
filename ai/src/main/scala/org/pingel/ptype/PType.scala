package org.pingel.ptype

import java.util.TreeMap;

import org.pingel.gestalt.core.Form

import scala.collection._

class PType {

    var values = mutable.ListBuffer[Form]()
    
    var values2index = new TreeMap[Form,Integer]()

    def getValues() = values

    def addValue(value: Form) = {
        val index = values.size
        values += value
        values2index.put(value, new Integer(index))
    }
    
    def indexOf(value: Form): Int = values2index.get(value).intValue

}

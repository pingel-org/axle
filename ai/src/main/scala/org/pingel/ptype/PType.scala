package org.pingel.ptype

import java.util.TreeMap;

import org.pingel.gestalt.core.Form

class PType {

    var values = List[Form]()
    
    var values2index = new TreeMap[Form,Integer]()

    def getValues() = values

    def addValue(value: Form) = {
        val index = values.size
        values.add(value)
        values2index.put(value, new Integer(index))
    }
    
    def indexOf(value: Form): Int = values2index.get(value).intValue

}

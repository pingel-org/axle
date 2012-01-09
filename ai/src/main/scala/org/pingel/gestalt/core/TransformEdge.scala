package org.pingel.gestalt.core

import org.pingel.util.LabelledDirectedEdge

case class TransformEdge(transformName: Name, traversal: Traversal, source: TransformVertex, dest: TransformVertex)
extends LabelledDirectedEdge[TransformVertex]
{
    def getSource() = source
    
    def getDest() = dest
    
    def getLabel() = transformName.base
    
    def toString(): String = {
      (traversal == null) match {
        case true  => "apply " + transformName + " " + source.toString() + " " + dest.toString()
        case false => "apply " + transformName + " " + source.toString() + "." + traversal + " " + dest.toString()
      }
    }
    
}

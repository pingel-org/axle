package org.pingel.gestalt.core;

import java.awt.Font
import java.awt.Graphics
import java.awt.Point

import org.pingel.axle.util.Printable

case class SimpleForm(name: Name, lambda: Lambda=new Lambda())
extends Form(lambda)
{
	GLogger.global.entering("SimpleSituation", "<init>: " + name)

	def size() = 1

	def compareTo(other: Form): Int = other match {
	  case sf: SimpleForm  => name.compareTo(sf.name)
	  case cf: ComplexForm => -1
	  case _               => -1 // or throw exception ???
	}
	
	def equals(other: Form): Boolean = other match {
	  case sf: SimpleForm => name.equals(sf.name)
	  case _ => false
	}
	
	def unify(freeLambda: Lambda, target: Form, unifier: Unifier): Boolean = {
		// Note: It may be more natural to merge the role of the unifier
		// and the "free" set.  Perhaps "free" could be represented as the
		// keys in unifier.  And unbound free variables will map to null
		// until they are bound.
		
		GLogger.global.entering("SimpleForm", "unify: this = " +
				this.toString() + ", target = " + target.toString())

		if( freeLambda != null && freeLambda.contains(this.name) ) {
			GLogger.global.fine(name + " is free")
			val already = unifier.get(name)
			if( already == null ) {
				GLogger.global.fine("binding " + name + " to " + target)
				unifier.put(name, target)
				return true
			}
			else {
				GLogger.global.fine(name + " already bound to " + already)
				return target.equals(already)
			}
		}
		else {
			GLogger.global.fine(name + " is not free. ")
			val e = equals(target)
			GLogger.global.fine("match will return " + e)
			return e
		}
	}
	
	def duplicate() = new SimpleForm(name, lambda) // TODO clone lambda if not null

	def duplicateAndReplace(replacements: Map[Name, Form]): Form = {
		val replacement = replacements(name)
		// println("this.name = " + this.name + ", replacement = " + replacement);
		if( replacement != null ) {
			return replacement.duplicate()
		}
		else {
			return duplicate()
		}
	}
	
	def _traverse(traversal: Traversal, i: Int): Form = {
		if( i == traversal.length() ) {
			return this
		}
		else {
			println("malformed traversal")
			System.exit(1)
			return null
		}
	}
	
	def _duplicateAndEmbed(tr: Traversal, i: Int, s: Form): Form = {
		if( i == tr.length() ) {
			return s
		}
		else {
			println("malformed traversal")
			System.exit(1)
			return null
		}
	}

    def memberIntersects(other: Form): Form = {
        if( this.intersects(other) ) {
            return this
        }
        else {
            return null
        }
    }
    
    def memberContains(p: Point): Form = {
        if( this.contains(p) ) {
            return this
        }
        else {
            return null
        }
    }

    def arrange(p: Point): Unit = {
      move(p)
    }

    def getBounds() = center

    def toString() = name.base

    def printToStream(name: Name, out: Printable): Unit = {
    	super.printToStream(name, out)
    	out.print(this.name.toString())
    }
    
    val font = new Font("TimesRoman", Font.BOLD, 24)

    def paint(g: Graphics): Unit = {
        super.paint(g)
        g.setFont(font)
        g.drawString(name.base, center.x - 5, center.y + 10)
    }

}

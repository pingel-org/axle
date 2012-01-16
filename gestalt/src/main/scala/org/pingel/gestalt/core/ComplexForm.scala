package org.pingel.gestalt.core

import java.awt.Color
import java.awt.Graphics
import java.awt.Point

import org.pingel.axle.util.Printable

case class ComplexForm(
    var left: Form,
    var right: Form, 
    override val lambda: Lambda=new Lambda())
extends Form(lambda) {
  
	var _size = -1

    def getLeft() = left
    
    def getRight() = right

    def setLeft(left: Form): Unit = {
        this.left = left
    }

    def setRight(right: Form): Unit = {
        this.right = right
    }
    
    def size(): Int = {
        if( _size == -1 ) {
            _size = 1 + left.size() + right.size()
        }
        _size
    }

    def compareTo(other: Form): Int = other match {
      case sf: SimpleForm => 1
      case cf: ComplexForm => left.compareTo(cf.left) match {
        case 0 => right.compareTo(cf.right)
        case lc @ _ => lc
      }
      case _ => -1 // or throw "incomparable" exception
    }
    

    def equals(other: Form) = other match {
        case null => false
        case otherCF: ComplexForm => (this == other) || ( left.equals(otherCF.getLeft()) && right.equals(otherCF.getRight()) )
        case _ => false
    }

    def unify(freeLambda: Lambda, target: Form, result: Unifier): Boolean = {
		GLogger.global.entering("ComplexSituation", "unify: this: " + this.toString() + ", unify: target = " +
					target.toString() + ", result = " + result.toString())
		target match {
		  case cf: ComplexForm => {
			if( lambda != null ) {
				freeLambda.addAll(lambda)
			}
		    (left.unify(freeLambda, cf.getLeft(), result) && right.unify(freeLambda, cf.getRight(), result) )
		  }
		  case _ => false
		}
    }

    def duplicate(): Form = {
        // TODO clone lambda if not null
        val leftDup = left.duplicate()
        val rightDup = right.duplicate()
        val dup = new ComplexForm(leftDup, rightDup, lambda)
        leftDup.setParent(dup)
        rightDup.setParent(dup)
        dup
    }

    def duplicateAndReplace(replacements: Map[Name, Form]) = 
      new ComplexForm(
          left.duplicateAndReplace(replacements),
          right.duplicateAndReplace(replacements),
          lambda) // TODO clone lambda if not null

    def _traverse(traversal: Traversal, i: Int): Form = {
    	if( i == traversal.length() ) {
    		return this
    	}
    	else if( traversal.elementAt(i) == 'r' ) {
    		return right._traverse(traversal, i+1)
    	}
    	else if ( traversal.elementAt(i) == 'l' ) {
    		return left._traverse(traversal, i+1)
    	}
    	else {
    		println("malformed traversal")
    		System.exit(1)
    		return null
    	}
    }
    
    def _duplicateAndEmbed(traversal: Traversal, i: Int, s: Form): Form = {
    	if( i == traversal.length() ) {
    		return s
    	}
    	else if ( traversal.elementAt(i) == 'r' ) {
    		return new ComplexForm(left.duplicate(), right._duplicateAndEmbed(traversal, i+1, s), lambda) // TODO clone lambda if not null
    	}
    	else if ( traversal.elementAt(i) == 'l' ) {
    		return new ComplexForm(left._duplicateAndEmbed(traversal, i+1, s), right.duplicate(), lambda) // TODO clone lambda if not null
    	}
    	else {
    		println("malformed traversal")
    		System.exit(1)
    		return null
    	}
    }
    
    override def printToStream(name: Name, out: Printable): Unit = {
    	super.printToStream(name, out)
    	out.print("(")
    	if( left == null ) {
    		out.print("?")
    	}
    	else {
    		left.printToStream(null, out)
    	}
    	out.print(" ")
    	if( right == null ) {
    		out.print("?")
    	}
    	else {
    		right.printToStream(null, out)
    	}
    	out.print(")")
    }


    override def toString() = "(" + left.toString() + " " + right.toString() + ")"

    def getLabel() = ""

    def arrange(p: Point): Unit = {
        move(p)
        val lp = new Point(p)
        lp.translate(-50, 50)
        left.arrange(lp)
        val rp = new Point(p)
        rp.translate(50, 50)
        right.arrange(rp)
    }

    
    override def move(p: Point): Unit = {
        // TODO the vector from parent to child won't change if the parent is the one that's doing the moving
        val p2l = new Point(left.center.x - center.x, left.center.y - center.y)
        val p2r = new Point(right.center.x - center.x, right.center.y - center.y)
        val old2new = new Point(p.x - center.x, p.y - center.y)
        val lc = new Point(center)
        lc.translate(p2l.x, p2l.y)
        lc.translate(old2new.x, old2new.y)
        val rc = new Point(center)
        rc.translate(p2r.x, p2r.y)
        rc.translate(old2new.x, old2new.y)
        super.move(p)
        left.move(lc)
        right.move(rc)
    }
    
    override def setDetachable(b: Boolean): Unit = {
        super.setDetachable(b)
        left.setDetachable(b)
        right.setDetachable(b)
    }

    override def setHighlighted(h: Boolean): Unit = {
        left.setHighlighted(h)
        right.setHighlighted(h)
        highlighted = h
    }

    def getLeftXOffset() = left.center.x - center.x

    def getLeftYOffset() = left.center.y - center.y

    def getRightXOffset() = right.center.x - center.x

    def getRightYOffset() = right.center.y - center.y

    def memberIntersects(other: Form): Form = {
        if( this.intersects(other) ) {
            return this
        }
        else {
            if( center.y <= other.center.y ) {
                if( center.x > other.center.x ) {
                    return left.memberIntersects(other)
                }
                else {
                    return right.memberIntersects(other)
                }
            }
            else {
                return null
            }
        }
    }
    
    def memberContains(p: Point): Form = {
        if( this.contains(p) ) {
            return this
        }
        else {
            if( center.y <= p.y ) {
                if( center.x > p.x ) {
                    return left.memberContains(p)
                }
                else {
                    return right.memberContains(p)
                }
            }
            else {
                return null
            }
        }
    }

    
    def getBounds() = {
        val leftBounds = left.getBounds()
        val rightBounds = right.getBounds()
        new Point(Math.max(leftBounds.x, rightBounds.x), Math.max(leftBounds.y, rightBounds.y))
    }

    
    override def paint(g: Graphics): Unit = {
        g.setColor(Color.BLACK)
        g.drawLine(center.x, center.y, left.center.x, left.center.y)
        g.setColor(Color.BLACK)
        g.drawLine(center.x, center.y, right.center.x, right.center.y)
        super.paint(g)
        left.paint(g)
        right.paint(g)
    }

}

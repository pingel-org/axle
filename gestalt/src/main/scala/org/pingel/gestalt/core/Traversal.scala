package org.pingel.gestalt.core

case class Traversal(var offset: String)
{
  var chars = offset.toList

  def toString = offset

  def copy() = new Traversal(offset)

  def append(tail: String): Unit = {
    offset += tail
    chars = offset.toList
  }

  def length() = chars.length

  def elementAt(i: Int) = chars(i)

}

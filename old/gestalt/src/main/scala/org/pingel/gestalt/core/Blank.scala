package org.pingel.gestalt.core

import java.awt.Color

case class Blank extends SimpleForm(new Name("?")) {

  color = new Color(100, 200, 240)

  override def duplicate() = new Blank()

}

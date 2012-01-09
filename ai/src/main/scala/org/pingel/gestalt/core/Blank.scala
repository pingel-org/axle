package org.pingel.gestalt.core

import java.awt.Color

case class Blank extends SimpleForm(new Name("?")) {

	color = new Color(100, 200, 240)

	def duplicate(): Form = new Blank()

}

package axle.visualize.element

import axle.visualize.Color
import axle.visualize.Point2D
import axle.visualize.ScaledArea2D

/**
 * For SVG, the rendering of hoverText and link rely on the id also being present
 * These few fields will likely be factored out into their own trait eventually.
 * 
 */

case class Rectangle[X, Y](
  scaledArea: ScaledArea2D[X, Y],
  lowerLeft: Point2D[X, Y],
  upperRight: Point2D[X, Y],
  fillColor: Option[Color] = None,
  borderColor: Option[Color] = None,
  id: Option[String] = None,
  hoverText: Option[String] = None,
  link: Option[(java.net.URL, Color)] = None // Color is the fill color when mouse over
  )
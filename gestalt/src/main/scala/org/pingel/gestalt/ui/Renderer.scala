package org.pingel.gestalt.ui;

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.GridLayout
import java.awt.Point

import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane

import org.pingel.gestalt.core.CallGraph
import org.pingel.gestalt.core.Form
import org.pingel.gestalt.core.FormFactory
import org.pingel.gestalt.core.History
import org.pingel.gestalt.core.Lexicon
import org.pingel.gestalt.core.Transform
import org.pingel.gestalt.core.TransformFactory

case class Renderer(controller: Controller, history: History, factoryLexicon: Lexicon, lexicon: Lexicon)
  extends JPanel {
  var area = new Dimension(0, 0)

  val bigFont = new Font("TimesRoman", Font.BOLD, 24)

  var highlighted: Widget = null

  setBackground(Color.LIGHT_GRAY)
  addMouseListener(controller)
  addMouseMotionListener(controller)

  val selectionLabel = new JLabel()
  selectionLabel.setFont(bigFont)
  val selectionPanel = new JPanel(new GridLayout(0, 1))
  selectionPanel.add(selectionLabel)

  val scroller = new JScrollPane(this)
  //scroller.setPreferredSize(area)

  controller.add(selectionPanel, BorderLayout.PAGE_START)
  controller.add(scroller, BorderLayout.CENTER)

  val dim = arrangeFactories()
  checkBounds(dim)

  def checkBounds(dim: Dimension): Unit = {
    area = new Dimension(Math.max(area.width, dim.width), Math.max(area.height, dim.height))
    setPreferredSize(area)
    revalidate()
  }

  def arrangeFactories(): Dimension = {
    val p = new Point()

    p.move(70, 100)

    for (factory <- factoryLexicon.getFormFactories()) {
      val form = factory.getArchetype()
      form.arrange(p)
      p.translate(0, 50)
    }

    p.translate(-50, 100)

    for (tf <- factoryLexicon.getTransformFactories()) {
      val t = tf.getTransform()
      t.arrange(p)
      p.translate(0, 50)
    }

    new Dimension(1000, p.y)
  }

  def getSelectionText() = (highlighted == null) match {
    case true => "no selection"
    case false => highlighted.toString
  }

  def setHighlighted(f: Widget) = {
    f.setHighlighted(true)
    this.highlighted = f
  }

  def unHighlight(): Boolean = {
    if (highlighted != null) {
      highlighted.setHighlighted(false)
      highlighted = null
      return true
    }
    return false
  }

  val serialVersionUID = 123123L

  override def paintComponent(g: Graphics): Unit = {

    super.paintComponent(g)

    selectionLabel.setText(getSelectionText())

    for (ff <- factoryLexicon.getFormFactories()) {
      ff.getArchetype().paint(g)
    }

    for (tf <- factoryLexicon.getTransformFactories()) {
      tf.getTransform().paint(g)
    }

    for (cg <- history.getCalls()) {
      cg.paint(g)
    }

    for (form <- lexicon.getTopForms()) {
      form.paint(g)
    }

  }

}

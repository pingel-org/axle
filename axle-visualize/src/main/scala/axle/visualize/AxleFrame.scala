package axle.visualize

import java.awt.Color

import javax.swing.JFrame

case class AxleFrame(
  pWidth: Int,
  pHeight: Int,
  bgColor: Color,
  pTitle: String)
  extends JFrame(pTitle) {

  def initialize(): Unit = {
    setBackground(bgColor)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(pWidth, pHeight)
    val bg = add(new BackgroundPanel(pTitle))
    bg.setVisible(true)
  }

}

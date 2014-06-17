package axle.visualize

import java.awt.Color

import javax.swing.JFrame

class AxleFrame(
  width: Int,
  height: Int,
  bgColor: Color,
  title: String)
  extends JFrame(title) {

  def initialize(): Unit = {
    setBackground(bgColor)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(width, height)
    val bg = add(new BackgroundPanel(title))
    bg.setVisible(true)
  }

}

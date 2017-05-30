package axle.awt

import java.awt.Color

import javax.swing.JFrame

// default width/height was 1100/800

case class AxleFrame(
  pWidth: Int,
  pHeight: Int,
  bgColor: Color = Color.white,
  pTitle: String = "αχλε")
  extends JFrame(pTitle) {

  def initialize(): Unit = {
    setBackground(bgColor)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(pWidth, pHeight)
    val bg = add(BackgroundPanel(pTitle))
    bg.setVisible(true)
  }

}

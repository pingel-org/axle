package axle.jogl

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.Dimension

import com.jogamp.opengl.util.FPSAnimator
import com.jogamp.opengl.GL2

import axle.quanta.AngleConverter
import axle.quanta.Distance
import axle.quanta.DistanceConverter
import axle.quanta.UnittedQuantity
import axle.quanta.UnitOfMeasurement
import javax.swing.JFrame
import javax.swing.SwingUtilities
import java.net.URL

case class SceneFrame[S](
    renderAll: (GL2, RenderContext, S) => Unit,
    initialState: S,
    tic: S => S,
    title: String,
    textureUrls: Seq[(URL, String)],
    distanceUnit: UnitOfMeasurement[Distance],
    width: Int,
    height: Int,
    zNear: UnittedQuantity[Distance, Float],
    zFar: UnittedQuantity[Distance, Float],
    fps: Int)(
        implicit angleMeta: AngleConverter[Float],
        distanceMeta: DistanceConverter[Float]) { sceneFrame =>

  val fortyFiveDegreeFloat = 45f *: angleMeta.degree

  val canvas = AxleGLCanvas(sceneFrame, textureUrls, fortyFiveDegreeFloat, zNear, zFar, distanceUnit)
  canvas.setPreferredSize(new Dimension(width, height))

  def run(): Unit = {

    SwingUtilities.invokeLater(new Runnable() {

      def run(): Unit = {

        val animator = new FPSAnimator(canvas, fps, true)

        val frame = new JFrame()
        frame.getContentPane.add(canvas)
        frame.addWindowListener(new WindowAdapter() {
          override def windowClosing(e: WindowEvent): Unit = {
            new Thread() {
              override def run(): Unit = {
                if (animator.isStarted) animator.stop()
                System.exit(0)
              }
            }.start()
          }
        })
        frame.setTitle(title)
        frame.pack()
        frame.setVisible(true)
        val _ = animator.start()
      }
    })

  }
}

package axle.visualize.gl

import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

import com.jogamp.opengl.util.FPSAnimator

import axle.quanta.Distance
import axle.quanta.DistanceConverter
import axle.quanta.UnittedQuantity
import javax.swing.JFrame
import javax.swing.SwingUtilities
import spire.math.Number.apply
import spire.implicits.DoubleAlgebra
import spire.implicits.FloatAlgebra
import spire.implicits.moduleOps
import axle.algebra.DirectedGraph
import axle.quanta.UnitOfMeasurement
import axle.quanta.Angle
import axle.quanta.AngleConverter

case class SceneFrame(
  scene: Scene,
  width: Int,
  height: Int,
  zNear: UnittedQuantity[Distance, Float],
  zFar: UnittedQuantity[Distance, Float],
  fps: Int)(
    implicit angleMeta: AngleConverter[Float],
    distanceMeta: DistanceConverter[Float]) {

  val fortyFiveDegreeFloat = 45f *: angleMeta.degree

  def run(): Unit = {

    SwingUtilities.invokeLater(new Runnable() {

      def run(): Unit = {

        val canvas = AxleGLCanvas(scene, fortyFiveDegreeFloat, zNear, zFar, scene.distanceUnit)
        canvas.setPreferredSize(new Dimension(width, height))

        val animator = new FPSAnimator(canvas, fps, true)

        val frame = new JFrame()
        frame.getContentPane().add(canvas)
        frame.addWindowListener(new WindowAdapter() {
          override def windowClosing(e: WindowEvent): Unit = {
            new Thread() {
              override def run(): Unit = {
                if (animator.isStarted()) animator.stop()
                System.exit(0)
              }
            }.start()
          }
        })
        frame.setTitle(scene.title)
        frame.pack()
        frame.setVisible(true)
        animator.start()
      }
    })

  }
}

package axle.visualize.gl

import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

import com.jogamp.opengl.util.FPSAnimator

import axle.quanta.Distance
import axle.quanta.UnittedQuantity4
import javax.swing.JFrame
import javax.swing.SwingUtilities
import spire.math.Number.apply
import spire.implicits.DoubleAlgebra
import spire.implicits.FloatAlgebra
import spire.implicits.moduleOps
import axle.algebra.DirectedGraph
import axle.quanta.UnitOfMeasurement4
import axle.quanta.AngleFloat

case class SceneFrame[DG[_, _]: DirectedGraph](
  scene: Scene[DG],
  width: Int,
  height: Int,
  zNear: UnittedQuantity4[Distance[Float], Float],
  zFar: UnittedQuantity4[Distance[Float], Float],
  fps: Int)(
    implicit angleCg: DG[UnitOfMeasurement4[axle.quanta.Angle[Float], Float], Float => Float],
    distanceCg: DG[UnitOfMeasurement4[axle.quanta.Distance[Float], Float], Float => Float]) {

  def run(): Unit = {

    SwingUtilities.invokeLater(new Runnable() {

      def run(): Unit = {

        val canvas = AxleGLCanvas(scene, 45f *: AngleFloat.degree, zNear, zFar, scene.distanceUnit)
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

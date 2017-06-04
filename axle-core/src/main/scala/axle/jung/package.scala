package axle

import java.awt.Dimension
import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.visualization.DefaultVisualizationModel
import edu.uci.ics.jung.graph.DirectedSparseGraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import scala.collection.JavaConverters._
import scala.xml.Node
import scala.xml.NodeSeq
import scala.xml.Text
import cats.Eq
import cats.Show
import cats.implicits._
import spire.implicits._
import axle.algebra.DirectedGraph
import axle.algebra.Finite
import axle.algebra.Functor
import axle.algebra.UndirectedGraph
import axle.syntax.directedgraph.directedGraphOps
import axle.syntax.undirectedgraph.undirectedGraphOps
import axle.math.arcTangent2
import axle.web.SVG
import axle.web.SVG.rgb
import axle.web.svgFrame
import axle.visualize.angleDouble
import axle.visualize.Color.black
import axle.awt.Draw
import axle.visualize.DirectedGraphVisualization
import axle.visualize.UndirectedGraphVisualization

package object jung {

  implicit def finiteDirectedSparseGraph[V, E]: Finite[DirectedSparseGraph[V, E], Int] =
    new Finite[DirectedSparseGraph[V, E], Int] {

      def size(jdsg: DirectedSparseGraph[V, E]): Int =
        jdsg.getVertexCount
    }

  implicit def vertexFunctorDSG[V, E, NV]: Functor[DirectedSparseGraph[V, E], V, NV, DirectedSparseGraph[NV, E]] =
    new Functor[DirectedSparseGraph[V, E], V, NV, DirectedSparseGraph[NV, E]] {

      def map(jdsg: DirectedSparseGraph[V, E])(f: V => NV): DirectedSparseGraph[NV, E] = {

        val vertexOld2New: Map[V, NV] =
          jdsg.getVertices.asScala.map({ v => v -> f(v) }).toMap

        val newEdges = jdsg.getEdges.asScala.toSeq.map(e => {
          val ends = jdsg.getEndpoints(e)
          val newV1 = vertexOld2New(ends.getFirst)
          val newV2 = vertexOld2New(ends.getSecond)
          (newV1, newV2, e)
        })

        directedGraphJung.make(
          vertexOld2New.values.toSeq,
          newEdges)
      }
    }

  implicit def edgeFunctorDSG[V, E, NE]: Functor[DirectedSparseGraph[V, E], E, NE, DirectedSparseGraph[V, NE]] =
    new Functor[DirectedSparseGraph[V, E], E, NE, DirectedSparseGraph[V, NE]] {

      def map(jdsg: DirectedSparseGraph[V, E])(f: E => NE): DirectedSparseGraph[V, NE] = {

        val newEdges = jdsg.getEdges.asScala.toSeq.map(e => {
          val ends = jdsg.getEndpoints(e)
          (ends.getFirst, ends.getSecond, f(e))
        })

        directedGraphJung.make(
          jdsg.getVertices.asScala.toSeq,
          newEdges)
      }
    }

  implicit def directedGraphJung[V, E]: DirectedGraph[DirectedSparseGraph[V, E], V, E] =
    new DirectedGraph[DirectedSparseGraph[V, E], V, E] {

      import scala.collection.JavaConverters.asScalaBufferConverter
      import scala.collection.JavaConverters.collectionAsScalaIterableConverter

      import axle.enrichIndexedSeq
      import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath
      import edu.uci.ics.jung.graph.DirectedSparseGraph
      import cats.kernel.Eq

      def make(vertices: Seq[V], ef: Seq[(V, V, E)]): DirectedSparseGraph[V, E] = {

        val jdsg = new DirectedSparseGraph[V, E]

        vertices foreach { jdsg.addVertex } // TODO check return value

        ef foreach {
          case (vi, vj, e) => {
            jdsg.addEdge(e, vi, vj) // TODO check return value
          }
        }

        jdsg
      }

      def vertices(jdsg: DirectedSparseGraph[V, E]): Iterable[V] =
        jdsg.getVertices.asScala

      def edges(jdsg: DirectedSparseGraph[V, E]): Iterable[E] =
        jdsg.getEdges.asScala

      def source(jdsg: DirectedSparseGraph[V, E], e: E): V =
        jdsg.getSource(e)

      def destination(jdsg: DirectedSparseGraph[V, E], e: E): V =
        jdsg.getDest(e)

      // TODO findVertex needs an index
      def findVertex(jdsg: DirectedSparseGraph[V, E], f: V => Boolean): Option[V] =
        vertices(jdsg).find(f)

      def filterEdges(jdsg: DirectedSparseGraph[V, E], f: E => Boolean): DirectedSparseGraph[V, E] =
        make(
          vertices(jdsg).toSeq,
          edges(jdsg).filter(f).toList.map({ case e => (source(jdsg, e), destination(jdsg, e), e) }))

      def areNeighbors(jdg: DirectedSparseGraph[V, E], v1: V, v2: V)(implicit eqV: Eq[V]): Boolean =
        edgesTouching(jdg, v1).exists(edge => connects(jdg, edge, v1, v2))

      def isClique(jdsg: DirectedSparseGraph[V, E], vs: Iterable[V])(implicit eqV: Eq[V]): Boolean =
        (for {
          vi <- vs
          vj <- vs
        } yield {
          (vi === vj) || areNeighbors(jdsg, vi, vj)
        }).forall(identity)

      def forceClique(
        jdsg: DirectedSparseGraph[V, E],
        among: Set[V],
        edgeFn: (V, V) => E)(implicit eqV: Eq[V], mV: Manifest[V]): DirectedSparseGraph[V, E] = {

        val newEdges: Iterable[(V, V, E)] = among.toVector.permutations(2)
          .map({ a =>
            val from = a(0)
            val to = a(1)
            val edge = Option(jdsg.findEdge(from, to)).getOrElse(edgeFn(from, to))
            (from, to, edge)
          })

        make(vertices(jdsg).toList, newEdges.toList)
      }

      def degree(jdsg: DirectedSparseGraph[V, E], v: V): Int =
        edgesTouching(jdsg, v).size

      def edgesTouching(jdsg: DirectedSparseGraph[V, E], v: V): Set[E] =
        jdsg.getIncidentEdges(v).asScala.toSet

      def neighbors(jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
        jdsg.getNeighbors(v).asScala.toSet

      // a "leaf" is vertex with only one neighbor
      def firstLeafOtherThan(jdsg: DirectedSparseGraph[V, E], r: V)(implicit eqV: Eq[V]): Option[V] =
        vertices(jdsg).find(v => neighbors(jdsg, v).size === 1 && (!(v === r)))

      /**
       * "decompositions" page 3 (Definition 3, Section 9.3)
       * turn the neighbors of v into a clique
       */

      def eliminate(jdsg: DirectedSparseGraph[V, E], v: V, payload: (V, V) => E): DirectedSparseGraph[V, E] = {

        // TODO
        //    val vs = neighbors(v)
        //    makeFunctional.jungGraph.removeVertex(v)
        //    forceClique(vs, payload)
        ???
      }

      def other(jdsg: DirectedSparseGraph[V, E], edge: E, u: V)(implicit eqV: Eq[V]): V =
        u match {
          case _ if (u === source(jdsg, edge))      => destination(jdsg, edge)
          case _ if (u === destination(jdsg, edge)) => source(jdsg, edge)
          case _                                    => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
        }

      def connects(jdsg: DirectedSparseGraph[V, E], edge: E, a1: V, a2: V)(implicit eqV: Eq[V]): Boolean =
        (a1 === source(jdsg, edge) && a2 === destination(jdsg, edge)) || (a2 === source(jdsg, edge) && a1 === destination(jdsg, edge))

      def leaves(jdsg: DirectedSparseGraph[V, E]): Set[V] =
        vertices(jdsg).filter(v => isLeaf(jdsg, v)).toSet

      def precedes(jdsg: DirectedSparseGraph[V, E], v1: V, v2: V): Boolean =
        predecessors(jdsg, v2).contains(v1)

      def predecessors(jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
        jdsg.getPredecessors(v).asScala.toSet

      def isLeaf(jdsg: DirectedSparseGraph[V, E], v: V): Boolean =
        jdsg.getSuccessorCount(v) === 0

      def successors(jdsg: DirectedSparseGraph[V, E], v: V): Set[V] =
        jdsg.getSuccessors(v).asScala.toSet

      def outputEdgesOf(jdsg: DirectedSparseGraph[V, E], v: V): Set[E] =
        jdsg.getOutEdges(v).asScala.toSet

      def descendantsIntersectsSet(jdsg: DirectedSparseGraph[V, E], v: V, s: Set[V]): Boolean =
        s.contains(v) || s.exists(x => descendantsIntersectsSet(jdsg, x, s))

      def removeInputs(jdsg: DirectedSparseGraph[V, E], to: Set[V]): DirectedSparseGraph[V, E] =
        filterEdges(jdsg, edge => !to.contains(destination(jdsg, edge)))

      def removeOutputs(jdsg: DirectedSparseGraph[V, E], from: Set[V]): DirectedSparseGraph[V, E] =
        filterEdges(jdsg, edge => !from.contains(source(jdsg, edge)))

      // TODO def moralGraph(jdsg: DirectedSparseGraph[V, E]): Boolean

      // TODO def isAcyclic(jdsg: DirectedSparseGraph[V, E]): Boolean

      def shortestPath(jdsg: DirectedSparseGraph[V, E], source: V, goal: V)(implicit eqV: Eq[V]): Option[List[E]] =
        if (source === goal) {
          Some(Nil)
        } else {
          Option((new DijkstraShortestPath(jdsg)).getPath(source, goal)) flatMap { path =>
            if (path.size === 0)
              None
            else
              Some(path.asScala.toList)
          }
        }

    }

  implicit def finiteUndirectedSparseGraph[V, E]: Finite[UndirectedSparseGraph[V, E], Int] =
    new Finite[UndirectedSparseGraph[V, E], Int] {

      def size(jusg: UndirectedSparseGraph[V, E]): Int =
        jusg.getVertexCount
    }

  implicit def vertexFunctorUDSG[V, E, NV]: Functor[UndirectedSparseGraph[V, E], V, NV, UndirectedSparseGraph[NV, E]] =
    new Functor[UndirectedSparseGraph[V, E], V, NV, UndirectedSparseGraph[NV, E]] {

      def map(jusg: UndirectedSparseGraph[V, E])(f: V => NV): UndirectedSparseGraph[NV, E] = {

        val vertexOld2New: Map[V, NV] =
          jusg.getVertices.asScala.map({ v => v -> f(v) }).toMap

        val newEdges = jusg.getEdges.asScala.toSeq.map(e => {
          val ends = jusg.getEndpoints(e)
          val newV1 = vertexOld2New(ends.getFirst)
          val newV2 = vertexOld2New(ends.getSecond)
          (newV1, newV2, e)
        })

        undirectedGraphJung.make(
          vertexOld2New.values.toSeq,
          newEdges)
      }
    }

  implicit def edgeFunctorUDSG[V, E, NE]: Functor[UndirectedSparseGraph[V, E], E, NE, UndirectedSparseGraph[V, NE]] =
    new Functor[UndirectedSparseGraph[V, E], E, NE, UndirectedSparseGraph[V, NE]] {
      def map(jusg: UndirectedSparseGraph[V, E])(f: E => NE): UndirectedSparseGraph[V, NE] = {

        val newEdges = jusg.getEdges.asScala.toSeq.map(e => {
          val ends = jusg.getEndpoints(e)
          (ends.getFirst, ends.getSecond, f(e))
        })

        undirectedGraphJung.make(
          jusg.getVertices.asScala.toSeq,
          newEdges)
      }
    }

  implicit def undirectedGraphJung[V, E]: UndirectedGraph[UndirectedSparseGraph[V, E], V, E] =
    new UndirectedGraph[UndirectedSparseGraph[V, E], V, E] {

      import scala.collection.JavaConverters.collectionAsScalaIterableConverter

      import edu.uci.ics.jung.graph.UndirectedSparseGraph
      import cats.kernel.Eq
      import spire.implicits.IntAlgebra
      import spire.implicits.eqOps

      def make(vertices: Seq[V], ef: Seq[(V, V, E)]): UndirectedSparseGraph[V, E] = {

        val jusg = new UndirectedSparseGraph[V, E]

        vertices foreach { jusg.addVertex } // TODO check return value

        ef foreach {
          case (vi, vj, e) =>
            jusg.addEdge(e, vi, vj) // TODO check return value
        }

        jusg
      }

      def vertices(jusg: UndirectedSparseGraph[V, E]): Iterable[V] =
        jusg.getVertices.asScala

      def edges(jusg: UndirectedSparseGraph[V, E]): Iterable[E] =
        jusg.getEdges.asScala

      def vertices(jusg: UndirectedSparseGraph[V, E], e: E): (V, V) = {
        val pair = jusg.getEndpoints(e)
        (pair.getFirst, pair.getSecond)
      }

      // TODO findVertex needs an index
      def findVertex(jusg: UndirectedSparseGraph[V, E], f: V => Boolean): Option[V] =
        vertices(jusg).find(f)

      def filterEdges(jusg: UndirectedSparseGraph[V, E], f: E => Boolean): UndirectedSparseGraph[V, E] =
        make(
          vertices(jusg).toSeq,
          edges(jusg).filter(f).toList.map({
            case e => {
              val pair = vertices(jusg, e)
              (pair._1, pair._2, e)
            }
          }))

      //  def unlink(e: Edge[ES, E]): UndirectedSparseGraph[V, E] =
      //    filterEdges(t => {
      //      val v1 = e.storage._1
      //      val v2 = e.storage._2
      //      !((v1, v2, e.payload) === t || (v2, v1, e.payload) === t)
      //    })

      //  // UndirectedSparseGraph[V, E]
      //  def unlink(v1: Vertex[V], v2: Vertex[V]) =
      //    filterEdges(e => (e._1 === v1 && e._2 === v2) || (e._2 === v1 && e._1 === v2))

      def areNeighbors(jug: UndirectedSparseGraph[V, E], v1: V, v2: V)(implicit eqV: Eq[V]): Boolean =
        edgesTouching(jug, v1).exists(edge => connects(jug, edge, v1, v2))

      def isClique(jug: UndirectedSparseGraph[V, E], vs: Iterable[V])(implicit eqV: Eq[V]): Boolean =
        (for {
          vi <- vs
          vj <- vs
        } yield {
          (vi === vj) || areNeighbors(jug, vi, vj)
        }).forall(identity)

      def forceClique(
        jug: UndirectedSparseGraph[V, E],
        among: Set[V],
        edgeFn: (V, V) => E)(implicit eqV: Eq[V], mv: Manifest[V]): UndirectedSparseGraph[V, E] = {

        val newEdges: Iterator[(V, V, E)] = among.toVector.combinations(2)
          .map({ both =>
            val v1 = both(0)
            val v2 = both(1)
            val edge = Option(jug.findEdge(v1, v2)).getOrElse(edgeFn(v1, v2))
            (v1, v2, edge)
          })

        make(vertices(jug).toList, newEdges.toList)
      }

      def degree(jusg: UndirectedSparseGraph[V, E], v: V): Int =
        edgesTouching(jusg, v).size

      def edgesTouching(jusg: UndirectedSparseGraph[V, E], v: V): Iterable[E] =
        jusg.getIncidentEdges(v).asScala

      def neighbors(jusg: UndirectedSparseGraph[V, E], v: V): Iterable[V] =
        jusg.getNeighbors(v).asScala

      //  def delete(v: Vertex[V]): UndirectedSparseGraph[V, E] =
      //    UndirectedSparseGraph(vertices.toSeq.filter(_ != v).map(_.payload), ef)

      // a "leaf" is vertex with only one neighbor
      def firstLeafOtherThan(jug: UndirectedSparseGraph[V, E], r: V)(implicit eqV: Eq[V]): Option[V] =
        vertices(jug).find(v => neighbors(jug, v).size === 1 && (!(v === r)))

      /**
       * "decompositions" page 3 (Definition 3, Section 9.3)
       * turn the neighbors of v into a clique
       */

      def eliminate(jug: UndirectedSparseGraph[V, E], v: V, payload: (V, V) => E): UndirectedSparseGraph[V, E] = {

        // TODO
        //    val vs = neighbors(v)
        //    makeFunctional.jungGraph.removeVertex(v)
        //    forceClique(vs, payload)
        ???
      }

      def other(jusg: UndirectedSparseGraph[V, E], edge: E, u: V)(implicit eqV: Eq[V]): V = {
        val (v1, v2) = vertices(jusg, edge)
        u match {
          case _ if (u === v1) => v2
          case _ if (u === v2) => v1
          case _               => throw new Exception("can't find 'other' of a vertex that isn't on the edge itself")
        }
      }

      def connects(jusg: UndirectedSparseGraph[V, E], edge: E, a1: V, a2: V)(implicit eqV: Eq[V]): Boolean = {
        val (v1, v2) = vertices(jusg, edge)
        (v1 === a1 && v2 === a2) || (v2 === a1 && v1 === a2)
      }

    }

  implicit def svgJungDirectedGraphVisualization[VP: Eq: HtmlFrom, EP: Show]: SVG[DirectedGraphVisualization[DirectedSparseGraph[VP, EP]]] =
    new SVG[DirectedGraphVisualization[DirectedSparseGraph[VP, EP]]] {

      def svg(vis: DirectedGraphVisualization[DirectedSparseGraph[VP, EP]]): NodeSeq = {

        import vis._

        val layout = new FRLayout(dg)
        layout.setSize(new Dimension(width, height))
        val visualization = new DefaultVisualizationModel(layout)

        val lines: List[Node] = dg.getEdges.asScala.map { edge =>
          <line x1={ s"${layout.getX(dg.getSource(edge))}" } y1={ s"${layout.getY(dg.getSource(edge))}" } x2={ s"${layout.getX(dg.getDest(edge))}" } y2={ s"${layout.getY(dg.getDest(edge))}" } stroke={ s"${rgb(black)}" } stroke-width="1"/>
        } toList

        val arrows: List[Node] = dg.getEdges.asScala.map { edge =>
          val height = layout.getY(dg.getSource(edge)) - layout.getY(dg.getDest(edge))
          val width = layout.getX(dg.getDest(edge)) - layout.getX(dg.getSource(edge))
          val svgRotationAngle = 180d - (arcTangent2(height, width) in angleDouble.degree).magnitude
          <polygon points={ s"${radius},0 ${radius + arrowLength},3 ${radius + arrowLength},-3" } fill="black" transform={ s"translate(${layout.getX(dg.getDest(edge))},${layout.getY(dg.getDest(edge))}) rotate($svgRotationAngle)" }/>
        } toList

        val circles: List[Node] = dg.getVertices.asScala.map { vertex =>
          <circle cx={ s"${layout.getX(vertex)}" } cy={ s"${layout.getY(vertex)}" } r={ s"${radius}" } fill={ s"${rgb(color)}" } stroke={ s"${rgb(borderColor)}" } stroke-width="1"/>
        } toList

        val labels: List[Node] = dg.getVertices.asScala.map { vertex =>
          val node = HtmlFrom[VP].toHtml(vertex)
          node match {
            case scala.xml.Text(text) =>
              <text text-anchor="middle" alignment-baseline="middle" x={ s"${layout.getX(vertex)}" } y={ s"${layout.getY(vertex)}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ text }</text>
            case _ =>
              <foreignObject x={ s"${layout.getX(vertex)}" } y={ s"${layout.getY(vertex)}" } width="100%" height="100%">
                <html xmlns="http://www.w3.org/1999/xhtml">
                  { node }
                </html>
              </foreignObject>
          }
        } toList

        val edgeLabels: List[Node] = dg.getEdges.asScala.map { edge =>
          val node = HtmlFrom[EP].toHtml(edge)
          val cx = (layout.getX(dg.getDest(edge)) - layout.getX(dg.getSource(edge))) * 0.6 + layout.getX(dg.getSource(edge))
          val cy = (layout.getY(dg.getDest(edge)) - layout.getY(dg.getSource(edge))) * 0.6 + layout.getY(dg.getSource(edge))
          node match {
            case Text(text) =>
              <text text-anchor="middle" alignment-baseline="middle" x={ s"${cx}" } y={ s"${cy}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ text }</text>
            case _ =>
              <foreignObject x={ s"${cx}" } y={ s"${cy}" } width="100%" height="100%">
                { node }
              </foreignObject>
          }
        } toList

        val nodes = lines ++ arrows ++ circles ++ labels ++ edgeLabels

        svgFrame(nodes, width.toDouble, height.toDouble)
      }

    }

  implicit def svgJungUndirectedGraphVisualization[VP: Eq: HtmlFrom, EP: Show]: SVG[UndirectedGraphVisualization[UndirectedSparseGraph[VP, EP]]] = new SVG[UndirectedGraphVisualization[UndirectedSparseGraph[VP, EP]]] {

    def svg(vis: UndirectedGraphVisualization[UndirectedSparseGraph[VP, EP]]): NodeSeq = {

      import vis._

      val layout = new FRLayout(ug)
      layout.setSize(new Dimension(width, height))
      val visualization = new DefaultVisualizationModel(layout)

      val lines: List[Node] = ug.getEdges.asScala.map { edge =>
        val (v1, v2) = ug.vertices(edge)
        <line x1={ s"${layout.getX(v1)}" } y1={ s"${layout.getY(v1)}" } x2={ s"${layout.getX(v2)}" } y2={ s"${layout.getY(v2)}" } stroke={ s"${rgb(black)}" } stroke-width="1"/>
      } toList

      val circles: List[Node] = ug.getVertices.asScala.map { vertex =>
        <circle cx={ s"${layout.getX(vertex)}" } cy={ s"${layout.getY(vertex)}" } r={ s"${radius}" } fill={ s"${rgb(color)}" } stroke={ s"${rgb(borderColor)}" } stroke-width="1"/>
      } toList

      val labels: List[Node] = ug.getVertices.asScala.map { vertex =>
        val node = HtmlFrom[VP].toHtml(vertex)
        node match {
          case Text(t) =>
            <text text-anchor="middle" alignment-baseline="middle" x={ s"${layout.getX(vertex)}" } y={ s"${layout.getY(vertex)}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ axle.html(vertex) }</text>
          case _ =>
            <foreignObject x={ s"${layout.getX(vertex)}" } y={ s"${layout.getY(vertex)}" } width="100%" height="100%">
              { node }
            </foreignObject>
        }
      } toList

      val edgeLabels: List[Node] = ug.getEdges.asScala.map { edge =>
        val node = HtmlFrom[EP].toHtml(edge)
        val (v1, v2) = ug.vertices(edge)
        val cx = (layout.getX(v2) - layout.getX(v1)) * 0.5 + layout.getX(v1)
        val cy = (layout.getY(v2) - layout.getY(v1)) * 0.5 + layout.getY(v1)
        node match {
          case Text(text) =>
            <text text-anchor="middle" alignment-baseline="middle" x={ s"${cx}" } y={ s"${cy}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ text }</text>
          case _ =>
            <foreignObject x={ s"${cx}" } y={ s"${cy}" } width="100%" height="100%">
              { node }
            </foreignObject>
        }
      } toList

      val nodes = lines ++ circles ++ labels ++ edgeLabels

      svgFrame(nodes, width.toDouble, height.toDouble)
    }

  }

  implicit def drawJungUndirectedGraph[VP: Show, EP: Show]: Draw[UndirectedGraphVisualization[UndirectedSparseGraph[VP, EP]]] =
    new Draw[UndirectedGraphVisualization[UndirectedSparseGraph[VP, EP]]] {

      import java.awt.BasicStroke
      import java.awt.Color
      import java.awt.Dimension
      import java.awt.Paint
      import java.awt.Stroke
      import java.awt.event.MouseEvent

      import edu.uci.ics.jung.algorithms.layout.FRLayout
      import edu.uci.ics.jung.visualization.VisualizationViewer
      import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
      import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
      import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
      import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position

      import com.google.common.base.{ Function => GoogleFunction }

      def component(vis: UndirectedGraphVisualization[UndirectedSparseGraph[VP, EP]]) = {
        import vis._
        val layout = new FRLayout(ug)
        layout.setSize(new Dimension(width, height))
        val vv = new VisualizationViewer(layout) // interactive
        vv.setPreferredSize(new Dimension(width + border, height + border))
        vv.setMinimumSize(new Dimension(width + border, height + border))

        val vertexPaint = new GoogleFunction[VP, Paint]() {
          def apply(i: VP): Paint = Color.GREEN // TODO use color.{r,g,b}
        }

        val dash = List(10f).toArray

        val edgeStroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f)

        val edgeStrokeTransformer = new GoogleFunction[EP, Stroke]() {
          def apply(edge: EP): BasicStroke = edgeStroke
        }

        val vertexLabelTransformer = new GoogleFunction[VP, String]() {
          def apply(vertex: VP): String = string(vertex)
        }

        val edgeLabelTransformer = new GoogleFunction[EP, String]() {
          def apply(edge: EP): String = string(edge)
        }

        vv.getRenderContext.setVertexFillPaintTransformer(vertexPaint)
        vv.getRenderContext.setEdgeStrokeTransformer(edgeStrokeTransformer)
        vv.getRenderContext.setVertexLabelTransformer(vertexLabelTransformer)
        vv.getRenderContext.setEdgeLabelTransformer(edgeLabelTransformer)
        vv.getRenderer.getVertexLabelRenderer.setPosition(Position.CNTR)

        val gm = new PluggableGraphMouse()
        gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1))
        gm.add(new PickingGraphMousePlugin())
        vv.setGraphMouse(gm)

        vv
      }

    }

  implicit def drawJungDirectedSparseGraphVisualization[VP: HtmlFrom, EP: Show]: Draw[DirectedGraphVisualization[DirectedSparseGraph[VP, EP]]] =
    new Draw[DirectedGraphVisualization[DirectedSparseGraph[VP, EP]]] {

      import java.awt.BasicStroke
      import java.awt.Color
      // import java.awt.Component
      import java.awt.Dimension
      import java.awt.Paint
      import java.awt.Stroke
      import java.awt.event.MouseEvent
      import edu.uci.ics.jung.algorithms.layout.FRLayout
      import edu.uci.ics.jung.visualization.VisualizationViewer
      import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin
      import edu.uci.ics.jung.visualization.control.PluggableGraphMouse
      import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin
      import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position

      import com.google.common.base.{ Function => GoogleFunction }

      def component(vis: DirectedGraphVisualization[DirectedSparseGraph[VP, EP]]) = {
        // see
        // http://www.grotto-networking.com/JUNG/
        // http://www.grotto-networking.com/JUNG/JUNG2-Tutorial.pdf

        import vis._

        val layout = new FRLayout(dg)
        layout.setSize(new Dimension(width, height))
        // val vv = new BasicVisualizationServer[ug.type#V, ug.type#E](layout) // non-interactive
        val vv = new VisualizationViewer(layout) // interactive
        vv.setPreferredSize(new Dimension(width + border, height + border))
        vv.setMinimumSize(new Dimension(width + border, height + border))

        val vertexPaint = new GoogleFunction[VP, Paint]() {
          def apply(i: VP): Paint = Color.GREEN // TODO use color.{r,g,b}
        }

        val dash = List(10f).toArray

        val edgeStroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f)

        val edgeStrokeTransformer = new GoogleFunction[EP, Stroke]() {
          def apply(e: EP): BasicStroke = edgeStroke
        }

        val vertexLabelTransformer = new GoogleFunction[VP, String]() {
          def apply(v: VP): String = {
            val label = html(v)
            label match {
              case scala.xml.Text(text) => text
              case _                    => string((<html>{ label }</html>).asInstanceOf[scala.xml.Node])
            }
          }
        }

        val edgeLabelTransformer = new GoogleFunction[EP, String]() {
          def apply(e: EP): String = string(e)
        }

        vv.getRenderContext.setVertexFillPaintTransformer(vertexPaint)
        vv.getRenderContext.setEdgeStrokeTransformer(edgeStrokeTransformer)
        vv.getRenderContext.setVertexLabelTransformer(vertexLabelTransformer)
        vv.getRenderContext.setEdgeLabelTransformer(edgeLabelTransformer)
        vv.getRenderer.getVertexLabelRenderer.setPosition(Position.CNTR)

        // val gm = new DefaultModalGraphMouse()
        // gm.setMode(ModalGraphMouse.Mode.TRANSFORMING)
        val gm = new PluggableGraphMouse()
        gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON1))
        gm.add(new PickingGraphMousePlugin())
        // gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f))
        vv.setGraphMouse(gm)
        vv
      }
    }

}

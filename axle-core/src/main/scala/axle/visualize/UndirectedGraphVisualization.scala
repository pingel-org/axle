package axle.visualize

import Color.yellow
import Color.black

case class UndirectedGraphVisualization[G, V, E](
  ug:          G,
  width:       Int   = 700,
  height:      Int   = 700,
  border:      Int   = 20,
  radius:      Int   = 10,
  color:       Color = yellow,
  borderColor: Color = black,
  fontSize:    Int   = 12)

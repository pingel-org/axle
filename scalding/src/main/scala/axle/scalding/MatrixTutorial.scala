package axle.scalding

import com.twitter.scalding._
import com.twitter.scalding.mathematics.Matrix

class MatrixTutorial(args: Args) extends Job(args) {

  import Matrix._

  val adjacencyMatrix = Tsv("graph.tsv", ('user1, 'user2, 'rel))
    .read
    .toMatrix[Long, Long, Double]('user1, 'user2, 'rel)

  adjacencyMatrix.sumColVectors.write(Tsv("outdegree.tsv"))

}


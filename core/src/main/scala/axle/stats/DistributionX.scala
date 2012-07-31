package axle.stats

abstract class DistributionX(variables: List[RandomVariable[_]]) {

  def getVariables(): List[RandomVariable[_]] = variables

}

package axle

import axle._
import collection._

package object stats {

  implicit def rv2it[K](rv: RandomVariable[K]) = rv.getValues.get
  
  implicit def enrichCaseGenTraversable[A](cgt: GenTraversable[Case[A]]) = EnrichedCaseGenTraversable(cgt)
  
  def coin(pHead: Double = 0.5) = RandomVariable0("coin",
    values = Some(List('HEAD, 'TAIL)),
    distribution = Some(new ConditionalProbabilityTable0(immutable.Map('HEAD -> pHead, 'TAIL -> (1.0 - pHead)))))
  
}
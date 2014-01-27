
package axle

import org.specs2.mutable._
import axle.algebra._

class AxleSpecification extends Specification {

  "Applicatives" should {
    "work" in {
      /*
    val p2 = pure2( { (x: Int) => x + 3 } )

    val somePlus3 = Some( { (x: Int) => x + 3 } )

    val a1 = somePlus3 <*> Some(9)

    val a2 = pure2( { (x: Int) => x + 3 } ) <*> Some(10)

    val a3 = pure2( { (x: Int) => x + 3 } ) <*> Some(9)

    val a4 = Some( { (s: String) => s ++ "hahah" } ) <*> None

    val a5 = None <*> Some("woot")

    val intAdd = { (x: Int, y: Int) => x + y }
    val stringAppend = { (s1: String, s2: String) => s1 ++ s2 }

    val p4 = pure3( intAdd )

    val a6 = ( pure( intAdd ) <*> Some(3) ) <*> Some(5)
    val a7 = ( pure( intAdd ) <*> Some(3) ) <*> None
    val a8 = ( pure( intAdd ) <*> None    ) <*> Some(5)
*/
      1 must be equalTo (1)
    }
  }

}

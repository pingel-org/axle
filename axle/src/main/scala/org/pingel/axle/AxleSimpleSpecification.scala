
package org.pingel.axle

import AxleSimple._

object AxleSimpleSpecification {


  // ((xs: List[Char]) => xs.map( _.toUpper ).reverse.intersperse('-'))(getLine).mkString("")

  // (((intersperse('-') _ )) compose (reverse[Char] _) compose (fmaplist[Char, Char]( _.toUpper) _))(getLine).mkString("")

  fmaplist( replicate[Int](3) _ )( List(1, 2, 3) )
  fmapopt( replicate[Int](3) _ )( Some(4) )
  fmapright( replicate[String](3) _ )( Right("blah") )
  fmapleft( replicate[String](3) _ )( Left("foo") )
  
  fmapfn({ (_: Int) * 3 } )( { (_: Int) + 100 } )(1)

  { (_: Int) * 3 } compose { (_: Int) + 100 } apply (1)

  fmaplist( replicate[Int](3) _ )( List(1, 2, 3, 4) )
  fmapopt( replicate[Int](3) _ )( Some(4) )
  fmapright( replicate[String](3) _ )( Right("blah") )
  fmapopt( replicate[Int](3) _ )( None )
  fmapleft( replicate[String](3) _ )( Left("foo") )

 
}

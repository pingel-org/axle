package org.pingel.cattheory

import scalaz._
import Scalaz._

// http://www.cs.nott.ac.uk/~vxc/mgs/MGS2011_categories.pdf

/*
object ScalazTest {

	def main(args: Array[String]) {

		val int_zero_0 = new Zero[Int] { val zero = 0 }
		val int_zero_1 = new Zero[Int] { val zero = 1 }
		val string_zero = new Zero[String] { val zero = "" }

		val int_semi_add = new Semigroup[Int] { def append(s1: Int, s2: => Int): Int = s1 + s2 }
		val int_monoid_add = new Monoid[Int] {
			def append(s1: Int, s2: => Int) = int_semi_add append (s1, s2)  
			val zero = int_zero_0.zero
		}

		val int_semi_mult = new Semigroup[Int] { def append(s1: Int, s2: => Int): Int = s1 * s2 }
		val int_monoid_mult = new Monoid[Int] {
			def append(s1: Int, s2: => Int) = int_semi_mult append (s1, s2)  
			val zero = int_zero_1.zero
		}

		val int_semi_mod = new Semigroup[Int] { def append(s1: Int, s2: => Int): Int = s1 % s2 }
		val int_monoid_mod = new Monoid[Int] {
			def append(s1: Int, s2: => Int) = int_semi_mod append (s1, s2)  
			val zero = int_zero_1.zero
		}

		val int_semi_div = new Semigroup[Int] { def append(s1: Int, s2: => Int): Int = s1 / s2 }
		val int_monoid_div = new Monoid[Int] {
			def append(s1: Int, s2: => Int) = int_semi_div append (s1, s2)  
			val zero = int_zero_1.zero
		}
				
		val string_semi_concat = new Semigroup[String] { def append(s1: String, s2: => String): String = s1 + s2 }		
		val string_monoid_concat = new Monoid[String] {
			def append(s1: String, s2: => String) = string_semi_concat append (s1, s2)  
			val zero = string_zero.zero
		}
		
		println("int_monoid_add append 3 4 = " + int_monoid_add.append(3,4))
		println("int_monoid_mult append 3 4 = " + int_monoid_mult.append(3,4))
		println("int_monoid_div append 3 4 = " + int_monoid_div.append(3,4))
		println("int_monoid_mod append 3 4 = " + int_monoid_mod.append(3,4))

		println("string_monoid_concat append abc def = " + string_monoid_concat.append("abc","def"))

		val set_category = new Category[Set] {
			// TODO
		}
		
	}

}
*/

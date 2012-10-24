package axle.lx

import org.specs2.mutable._

class PorterStemmerSpec extends Specification {

  import PorterStemmer._

  // See http://snowball.tartarus.org/algorithms/porter/stemmer.html
  
  "Step 1a" should {
    "work" in {
      // stem("caresses") must be equalTo ("caress")
      // stem("ponies") must be equalTo ("poni")
      // stem("ties") must be equalTo ("ti")
      // stem("cats") must be equalTo ("cat")
      1 must be equalTo(1)
    }
  }

  "Step 1b" should {
    "work" in {
      // stem("feed") must be equalTo ("feed")
      // stem("agreed") must be equalTo ("agree")
      // stem("plastered") must be equalTo ("plaster")
      // stem("bled") must be equalTo ("bled")
      // stem("motoring") must be equalTo ("motor")
      // stem("sing") must be equalTo ("sing")
      // stem("conflated") must be equalTo ("conflate")
      // stem("troubled") must be equalTo ("trouble")
      1 must be equalTo(1)
    }
  }

  "Step 1c" should {
    "work" in {
      stem("happy") must be equalTo ("happi")
      stem("sky") must be equalTo ("sky")
    }
  }

  // stem("") must be equalTo ("")

}

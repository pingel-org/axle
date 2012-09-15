package axle.lx

/**
 * http://snowball.tartarus.org/algorithms/porter/stemmer.html
 *
 * Based on the java implementation found at
 *    http://tartarus.org/martin/PorterStemmer/java.txt
 */

import scalaz._
import Scalaz._
import collection._

object PorterStemmer {

  val b = new mutable.ListBuffer[Char]
  var j = 0
  var k = 0

  private def cons(i: Int): Boolean = b(i) match {
    case 'a' | 'e' | 'i' | 'o' | 'u' => false
    case 'y' => (i == 0) ? true | !cons(i - 1)
    case _ => true
  }

  private def m(): Int = {
    var n = 0
    var i = 0
    var broken = false
    while (!broken) {
      if (i > j) return n
      if (!cons(i))
        broken = true
      else
        i += 1
    }
    i += 1
    broken = false
    while (!broken) {
      while (!broken) {
        if (i > j) return n
        if (cons(i))
          broken = true
        else
          i += 1
      }
      if (!broken) {
        i += 1
        n += 1
        while (!broken) {
          if (i > j) return n
          if (!cons(i))
            broken = true
          else
            i += 1
        }
        i += 1
      }
    }
    return -1
  }

  private def vowelinstem(): Boolean = {
    for (i <- 0 until j) if (!cons(i)) return true
    return false
  }

  private def doublec(j: Int): Boolean = {
    if (j < 1) return false
    if (b(j) != b(j - 1)) return false
    cons(j)
  }

  private def cvc(i: Int): Boolean = {
    if (i < 2 || !cons(i) || cons(i - 1) || !cons(i - 2))
      return false
    val ch = b(i)
    if (ch == 'w' || ch == 'x' || ch == 'y') return false
    true
  }

  private def ends(s: String): Boolean = {
    val l = s.length()
    val o = k - l + 1
    if (o < 0) return false
    for (i <- 0 until l) if (b(o + i) != s.charAt(i)) return false
    j = k - l
    true
  }

  private def setto(s: String): Unit = {
    var l = s.length()
    var o = j + 1
    for (i <- 0 until l) b(o + i) = s.charAt(i)
    k = j + l
  }

  private def r(s: String): Unit = if (m() > 0) setto(s)

  private def step1(): Unit = {
    if (b(k) == 's') {
      if (ends("sses")) k -= 2 else if (ends("ies")) setto("i") else if (b(k - 1) != 's') k -= 1
    }
    if (ends("eed")) {
      if (m() > 0) { k -= 1 }
    } else if ((ends("ed") || ends("ing")) && vowelinstem()) {
      k = j
      if (ends("at")) setto("ate")
      else if (ends("bl")) setto("ble")
      else if (ends("iz")) setto("ize")
      else if (doublec(k)) {
        k -= 1
        val ch = b(k)
        if (ch == 'l' || ch == 's' || ch == 'z') k += 1
      } else if (m() == 1 && cvc(k)) setto("e")
    }
  }

  private def step2(): Unit = { if (ends("y") && vowelinstem()) b(k) = 'i' }

  private def step3(): Unit = {
    if (k == 0) return
    else
      b(k - 1) match {
        case 'a' => {
          if (ends("ational")) { r("ate") }
          else if (ends("tional")) { r("tion") }
        }
        case 'c' => {
          if (ends("enci")) { r("ence") }
          else if (ends("anci")) { r("ance") }
        }
        case 'e' => if (ends("izer")) { r("ize") }
        case 'l' => {
          if (ends("bli")) { r("ble") }
          else if (ends("alli")) { r("al") }
          else if (ends("entli")) { r("ent") }
          else if (ends("eli")) { r("e") }
          else if (ends("ousli")) { r("ous") }
        }
        case 'o' => {
          if (ends("ization")) { r("ize") }
          else if (ends("ation")) { r("ate") }
          else if (ends("ator")) { r("ate") }
        }
        case 's' => {
          if (ends("alism")) { r("al") }
          else if (ends("iveness")) { r("ive") }
          else if (ends("fulness")) { r("ful") }
          else if (ends("ousness")) { r("ous") }
        }
        case 't' => {
          if (ends("aliti")) { r("al") }
          else if (ends("iviti")) { r("ive") }
          else if (ends("biliti")) { r("ble") }
        }
        case 'g' => if (ends("logi")) { r("log") }
        case _ => { }
      }
  }

  private def step4(): Unit = b(k) match {
    case 'e' => {
      if (ends("icate")) { r("ic") }
      else if (ends("ative")) { r("") }
      else if (ends("alize")) { r("al") }
    }
    case 'i' => if (ends("iciti")) { r("ic") }
    case 'l' => {
      if (ends("ical")) { r("ic") }
      else if (ends("ful")) { r("") }
    }
    case 's' => if (ends("ness")) { r("") }
    case _ => { }
  }

  private def step5(): Unit = {
    if (k == 0) return
    b(k - 1) match {
      case 'a' => if (ends("al")) { return }
      case 'c' => {
        if (ends("ance")) {}
        else if (ends("ence")) { return }
      }
      case 'e' => if (ends("er")) { return }
      case 'i' => if (ends("ic")) { return }
      case 'l' => {
        if (ends("able")) {}
        else if (ends("ible")) { return }
      }
      case 'n' => {
        if (ends("ant")) {}
        else if (ends("ement")) {}
        else if (ends("ment")) {}
        else if (ends("ent")) { return }
      }
      case 'o' => {
        if (ends("ion") && j >= 0 && (b(j) == 's' || b(j) == 't')) {}
        else if (ends("ou")) { return }
      }
      case 's' => if (ends("ism")) { return }
      case 't' => {
        if (ends("ate")) {}
        else if (ends("iti")) { return }
      }
      case 'u' => if (ends("ous")) { return }
      case 'v' => if (ends("ive")) { return }
      case 'z' => if (ends("ize")) { return }
      case _ => { return }
    }
    if (m() > 1) k = j
  }

  private def step6(): Unit = {
    j = k
    if (b(k) == 'e') {
      val a = m()
      if (a > 1 || a == 1 && !cvc(k - 1)) k -= 1
    }
    if (b(k) == 'l' && doublec(k) && m() > 1) k -= 1
  }

  def stem(word: String): String = {
    b ++= word // TODO: padding should be added dynamically as needed
    k = word.length - 1
    if (k > 1) {
      step1()
      step2()
      step3()
      step4()
      step5()
      step6()
    }
    val result = b.slice(0, k + 1).mkString("")
    b.clear
    result
  }

}


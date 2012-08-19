

import axle.Loggable

object Foo extends Loggable {

  def doit() = { info("logged info message") }

}

object doit {

  def main(args: Array[String]): Unit = {
    Foo.doit()
  }

}

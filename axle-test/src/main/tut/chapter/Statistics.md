
package axle.site.chapter

import axle.blog.model._
import xml._
import axle.blog.Util._
import axle.site.Util._
import axle.blog.model.Chapter
import axle.blog.model.CodeSnippet
import axle.blog.model.Image
import axle.site._

object Statistics {

  val interpreter = Interpreter()

  val chapter =
    Chapter("Statistics",
      <div>
        Topics include: Random Variables, Distributions, Probability, and Standard Deviation.
      </div>,
      Section("Uniform Distribution",
        InterpretSideEffect(interpreter, """
import axle._
import axle.stats._
import spire.math._
import spire.algebra._
"""),
        InterpretSideEffect(interpreter, """
val dist = uniformDistribution(List(2d, 4d, 4d, 4d, 5d, 5d, 7d, 9d), "some doubles")
""")),
      Section("Standard Deviation", Interpret(interpreter, """
import spire.implicits.DoubleAlgebra

standardDeviation(dist)
""")),
      Section("Random Variables",
        Interpret(interpreter,
          "val fairCoin = coin()",
          "val biasedCoin = coin(Rational(9, 10))"),
        <span>
          The<code>observe</code>
          method selects a value for the random variable based on the distribution.
        </span>,
        Interpret(interpreter,
          "(1 to 10) map { i => fairCoin.observe }",
          "(1 to 10) map { i => biasedCoin.observe }"),
        Interpret(interpreter,
          """val flip1 = coin()
val flip2 = coin()

P(flip1 is 'HEAD).apply()""",
          "P((flip1 is 'HEAD) and (flip2 is 'HEAD)).apply()",
          "P((flip1 is 'HEAD) or (flip2 is 'HEAD)).apply()",
          "P((flip1 is 'HEAD) | (flip2 is 'TAIL)).apply()",
          """
import axle.game.Dice._
val d6a = utfD6
val d6b = utfD6
P((d6a is '⚃) and (d6b is '⚃)).apply()""",
          "P((d6a isnt '⚃)).apply()"),
        Interpret(interpreter, "(1 to 10) map { i => utfD6.observe }")),
      Section("Examples",
        <ul>
          <li>{ pageForChapter(TwoDice.chapter).link }</li>
        </ul>))

}
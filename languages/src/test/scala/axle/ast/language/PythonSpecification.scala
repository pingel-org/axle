
package axle.ast.language

import collection._
import axle.ast._
import axle.ast.view.ViewString
import org.specs2.mutable._

import net.liftweb.common.Full
import net.liftweb.json.JsonParser

object PythonSpecification extends Specification {
  "conversion from JSON to AstNode" should {

    "turn 3 into 3" in {

      //JsonAST.fromJson("""{"node": {"spread": [{"expr": {"right": {"type": "Const", "value": 2}, "type": "Add", "left": {"type": "Const", "value": 1}}, "type": "Discard"}], "type": "Stmt"}, "type": "Module"}""")
      //JsonAST.fromJson("""{"node": {"spread": [{"test": {"type": "Name", "name": "True"}, "fail": {"type": "Name", "name": "True"}, "type": "Assert"}], "type": "Stmt"}, "type": "Module"}""")

      JsonAST.fromJson("""{"node": {"spread": [{"test": {"type": "Name", "name": "True"}, "fail": null, "type": "Assert"}], "type": "Stmt"}, "type": "Module"}""")

      // TODO add some assertions about how Python.trim should work

      JsonAST.fromJson(
        """{"type": "Const", "value": 3}"""
      ) must be equalTo (
          AstNodeRule("Const", Map("value" -> AstNodeValue(Some("3"), 1)), 1)
        )

      JsonAST.fromJson(
        """
{
   "node": {
      "spread": [{
         "expr": {"_lineno": 1,
                  "type": "Add",
                  "right": {"_lineno": 1, "type": "Const", "value": "3"},
                  "left": {"_lineno": 1, "type": "Const", "value": "3"}
                 },
         "_lineno": 1,
         "type": "Discard"
      }],
      "_lineno": null,
      "type": "Stmt"
   },
   "_lineno": null,
   "type": "Module"
}
"""
      ) must be equalTo (
          AstNodeRule("Module", Map(
            "node" -> AstNodeRule("Stmt", Map(
              "spread" -> AstNodeList(List(AstNodeRule("Discard", Map(
                "expr" -> AstNodeRule("Add", Map(
                  "right" -> AstNodeRule("Const", Map(
                    "value" -> AstNodeValue(Some("3"), 1)), 1),
                  "left" -> AstNodeRule("Const", Map(
                    "value" -> AstNodeValue(Some("3"), 1)), 1)), 1)), 1)), 1)), 1)), 1)
        )
    }
  }

  val language = Python.language

  "signature extraction" should {

    "emit correct python" in {

      def parseTests =
        "a\n" ::
          "3\n" ::
          "3.0\n" ::
          "a = 'a'\n" ::
          "x = f(a, b)\n" ::
          "a = [f(x) for x in range(3, 4)]\n" ::
          "a = [f(x) for x in range(3, 4) if False]\n" ::
          "y = a.z\n" ::
          "f = {}\n" ::
          "f = dict()\n" ::
          "y = (a, b, c)\n" ::
          "a = x[3:4]\n" ::
          "x = [1, 2, 3]\n" ::
          "f = {a : b, c : d}\n" ::
          "f = 4 + f(3)\n" ::
          "f = 2 - 1\n" ::
          "f = 4 * 3\n" ::
          "f = 2 / 4\n" ::
          "g[3] = f[3] + h[3 * 2]\n" ::
          "global a, b, c\n" ::
          "print fmtstr % (x, y)\n" ::
          "assert True, True\n" ::
          "assert True\n" ::
          "a and b and c\n" ::
          "3 < 4 < 5\n" ::
          "exec code\n" ::
          "exec code in locals(), globals()\n" ::
          "exec code in locals()\n" ::
          "lambda x, y: x * y\n" ::
          "import re\n" ::
          "import bar as b\n" ::
          "import bar as b, baz as c\n" ::
          "from foo import x\n" ::
          "raise\n" ::
          "raise Exception(a)\n" ::
          """try:
   foo()
finally:
   close()
""" ::
          """while x < 5:
   print x
   x += 1
else:
   y = 0
""" ::
          """def f(a, b, c):
   g()
""" ::
          //      """def f(a, b, *c):
          //   g()
          //""" ::
          //      """def f(a, b, **c):
          //   g()
          //""" ::
          //      """def f(a, *b, **c):
          //   g()
          //""" ::
          """@logging
def f(a, b, x=True, y=False):
   return a
""" :: Nil

      // TODO:
      // function's docstring
      // try/except (try: bar() except Exception, e: foo()
      // try/except/finally
      // indentation following else (see BaseHTTPServer.py.html)
      // if's condition not showing up sometimes

      parseTests
        .map(expectedText => {
          language
            .parseString(expectedText)
            .map(ViewString.AstNode(_, language))
            .getOrElse("") must be equalTo (expectedText)
        })

      "todo" must be equalTo ("todo")
    }

  }
}


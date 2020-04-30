package axle.ast.language

import cats.implicits._
import axle.ast._

object Python {

  case class RuleBuilder(name: String) {
    def :=(statement: Statement): Rule = Rule(name, statement)
  }

  implicit val enrichRuleName = (name: String) => RuleBuilder(name)


  val expressions = List(
    "Module" := Sub("node"),
    "Stmt" := Spread(),
    "EmptyNode" := Nop(),
    "Keyword" := SqT(Attr("name"), Sq(Lit("="), Sub("expr"))),
    "Name" := Attr("name"),
    "Const" := Repr("value"),
    "Dict" := Sq(Emb("{", JItems("items", " : ", ", "), "}")),
    "List" := Sq(Emb("[", J("nodes", Sq(Lit(","), Sp())), "]")),
    "Tuple" := Sq(Emb("(", J("nodes", Sq(Lit(","), Sp())), ")")),
    "ListComp" := Sq(Emb("[", Sq(Sub("expr"), Sp(), Kw("for"), Sp(), J("quals", Nop())), "]")),
    "ListCompFor" := Sq(Sub("assign"), Sp(), Kw("in"), Sp(), Sub("list"), Affix("ifs", " ", None)),
    "ListCompIf" := Sq(Kw("if"), Sp(), Sub("test")),
    "Subscript" := Sq(Sub("expr"), Emb("[", J("subs", Sq(Lit(","), Sp())), "]")),
    "Slice" := Sq(Sub("expr"), Emb("[", Sq(Sub("lower"), Lit(":"), Sub("upper")), "]")),
    "Ellipsis" := Kw("..."),
    "Sliceobj" := Nop(),
    "Lambda" := Sq(Kw("lambda"), Sp(), J("argnames", Sq(Lit(","), Sp())), Lit(":"), Sp(), Sub("code")),
    "GenExpr" := Sq(Sub("code")),
    "GenExprFor" := Sq(Kw("for"), Sp(), Sub("assign"), Sp(), Kw("in"), Sp(), Sub("iter"), Sp(), Sub("ifs")),
    "GenExprIf" := Sq(Kw("if"), Sp(), Sub("test")),
    "GenExprInner" := Sq(Sub("expr"), J("quals", Nop())),
    "Getattr" := Sq(Sub("expr"), Op("." /* TODO tight=True */ ), Attr("attrname")),
    "Backquote" := Sq(Emb("`", Sub("expr"), "`")),
    "CallFunc" := Sq(Sub("node"), Emb("(", J("args", Sq(Lit(","), Sp())), ")")),
    "Compare" := Sq(Sub("node"), Sp(), J("args", Sp())),
    "Add" := Sq(Sub("left"), Sp(), Op("+"), Sp(), Sub("right")),
    "AssAttr" := Sq(Sub("expr"), Op("."), Attr("attrname")),
    "Assign" := Sq(J("nodes", Sq(Lit(","), Sp())), Sp(), Op("="), Sp(), Sub("expr")),
    "Invert" := Sq(Op("~"), Sp(), Sub("expr")),
    "UnaryAdd" := Sq(Op("+"), Sp(), Sub("expr")),
    "UnarySub" := Sq(Op("-"), Sp(), Sub("expr")),
    "Not" := Sq(Op("not"), Sp(), Sub("expr")),
    "And" := J("nodes", Sq(Sp(), Op("and"), Sp())),
    "Or" := J("nodes", Sq(Sp(), Op("or"), Sp())),
    "Div" := Sq(Sub("left"), Sp(), Op("/"), Sp(), Sub("right")),
    "FloorDiv" := Sq(Sub("left"), Sp(), Op("//"), Sp(), Sub("right")),
    "LeftShift" := Sq(Sub("left"), Sp(), Op("<<"), Sp(), Sub("right")),
    "Mod" := Sq(Sub("left"), Sp(), Op("%"), Sp(), Sub("right")),
    "Mul" := Sq(Sub("left"), Sp(), Op("*"), Sp(), Sub("right")),
    "Power" := Sq(Sub("left"), Sp(), Op("**"), Sp(), Sub("right")),
    "RightShift" := Sq(Sub("left"), Sp(), Op(">>"), Sp(), Sub("right")),
    "Sub" := Sq(Sub("left"), Sp(), Op("-"), Sp(), Sub("right")),
    "Bitand" := J("nodes", Sq(Sp(), Op("&"), Sp())),
    "Bitor" := J("nodes", Sq(Sp(), Op("|"), Sp())),
    "Bitxor" := J("nodes", Sq(Sp(), Op("^"), Sp())))

  val simpleStatements: List[Rule] = List(
    "Expression" := Nop(),
    "Discard" := Sub("expr"),
    "Pass" := Kw("pass"),
    "Assert" := Sq(Kw("assert"), Sp(), Sub("test"), SqT(Lit(","), Sp(), Sub("fail"))),
    "AssList" := Sq(Emb("[", J("nodes", Sq(Lit(","), Sp())), "]")),
    "AssName" := Attr("name"),
    "AssTuple" := Sq(Emb("(", J("nodes", Sq(Lit(","), Sp())), ")")),
    "AugAssign" := Sq(Sub("node"), Sp(), Attr("op"), Sp(), Sub("expr")),
    "Print" := Sq(Kw("print"), Sp(), J("nodes", Sp())),
    "Printnl" := Sq(Kw("print"), Sp(), J("nodes", Sp())),
    "Return" := Sq(Kw("return"), Sp(), Sub("value")),
    "Yield" := Sq(Kw("yield"), Sp(), Sub("value")),
    "Raise" := Sq(Kw("raise"), SqT(Sp(), Sub("expr1")), SqT(Lit(", "), Sub("expr2"))), // TODO: possibly also expr3.  not sure how to trigger this
    "Break" := Kw("break"),
    "Continue" := Kw("continue"),
    "From" := Sq(Kw("from"), Sp(), Attr("modname"), Sp(), Kw("import"), Sp(), ForDel("names", Sq(VarN(0), SqT(Sp(), Kw("as"), Sp(), VarN(1))), ", ")),
    "Import" := SqT(Kw("import"), Sp(), ForDel("names", Sq(VarN(0), SqT(Sp(), Kw("as"), Sp(), VarN(1))), ", ")),
    "Global" := Sq(Kw("global"), Sp(), J("names", Sq(Lit(","), Sp()))),
    "Exec" := Sq(Kw("exec"), Sp(), Sub("expr"), SqT(Lit(" in "), Sub("locals")), SqT(Lit(", "), Sub("globals"))))

  val if_rule = "If" :=
    Sq(
      For(
      "tests",
      Sq(PosKw("if", "elif"), Sp(), VarN(0), Lit(":"), CR(),
        Indent(),
        VarN(1),
        Dedent())),
      SqT(Kw("else"), Lit(":"), CR(),
        Indent(),
        Sub("else_"),
        Dedent()))

  val for_rule = "For" := Sq(Kw("for"), Sp(), Sub("assign"), Sp(), Kw("in"), Sp(), Sub("list"), Lit(":"), CR(),
    Indent(),
    Sub("body"),
    Dedent(),
    Sq(Kw("else"), Lit(":"), CR(),
      Indent(),
      Sub("else_"),
      Dedent()))

  val ifexp_rule = "IfExp" := Sq(Kw("if"), Sub("test"), Lit(":"), CR(),
    Indent(),
    Sub("then"),
    Dedent(),
    Kw("else"), Lit(":"), CR(),
    Indent(),
    Sub("else_"),
    Dedent())

  val while_rule = "While" := Sq(Kw("while"), Sp(), Sub("test"), Lit(":"), CR(),
    Indent(),
    Sub("body"),
    Dedent(),
    SqT(Kw("else"), Lit(":"), CR(),
      Indent(),
      Sub("else_"),
      Dedent()))

  val with_rule = "With" := Sq(Kw("with"), Sp(), Sub("expr"), Sp(), Kw("as"), SqT(Sp(), Sub("vars")), Lit(":"), CR(),
    Indent(),
    Sub("body"),
    Dedent())

  val tryexcept_rule = "TryExcept" := Sq(Kw("try"), Lit(":"), CR(),
    Indent(),
    Sub("body"),
    Dedent(),
    For(
      "handlers",
      Sq(Dedent(), Kw("except"), Sp(), VarN(0), SqT(Lit(","), Sp(), VarN(1)), Lit(":"), CR(),
        Indent(),
        VarN(2),
        Dedent(),
        Indent())),
    SqT(Kw("else"), Lit(":"), CR(),
      Indent(),
      Sub("else_"),
      Dedent()))

  val tryfinally_rule = "TryFinally" := Sq(Kw("try"), Lit(":"), CR(),
    Indent(),
    Sub("body"),
    Dedent(),
    Kw("finally"), Lit(":"), CR(),
    Indent(),
    Sub("final"),
    Dedent())

  val class_rule = "Class" := Sq(Kw("class"), Sp(), Attr("name"), Emb("(", J("bases", Sq(Lit(","), Sp())), ")"), Lit(":"), CR(),
    CRH(),
    Indent(),
    SqT(Repr("doc"), CR()),
    Sub("code"),
    Dedent(), CRH())

  val function_rule = "Function" := Sq(
    Sub("decorators"), // CR()), J decorators with CR?
    Kw("def"), Sp(), Attr("name"), Emb("(", Arglist(), ")"), Lit(":"), CR(),
    Indent(),
    SqT(Repr("doc"), CR()),
    Sub("code"),
    Dedent(), CRH())

  val decorators_rule = "Decorators" := For("nodes", Sq(Lit("@"), Var(), CR()))

  val compoundStatements: List[Rule] =
    if_rule :: for_rule :: ifexp_rule :: while_rule :: with_rule :: tryexcept_rule ::
      tryfinally_rule :: class_rule :: function_rule :: decorators_rule :: Nil

  val precedence = List(
    (List("Lambda"), "left"),
    (List("Or"), "left"),
    (List("And"), "left"),
    (List("Not"), "left"),
    (List("Compare"), "left"),
    (List("Bitor"), "left"),
    (List("Bitxor"), "left"),
    (List("Bitand"), "left"),
    (List("LeftShift", "RightShift"), "left"),
    (List("Add", "Sub"), "left"),
    (List("Mul", "Div", "FloorDiv", "Mod"), "left"),
    (List("UnaryAdd", "UnarySub", "Invert"), "left"),
    (List("Power"), "right"),
    (List("Subscript", "Slice", "CallFunc", "Getattr"), "left"),
    (List("Tuple", "List", "Dict", "Backquote"), "left"))

  val parse: String => Option[AstNode] =

    (code: String) => {

      val ast2json = "./axle-wheel/src/main/python/python2json.py"

      val pb = new ProcessBuilder(ast2json)
      val p = pb.start()
      val os = p.getOutputStream()
      os.write(code.getBytes)
      os.close

      val is = p.getInputStream()
      val json = axle.IO.convertStreamToString(is)
      val rc = p.waitFor()
      if (rc != 0) {
        throw new Exception("error parsing python or converting it to json")
      }

      Some(JsonAST.fromJson(json))
    }

  // trim:
  // AstNodeRule("Module", Map("node" -> AstNodeRule("Stmt", Map("spread" -> AstNodeList(List(_))))))

  val trim = (ast: AstNode) => ast match {

    case AstNodeRule("Module", topmap, _) => {
      topmap("node").asInstanceOf[AstNodeRule] match {
        case AstNodeRule("Stmt", stmtmap, _) => {
          val mnl = stmtmap("spread").asInstanceOf[AstNodeList]
          if (mnl.list.length === 1) {
            mnl.list.head match {
              case AstNodeRule("Discard", cm, _) => cm("expr")
              case _                             => mnl.list.head
            }
          } else {
            mnl
          }
        }
        case _ => throw new Exception("expected a Stmt as Module's node")
      }
    }
    case _ => throw new Exception("expected to find a Module at the top of the ast")
  }

  val lang = new Language {

    def name: String = "python"

    def rules: List[Rule] = expressions ++ simpleStatements ++ compoundStatements

    def precedenceGroups: List[(List[String], String)] = precedence

    def parser: String => Option[AstNode] = parse

    def trimmer: AstNode => AstNode = trim

  }

  def language: Language = lang

}


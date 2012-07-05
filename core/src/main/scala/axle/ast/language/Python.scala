package axle.ast.language

import axle.ast._
import util.matching.Regex

object Python {

  val expressions =
    new Rule("Module", Sub("node")) ::
      new Rule("Stmt", Spread()) ::
      new Rule("EmptyNode", Nop()) ::
      new Rule("Keyword", SqT(Attr("name"), Sq(Lit("="), Sub("expr")))) ::
      new Rule("Name", Attr("name")) ::
      new Rule("Const", Repr("value")) ::
      new Rule("Dict", Sq(Emb("{", JItems("items", " : ", ", "), "}"))) ::
      new Rule("List", Sq(Emb("[", J("nodes", Sq(Lit(","), Sp())), "]"))) ::
      new Rule("Tuple", Sq(Emb("(", J("nodes", Sq(Lit(","), Sp())), ")"))) ::
      new Rule("ListComp", Sq(Emb("[", Sq(Sub("expr"), Sp(), Kw("for"), Sp(), J("quals", Nop())), "]"))) ::
      new Rule("ListCompFor", Sq(Sub("assign"), Sp(), Kw("in"), Sp(), Sub("list"), Affix("ifs", " ", None))) ::
      new Rule("ListCompIf", Sq(Kw("if"), Sp(), Sub("test"))) ::
      new Rule("Subscript", Sq(Sub("expr"), Emb("[", J("subs", Sq(Lit(","), Sp())), "]"))) ::
      new Rule("Slice", Sq(Sub("expr"), Emb("[", Sq(Sub("lower"), Lit(":"), Sub("upper")), "]"))) ::
      new Rule("Ellipsis", Kw("...")) ::
      new Rule("Sliceobj", Nop()) ::
      new Rule("Lambda", Sq(Kw("lambda"), Sp(),
        J("argnames", Sq(Lit(","), Sp())), Lit(":"), Sp(), Sub("code"))) ::
      new Rule("GenExpr", Sq(Sub("code"))) ::
      new Rule("GenExprFor", Sq(Kw("for"), Sp(), Sub("assign"), Sp(), Kw("in"), Sp(),
        Sub("iter"), Sp(), Sub("ifs"))) ::
      new Rule("GenExprIf", Sq(Kw("if"), Sp(), Sub("test"))) ::
      new Rule("GenExprInner", Sq(Sub("expr"), J("quals", Nop()))) ::
      new Rule("Getattr", Sq(Sub("expr"), Op("." /* TODO tight=True */ ), Attr("attrname"))) ::
      new Rule("Backquote", Sq(Emb("`", Sub("expr"), "`"))) ::
      new Rule("CallFunc", Sq(Sub("node"), Emb("(", J("args", Sq(Lit(","), Sp())), ")"))) ::
      new Rule("Compare", Sq(Sub("node"), Sp(), J("args", Sp()))) ::
      new Rule("Add", Sq(Sub("left"), Sp(), Op("+"), Sp(), Sub("right"))) ::
      new Rule("AssAttr", Sq(Sub("expr"), Op("."), Attr("attrname"))) ::
      new Rule("Assign", Sq(J("nodes", Sq(Lit(","), Sp())), Sp(), Op("="), Sp(), Sub("expr"))) ::
      new Rule("Invert", Sq(Op("~"), Sp(), Sub("expr"))) ::
      new Rule("UnaryAdd", Sq(Op("+"), Sp(), Sub("expr"))) ::
      new Rule("UnarySub", Sq(Op("-"), Sp(), Sub("expr"))) ::
      new Rule("Not", Sq(Op("not"), Sp(), Sub("expr"))) ::
      new Rule("And", J("nodes", Sq(Sp(), Op("and"), Sp()))) ::
      new Rule("Or", J("nodes", Sq(Sp(), Op("or"), Sp()))) ::
      new Rule("Div", Sq(Sub("left"), Sp(), Op("/"), Sp(), Sub("right"))) ::
      new Rule("FloorDiv", Sq(Sub("left"), Sp(), Op("//"), Sp(), Sub("right"))) ::
      new Rule("LeftShift", Sq(Sub("left"), Sp(), Op("<<"), Sp(), Sub("right"))) ::
      new Rule("Mod", Sq(Sub("left"), Sp(), Op("%"), Sp(), Sub("right"))) ::
      new Rule("Mul", Sq(Sub("left"), Sp(), Op("*"), Sp(), Sub("right"))) ::
      new Rule("Power", Sq(Sub("left"), Sp(), Op("**"), Sp(), Sub("right"))) ::
      new Rule("RightShift", Sq(Sub("left"), Sp(), Op(">>"), Sp(), Sub("right"))) ::
      new Rule("Sub", Sq(Sub("left"), Sp(), Op("-"), Sp(), Sub("right"))) ::
      new Rule("Bitand", J("nodes", Sq(Sp(), Op("&"), Sp()))) ::
      new Rule("Bitor", J("nodes", Sq(Sp(), Op("|"), Sp()))) ::
      new Rule("Bitxor", J("nodes", Sq(Sp(), Op("^"), Sp()))) ::
      Nil;

  val simpleStatements: List[Rule] =
    new Rule("Expression", Nop()) ::
      new Rule("Discard", Sub("expr")) ::
      new Rule("Pass", Kw("pass")) ::
      new Rule("Assert", Sq(Kw("assert"), Sp(), Sub("test"), SqT(Lit(","), Sp(), Sub("fail")))) ::
      new Rule("AssList", Sq(Emb("[", J("nodes", Sq(Lit(","), Sp())), "]"))) ::
      new Rule("AssName", Attr("name")) ::
      new Rule("AssTuple", Sq(Emb("(", J("nodes", Sq(Lit(","), Sp())), ")"))) ::
      new Rule("AugAssign", Sq(Sub("node"), Sp(), Attr("op"), Sp(), Sub("expr"))) ::
      new Rule("Print", Sq(Kw("print"), Sp(), J("nodes", Sp()))) ::
      new Rule("Printnl", Sq(Kw("print"), Sp(), J("nodes", Sp()))) ::
      new Rule("Return", Sq(Kw("return"), Sp(), Sub("value"))) ::
      new Rule("Yield", Sq(Kw("yield"), Sp(), Sub("value"))) ::
      new Rule("Raise", Sq(Kw("raise"), SqT(Sp(), Sub("expr1")), SqT(Lit(", "), Sub("expr2")))) :: // TODO: possibly also expr3.  not sure how to trigger this
      new Rule("Break", Kw("break")) ::
      new Rule("Continue", Kw("continue")) ::
      new Rule("From", Sq(Kw("from"), Sp(), Attr("modname"), Sp(), Kw("import"), Sp(),
        ForDel("names", Sq(VarN(0), SqT(Sp(), Kw("as"), Sp(), VarN(1))), ", "))) ::
      new Rule("Import", SqT(Kw("import"), Sp(), ForDel("names", Sq(VarN(0), SqT(Sp(), Kw("as"), Sp(), VarN(1))), ", "))) ::
      new Rule("Global", Sq(Kw("global"), Sp(), J("names", Sq(Lit(","), Sp())))) ::
      new Rule("Exec", Sq(Kw("exec"), Sp(), Sub("expr"), SqT(Lit(" in "), Sub("locals")), SqT(Lit(", "), Sub("globals")))) ::
      Nil;

  val if_rule = new Rule("If",
    Sq(For("tests",
      Sq(PosKw("if", "elif"), Sp(), VarN(0), Lit(":"), CR(),
        Indent(),
        VarN(1),
        Dedent())),
      SqT(Kw("else"), Lit(":"), CR(),
        Indent(),
        Sub("else_"),
        Dedent())))

  val for_rule = new Rule("For", Sq(Kw("for"), Sp(), Sub("assign"), Sp(), Kw("in"), Sp(), Sub("list"), Lit(":"), CR(),
    Indent(),
    Sub("body"),
    Dedent(),
    Sq(Kw("else"), Lit(":"), CR(),
      Indent(),
      Sub("else_"),
      Dedent())
  ))

  val ifexp_rule = new Rule("IfExp", Sq(Kw("if"), Sub("test"), Lit(":"), CR(),
    Indent(),
    Sub("then"),
    Dedent(),
    Kw("else"), Lit(":"), CR(),
    Indent(),
    Sub("else_"),
    Dedent()))

  val while_rule = new Rule("While", Sq(Kw("while"), Sp(), Sub("test"), Lit(":"), CR(),
    Indent(),
    Sub("body"),
    Dedent(),
    SqT(Kw("else"), Lit(":"), CR(),
      Indent(),
      Sub("else_"),
      Dedent())
  ))

  val with_rule = new Rule("With", Sq(Kw("with"), Sp(), Sub("expr"), Sp(), Kw("as"), SqT(Sp(), Sub("vars")), Lit(":"), CR(),
    Indent(),
    Sub("body"),
    Dedent()))

  val tryexcept_rule = new Rule("TryExcept", Sq(Kw("try"), Lit(":"), CR(),
    Indent(),
    Sub("body"),
    Dedent(),
    For("handlers",
      Sq(Dedent(), Kw("except"), Sp(), VarN(0), SqT(Lit(","), Sp(), VarN(1)), Lit(":"), CR(),
        Indent(),
        VarN(2),
        Dedent(),
        Indent())),
    SqT(Kw("else"), Lit(":"), CR(),
      Indent(),
      Sub("else_"),
      Dedent())
  ))

  val tryfinally_rule = new Rule("TryFinally", Sq(Kw("try"), Lit(":"), CR(),
    Indent(),
    Sub("body"),
    Dedent(),
    Kw("finally"), Lit(":"), CR(),
    Indent(),
    Sub("final"),
    Dedent()
  ))

  val class_rule = new Rule("Class", Sq(Kw("class"), Sp(), Attr("name"), Emb("(", J("bases", Sq(Lit(","), Sp())), ")"), Lit(":"), CR(),
    CRH(),
    Indent(),
    SqT(Repr("doc"), CR()),
    Sub("code"),
    Dedent(), CRH()
  ))

  val function_rule = new Rule("Function", Sq(
    Sub("decorators"), // CR()), J decorators with CR?
    Kw("def"), Sp(), Attr("name"), Emb("(", Arglist(), ")"), Lit(":"), CR(),
    Indent(),
    SqT(Repr("doc"), CR()),
    Sub("code"),
    Dedent(), CRH()
  ))

  val decorators_rule = new Rule("Decorators", For("nodes", Sq(Lit("@"), Var(), CR())))

  val compoundStatements: List[Rule] =
    if_rule :: for_rule :: ifexp_rule :: while_rule :: with_rule :: tryexcept_rule ::
      tryfinally_rule :: class_rule :: function_rule :: decorators_rule :: Nil

  val precedence = List((List("Lambda"), "left"),
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
    (List("Tuple", "List", "Dict", "Backquote"), "left")
  )

  val parse: String => Option[MetaNode] =
    
    (code: String) => {

      val ast2json = "python2json.py"

      val pb = new ProcessBuilder(ast2json)
      val p = pb.start()
      val os = p.getOutputStream()
      os.write(code.getBytes)
      os.close

      val is = p.getInputStream()
      val json = Util.convertStreamToString(is)
      val rc = p.waitFor()
      if (rc != 0) {
        throw new Exception("error parsing python or converting it to json")
      }

      Some(MetaNode.fromJson(json))
    }

  // trim:
  // MetaNodeRule("Module", Map("node" -> MetaNodeRule("Stmt", Map("spread" -> MetaNodeList(List(_))))))

  val trim = (ast: MetaNode) => ast match {

    case MetaNodeRule("Module", topmap, _) => {
      topmap("node").asInstanceOf[MetaNodeRule] match {
        case MetaNodeRule("Stmt", stmtmap, _) => {
          val mnl = stmtmap("spread").asInstanceOf[MetaNodeList]
          if (mnl.list.length == 1) {
            mnl.list.head match {
              case MetaNodeRule("Discard", cm, _) => cm("expr")
              case _ => mnl.list.head
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

  val lang = new Language(
    "python",
    expressions ++ simpleStatements ++ compoundStatements,
    precedence,
    parse,
    trim
  )

  def language = lang

}


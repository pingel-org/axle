#!/usr/bin/env python

import compiler
 
class Ast2Raw:

    def __init__(self):
        self.result = None

    def visitModule(self, t):
        compiler.walk(t.node, self) 
        d_node = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Module',
            "node" : d_node # Sub
            }

    def visitStmt(self, t):
        stmts = []
        for i in t:
             compiler.walk(i, self)
             stmts.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type': 'Stmt',
            'spread' : stmts
            }

    def visitEmptyNode(self, t):
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'EmptyNode'
            }

    def visitKeyword(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Keyword',
            'name' : t.name,
            'expr' : d_expr # Sub
            }

    def visitName(self, t):
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Name',
            'name' : t.name
            }

    def visitConst(self, t):
        v = repr(t.value)
        #if isinstance(v, unicode):
        #    v = unicode(v, "unicode_escape").encode('utf-8')
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Const',
            'value' : v
            }
 
    def visitAssName(self, t):
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'AssName',
            'name' : t.name
            }

    def visitDict(self, t):
        d_items = []
        for (f, s) in t.items:
            compiler.walk(f, self)
            d_f = self.result
            compiler.walk(s, self)
            d_s = self.result
            d_items.append((d_f, d_s))
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Dict',
            'items' : d_items # JItems
            }

    def visitList(self, t):
        d_nodes = []
        for n in t.nodes:
            compiler.walk(n, self)
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'List',
            'nodes' : d_nodes # J
            }

    def visitTuple(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self)
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Tuple',
            'nodes' : d_nodes # J
            }

    def visitListComp(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        d_quals = []
        for qual in t.quals:
            compiler.walk(qual, self)
            d_quals.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'ListComp',
            'expr' : d_expr,  # Sub
            'quals' : d_quals # J
            }

    def visitListCompFor(self, t):
        compiler.walk(t.assign, self)
        d_assign = self.result
        compiler.walk(t.list, self)
        d_list = self.result
        d_ifs = []
        for iff in t.ifs:
            compiler.walk(iff, self)
            d_ifs.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'ListCompFor',
            'assign' : d_assign,  # Sub
            'list' : d_list,      # Sub
            'ifs' : d_ifs         # Affix
            }

    def visitListCompIf(self, t):
        compiler.walk(t.test, self) # Sub
        d_test = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'ListCompIf',
            'test' : d_test
            }

    def visitSubscript(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        d_subs = []
        for sub in t.subs:
            compiler.walk(sub, self)
            d_subs.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Subscript',
            'expr' : d_expr, # Sub
            'subs' : d_subs  # J
            }

    def visitSlice(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        d_lower = None
        if t.lower:
            compiler.walk(t.lower, self)
            d_lower = self.result
        d_upper = None
        if t.upper:
            compiler.walk(t.upper, self)
            d_upper = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Slice',
            'expr' : d_expr,   # Sub
            'lower' : d_lower, # Sub
            'upper' : d_upper  # Sub
            }

    def visitEllipsis(self, t):
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Ellipsis'
            }

    def visitSliceobj(self, t):
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Sliceobj'
            }

    def visitLambda(self, t):
        compiler.walk(t.code, self)
        d_code = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Lambda',
            'argnames' : t.argnames, # J
            'code' : d_code          # Sub
            }

    def visitGenExpr(self, t):
        compiler.walk(t.code, self)
        d_code = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'GenExpr',
            'code' : d_code   # Sub
            }

    def visitGenExprFor(self, t):
        compiler.walk(t.assign, self)
        d_assign = self.result
        compiler.walk(t.iter, self)
        d_iter = self.result
        d_ifs = []
        for iff in t.ifs:
            compiler.walk(iff, self)
            d_ifs.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'GenExprFor',
            'assign' : d_assign, # Sub
            'iter' : d_iter,     # Sub
            'ifs' : d_ifs,       # Sub
            }

    def visitGenExprIf(self, t):
        compiler.walk(t.test, self)
        d_test = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'GenExprIf',
            'test' : d_test # Sub
            }

    def visitGenExprInner(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        d_quals = []
        for qual in t.quals:
            compiler.walk(qual, self)
            d_quals.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'GenExprInner',
            'expr' : d_expr,  # Sub
            'quals' : d_quals # J
            }

    def visitGetattr(self, t):
        compiler.walk(t.expr, self) # Sub
        d_expr = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Getattr',
            'expr' : d_expr,
            'attrname' : t.attrname
            }

    def visitBackquote(self, t):
        compiler.walk(t.expr, self) # Sub
        d_expr = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Backquote',
            'expr' : d_expr
            }

    def visitCallFunc(self, t):
        compiler.walk(t.node, self)
        d_node = self.result
        d_args = []
        for arg in t.args:
            compiler.walk(arg, self)
            d_args.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'CallFunc',
            'node' : d_node, # Sub
            'args' : d_args  # J
            }

    def visitCompare(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        d_ops = []
        for op in t.ops:
            d_op0 = op[0] # TODO: this is a string -- not a node
            compiler.walk(op[1], self)
            d_op1 = self.result
            d_ops.append((d_op0, d_op1))
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Compare',
            'node' : d_expr, # Sub
            'args' : d_ops   # J
            }

    def visitAdd(self, t):
        compiler.walk(t.left, self)
        d_left = self.result
        compiler.walk(t.right, self)
        d_right = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Add',
            'left' : d_left,  # Sub
            'right' : d_right # Sub
            }

    def visitAssAttr(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'AssAttr',
            'expr' : d_expr, # Sub
            'attrname' : t.attrname # Attr
            }

    def visitAssign(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self)
            d_nodes.append(self.result)
        compiler.walk(t.expr, self)
        d_expr = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Assign',
            'nodes' : d_nodes, # J
            'expr' : d_expr # Sub
            }

    def visitInvert(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Invert',
            'expr' : d_expr # Sub
            }

    def visitUnaryAdd(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'UnaryAdd',
            'expr' : d_expr # Sub
            }

    def visitUnarySub(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'UnarySub',
            'expr' : d_expr # Sub
            }

    def visitNot(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        self.result = {
            'type' : 'Not',
            'expr' : d_expr # Sub
            }

    def visitAnd(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self)
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'And',
            'nodes' : d_nodes # J
            }
        
    def visitOr(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self)
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Or',
            'nodes' : d_nodes # J
            }

    def visitDiv(self, t):
        compiler.walk(t.left, self)
        d_left = self.result
        compiler.walk(t.right, self)
        d_right = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Div',
            'left' : d_left,  # Sub
            'right' : d_right # Sub
            }

    def visitFloorDiv(self, t):
        compiler.walk(t.left, self) # Sub
        d_left = self.result
        compiler.walk(t.right, self) # Sub
        d_right = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'FloorDiv',
            'left' : d_left,
            'right' : d_right
            }

    def visitLeftShift(self, t):
        compiler.walk(t.left, self)
        d_left = self.result
        compiler.walk(t.right, self)
        d_right = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'LeftShift',
            'left' : d_left,  # Sub
            'right' : d_right # Sub
            }

    def visitMod(self, t):
        compiler.walk(t.left, self)
        d_left = self.result
        compiler.walk(t.right, self)
        d_right = self.result
        self.result = { 
            '_lineno' : t.lineno,
            'type' : 'Mod', 
            'left' : d_left,   # Sub
            'right' : d_right  # Sub
            }

    def visitMul(self, t):
        compiler.walk(t.left, self) # Sub
        d_left = self.result
        compiler.walk(t.right, self) # Sub
        d_right = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Mul', 
            'left' : d_left, 
            'right' : d_right
            }

    def visitPower(self, t):
        compiler.walk(t.left, self) # Sub
        d_left = self.result
        compiler.walk(t.right, self) # Sub
        d_right = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Power',
            'left' : d_left,
            'right' : d_right 
            }

    def visitRightShift(self, t):
        compiler.walk(t.left, self)
        d_left = self.result
        compiler.walk(t.right, self)
        d_right = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'RightShift',
            'left' : d_left,  # Sub
            'right' : d_right # Sub
            }

    def visitSub(self, t):
        compiler.walk(t.left, self)
        d_left = self.result
        compiler.walk(t.right, self)
        d_right = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Sub',
            'left' : d_left, # Sub
            'right' : d_right # Sub
            }

    def visitBitand(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self)
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Bitand',
            'nodes' : d_nodes # J
            }

    def visitBitor(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self)
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Bitor',
            'nodes' : d_nodes # J
            }

    def visitBitxor(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self)
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Bitxor',
            'nodes' : d_nodes # J
            }

    # EXPRESSIONS

    def visitExpression(self, t):
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Expression'
            }

    def visitDiscard(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Discard',
            'expr' : d_expr  # Sub
            }

    def visitPass(self, t):
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Pass'
            }

    def visitAssert(self, t):
        compiler.walk(t.test, self)
        d_test = self.result
        d_fail = None
        if t.fail:
            compiler.walk(t.fail, self)
            d_fail = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Assert',
            'test' : d_test, # Sub
            'fail' : d_fail  # Sub
            }

    def visitAssList(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self) # J
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'AssList',
            'nodes' : d_nodes
            }

    def visitAssName(self, t):
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'AssName',
            'name' : t.name
            }

    def visitAssTuple(self, t):
        d_nodes = []
        for i in t.nodes:
            compiler.walk(i, self)
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'AssTuple',
            'nodes' : d_nodes # J
            }

    def visitAugAssign(self, t):
        compiler.walk(t.node, self)
        d_node = self.result
        compiler.walk(t.expr, self)
        d_expr = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'AugAssign',
            'node' : d_node, # Sub
            'op' : t.op,
            'expr' : d_expr  # Sub
            }
        
    def visitPrint(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self) # J
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Print',
            'nodes' : d_nodes
            }

    def visitPrintnl(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self)
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Printnl',
            'nodes' : d_nodes   # J
            }

    def visitReturn(self, t):
        compiler.walk(t.value, self)
        d_value = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Return',
            'value' : d_value  # Sub
            }

    def visitYield(self, t):
        compiler.walk(t.value, self)
        d_value = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Yield',
            'value' : d_value  # Sub
            }

    def visitRaise(self, t):
        d_expr1 = None
        if t.expr1:
            compiler.walk(t.expr1, self)
            d_expr1 = self.result
        d_expr2 = None
        if d_expr2:
            compiler.walk(t.expr2, self)
            d_expr2 = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Raise',
            'expr1' : d_expr1, # Sub
            'expr2' : d_expr2  # Sub
            }

    def visitBreak(self, t):
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Break'
            }

    def visitContinue(self, t):
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Continue'
            }

    def visitFrom(self, t):
        #d_names = []
        # >>> ast.node.nodes[0].names
        # [('x', None), ('y', None)]
        #for (n0, n1) in t.names:
            #compiler.walk(n0, self)
            #d_n0 = self.result
            #compiler.walk(n1, self)
            #d_n1 = self.result
            #d_names.append((n0, d_n1))
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'From',
            'modname' : t.modname, # Attr
            'names' : t.names # d_names # ForDel(VarN(0), VarN(1))
            }

    def visitImport(self, t):
        #d_names = []
        #for (f, s) in t.names:
        #    d_names.append((f, s))
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Import',
            'names' : t.names # ForDel
            }

    def visitGlobal(self, t):
        #d_names = []
        #for name in t.names:
        #    compiler.walk(name, self)
        #    d_names.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Global',
            'names' : t.names # J
            }

    def visitExec(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        d_locals = None
        if t.locals:
            compiler.walk(t.locals, self)
            d_locals = self.result
        d_globals = None
        if t.globals:
            compiler.walk(t.globals, self)
            d_globals = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Exec',
            'expr' : d_expr,      # Sub
            'locals' : d_locals,  # Sub
            'globals' : d_globals # Sub
            }

    def visitIf(self, t):
        d_else_ = None
        if t.else_:
            compiler.walk(t.else_, self)
            d_else_ = self.result
        d_tests = []
        for (t0, t1) in t.tests:
            compiler.walk(t0, self)
            d_t0 = self.result
            compiler.walk(t1, self)
            d_t1 = self.result
            d_tests.append((d_t0, d_t1))
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'If',
            'tests' : d_tests, # For(VarN(0), VarN(1))
            'else_' : d_else_  # Sub
            }
        
    def visitFor(self, t):
        compiler.walk(t.assign, self)
        d_assign = self.result
        compiler.walk(t.list, self)
        d_list = self.result
        compiler.walk(t.body, self)
        d_body = self.result
        d_else_ = None
        if t.else_:
            compiler.walk(t.else_, self)
            d_else_ = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'For',
            'assign' : d_assign, # Sub
            'list' : d_list,     # Sub
            'body' : d_body,     # Sub
            'else_' : d_else_    # Sub
            }

    def visitIfExp(self, t):
        compiler.walk(t.test, self)
        d_test = self.result
        compiler.walk(t.then, self)
        d_then = self.result
        compiler.walk(t.else_, self)
        d_else_ = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'IfExp',
            'test' : d_test,    # Sub
            'then' : d_then,    # Sub
            'else_' : d_else_   # Sub
            }

    def visitWhile(self, t):
        compiler.walk(t.test, self)
        d_test = self.result
        compiler.walk(t.body, self)
        d_body = self.result
        d_else_ = None
        if t.else_:
            compiler.walk(t.else_, self)
            d_else_ = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'While',
            'test' : d_test,   # Sub
            'body' : d_body,   # Sub
            'else_' : d_else_  # Sub
            }

    def visitWith(self, t):
        compiler.walk(t.expr, self)
        d_expr = self.result
        d_vars = []
        if t.vars:
            compiler.walk(t.vars, self)
            d_vars = self.result
        compiler.walk(t.body, self)
        d_body = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'With',
            'expr' : d_expr, # Sub
            'vars' : d_vars, # Sub
            'body' : d_body  # Sub
            }

    def visitTryExcept(self, t):
        compiler.walk(t.body, self)
        d_body = self.result
        d_handlers = []
        for (h0, h1, h2) in t.handlers:
            d_h0 = None
            if h0:
                compiler.walk(h0, self)
                d_h0 = self.result
            d_h1 = None
            if h1:
                compiler.walk(h1, self)
                d_h1 = self.result
            compiler.walk(h2, self)
            d_h2 = self.result
            d_handlers.append((d_h0, d_h1, d_h2))
        d_else_ = None
        if t.else_:
            compiler.walk(t.else_, self)
            d_else_ = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'TryExcept',
            'body' : d_body,         # Sub
            'handlers' : d_handlers, # For(VarN(0), VarN(1), VarN(2))
            'else_' : d_else_        # Sub
            }

    def visitTryFinally(self, t):
        compiler.walk(t.body, self)
        d_body = self.result
        compiler.walk(t.final, self)
        d_final = self.result
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'TryFinally',
            'body' : d_body,  # Sub
            'final' : d_final # Sub
            }

    def visitClass(self, t):
        d_bases = []
        for base in t.bases:
            compiler.walk(base, self)
            d_bases.append(self.result)
        compiler.walk(t.code, self)
        d_code = self.result
        doc = t.doc
        if doc: # isinstance(doc, unicode):
            doc = repr(doc) # unicode(v, "unicode_escape").encode('utf-8')
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Class',
            'name' : t.name,   # Attr
            'bases' : d_bases, # J
            'doc' : doc,     # Repr
            'code' : d_code    # Sub
            }

    def visitFunction(self, t):
        d_decorators = None
        if( t.decorators ):
            compiler.walk(t.decorators, self)
            d_decorators = self.result
        d_argnames = []
        for argname in t.argnames:
            d_argnames.append(argname)
        d_defaults = []
        for default in t.defaults:
            compiler.walk(default, self)
            d_defaults.append(self.result)
        compiler.walk(t.code, self)
        d_code = self.result
        doc = t.doc
        if doc: # isinstance(doc, unicode):
            doc = repr(doc) # unicode(v, "unicode_escape").encode('utf-8')
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Function',
            'decorators' : d_decorators, # Sub
            'name' : t.name,             # Attr
            'argnames' : d_argnames,     # see Arglist
            'defaults' : d_defaults,     # see Arglist
            'flags' : t.flags,
            'doc' : doc,                 # Repr
            'code' : d_code              # Sub
            }

    def visitDecorators(self, t):
        d_nodes = []
        for node in t.nodes:
            compiler.walk(node, self)
            d_nodes.append(self.result)
        self.result = {
            '_lineno' : t.lineno,
            'type' : 'Decorators',
            'nodes' : d_nodes # For(Var)
            }
 
def ast2dict(node):
    v = Ast2Raw()
    return compiler.walk(node, v).result


from optparse import OptionParser

args_parser = OptionParser()
args_parser.add_option("-r", "--repr", action="store_true", dest="pyrepr", default=False,
                       help="print repr of python structure instead of json")
args_parser.add_option("-f", "--filename",
                       dest="filename",
                       help="name of python file to parse (uses STDIN if none specified)"
                       )


(options, args) = args_parser.parse_args()

# filename = args[0]

def get_ast():
    if options.filename:
        with open(options.filename, 'r') as f:
            return compiler.parse("".join(f.readlines()))
    else:
        import sys
        return compiler.parse("".join(sys.stdin))
        
ast = get_ast()
d = ast2dict(ast)

if options.pyrepr:
    import pprint
    pp = pprint.PrettyPrinter(indent=4)
    pp.pprint(d)
else:
    # import simplejson
    # print simplejson.dumps(d, check_circular=False, encoding='utf-8')
    import json
    print json.dumps(d, encoding='utf-8')
    # | python -mjson.tool

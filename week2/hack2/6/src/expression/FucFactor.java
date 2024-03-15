package expression;

import mainhandle.Handle;
import mainhandle.Lexer;
import mainhandle.Parser;
import simplify.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class FucFactor implements Factor {
    private final String realFunc;
    private Expr expr;

    public FucFactor(String funcName, ArrayList<Factor> factorList) {
        String realFunc1 = FuncHandle.realFunc(funcName, factorList);
        Handle handle = new Handle(realFunc1);
        realFunc = handle.secondHandle();
        Lexer lexer = new Lexer(realFunc);
        Parser parser = new Parser(lexer);
        this.expr = parser.parseExpr();
    }

    @Override
    public Poly transPoly() {
        return expr.transPoly();
    }

    @Override
    public void changeIndex(BigInteger index) {
        expr.changeIndex(index);
    }
}

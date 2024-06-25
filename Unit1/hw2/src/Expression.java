import java.math.BigInteger;
import java.util.ArrayList;

public class Expression implements Factor {
    private ArrayList<Term> termList;

    public Expression() {
        this.termList = new ArrayList<>();
    }

    public Expression(ArrayList<Term> newTermList) {
        this.termList = newTermList;
    }

    public Expression ExprSimplify() {
        Expression finalExpr = new Expression();
        for (Term term : termList) {
            finalExpr.AddExpression(term.TermSimplify());
        }

        return finalExpr;
    }

    public int GetTermListSize() {
        return this.termList.size();
    }

    public void AddExpression(Expression newExpr) {
        for (Term newTerm : newExpr.termList) {
            this.AddTerm(newTerm, true);
        }
    }

    public void AddTerm(Term newTerm, boolean isAdd) {
        boolean havaAdded = false;
        if (!isAdd) {
            newTerm.TermInversion();
        }

        for (Term term : termList) {
            if (term.IfSameTerm(newTerm)) {
                term.AddTerm(newTerm);
                havaAdded = true;
                break;
            }
        }

        if (!havaAdded) {
            termList.add(newTerm);
        }
    }

    public Expression MulExpr(Expression otherExpr) {
        Expression finalExpression = new Expression();

        for (Term thisTerm : termList) {
            for (Term otherTerm : otherExpr.termList) {
                finalExpression.AddTerm(thisTerm.CopyTerm().MulTerm(otherTerm), true);
            }
        }

        return finalExpression;
    }

    public BigInteger GetExpressionGcd() {
        if (this.termList.isEmpty()) {
            return BigInteger.ONE;
        }

        BigInteger gcd = BigInteger.ZERO;
        for (Term term : this.termList) {
            gcd = term.GetTermGcd(gcd);
        }

        return gcd;
    }

    public Expression GetExpressionDivideNum(BigInteger num) {
        Expression expression = new Expression();
        for (Term term : this.termList) {
            expression.termList.add(term.GetTermDivideNum(num));
        }
        return expression;
    }

    @Override
    public Factor CopyFactor() {
        ArrayList<Term> newList = new ArrayList<>();
        for (Term term : termList) {
            newList.add(term.CopyTerm());
        }

        return new Expression(newList);
    }

    @Override
    public boolean equals(Factor otherFactor) {
        if (!otherFactor.getClass().getSimpleName().equals("Expression")) {
            return false;
        }

        ArrayList<Term> thisTermList = this.ExprSimplify().termList;
        ArrayList<Term> otherTermList = ((Expression) otherFactor).ExprSimplify().termList;

        if (thisTermList.size() != otherTermList.size()) {
            return false;
        }
        for (Term term : termList) {
            boolean ifContain = false;
            for (Term otherTerm : otherTermList) {
                if (term.equals(otherTerm)) {
                    ifContain = true;
                    break;
                }
            }
            if (!ifContain) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        termList.removeIf(term -> "0".equals(term.toString()));

        StringBuilder stringBuilder = new StringBuilder();
        int skipPos = GetSkipPos(stringBuilder);
        boolean havePrinted = (skipPos >= 0);
        this.PrintExpression(stringBuilder, skipPos, havePrinted);

        return stringBuilder.toString();
    }

    private int GetSkipPos(StringBuilder stringBuilder) {
        int skipPos = -1;
        if (!this.termList.isEmpty() && this.termList.get(0).toString().charAt(0) == '-') {
            for (int i = 0; i < this.termList.size(); i++) {
                String termString = this.termList.get(i).toString();
                if (termString.charAt(0) != '-') {
                    stringBuilder.append(termString);
                    skipPos = i;
                    break;
                }
            }
        }

        return skipPos;
    }

    private void PrintExpression(StringBuilder stringBuilder, int skipPos, boolean ifHavePrinted) {
        boolean havePrinted = ifHavePrinted;
        for (int i = 0; i < this.termList.size(); i++) {
            if (i == skipPos) {
                continue;
            }

            String termString = this.termList.get(i).toString();
            if (havePrinted && termString.charAt(0) != '-') {
                stringBuilder.append("+");
            }
            stringBuilder.append(termString);
            havePrinted = true;
        }

        if (!havePrinted || this.termList.isEmpty()) {
            stringBuilder.append("0");
        }
    }
}

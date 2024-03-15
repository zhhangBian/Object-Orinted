import java.math.BigInteger;
import java.util.ArrayList;

public class Expr implements Factor {
    private final ArrayList<Term> terms;
    private final ArrayList<Token> ops;
    private int exp = 1;

    public Expr() {
        this.terms = new ArrayList<>();
        this.ops = new ArrayList<>();
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void addOp(Token op) {
        this.ops.add(op);
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public Polynomial toPoly() {
        if (exp == 0) {
            return new Polynomial(new PolyKey(), BigInteger.ONE);
        }

        Polynomial poly = new Polynomial();

        if (ops.get(0).getType().equals(Token.Type.ADD)) {
            poly.addPoly(terms.get(0).toPoly());
        } else {
            poly.subPoly(terms.get(0).toPoly());
        }

        for (int i = 1; i < terms.size(); i++) {
            Term term = terms.get(i);
            Token op = ops.get(i);
            if (op.getType().equals(Token.Type.ADD)) {
                poly.addPoly(term.toPoly());
            } else {
                poly.subPoly(term.toPoly());
            }
        }
        poly.power(exp);
        return poly;
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}

package expression;

import simplify.Mono;
import simplify.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class Expr implements Factor {

    private final ArrayList<Term> terms;
    private  BigInteger index = BigInteger.ONE;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    @Override
    public void changeIndex(BigInteger index) {
        this.index = index;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    @Override
    public Poly transPoly() {
        Poly poly = new Poly();
        Mono mono = new Mono(BigInteger.ZERO, "x", BigInteger.ZERO);
        poly.addMono(mono);
        if (index.equals(BigInteger.ZERO)) {
            Mono mono1 = new Mono(BigInteger.ONE, "x", BigInteger.ZERO);
            poly.addMono(mono1);
        } else {
            for (Term term: terms) {
                poly = poly.add(term.transPoly());
            }
            poly = poly.pow(index);
        }
        return poly;
    }

}

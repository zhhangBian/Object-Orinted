package expression;

import simplify.Mono;
import simplify.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class Term {

    private final ArrayList<Factor> factors;
    private final int sign;

    public Term(int sign) {
        this.factors = new ArrayList<>();
        this.sign = sign;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public Poly transPoly() {
        Poly poly = new Poly();
        Mono mono = new Mono(BigInteger.ONE, "x", BigInteger.ZERO);
        poly.addMono(mono);
        for (Factor factor : factors) {
            poly = poly.mul(factor.transPoly());
        }
        if (sign == -1) {
            poly = poly.changeSign();
        }
        return poly;
    }
}

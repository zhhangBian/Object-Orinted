package expression;

import simplify.Mono;
import simplify.Poly;

import java.math.BigInteger;

public class ExpFactor implements Factor {
    private final Factor factor;
    private BigInteger index = BigInteger.ONE;

    public ExpFactor(Factor factor) {
        this.factor = factor;
    }

    @Override
    public void changeIndex(BigInteger index) {
        this.index = index;
    }

    @Override
    public Poly transPoly() {
        Poly poly = new Poly();
        Mono mono = new Mono(BigInteger.ONE, "x", BigInteger.ZERO);
        mono.addExpExpr(index, factor.transPoly());
        poly.addMono(mono);
        return poly;
    }
}

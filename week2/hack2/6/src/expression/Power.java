package expression;

import simplify.Mono;
import simplify.Poly;

import java.math.BigInteger;

public class Power implements Factor {

    private final String variable;

    private final BigInteger index;

    public Power(String variable, BigInteger index) {
        this.variable = variable;
        this.index = index;
    }

    @Override
    public Poly transPoly() {
        Poly poly = new Poly();
        Mono mono = new Mono(BigInteger.ONE, variable, index);
        poly.addMono(mono);
        return poly;
    }

    @Override
    public void changeIndex(BigInteger index) {
        return;
    }
}

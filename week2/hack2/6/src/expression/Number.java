package expression;

import simplify.Mono;
import simplify.Poly;

import java.math.BigInteger;

public class Number implements Factor {

    private final BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    @Override
    public Poly transPoly() {
        Poly poly = new Poly();
        Mono mono = new Mono(num,"x",BigInteger.ZERO);
        poly.addMono(mono);
        return poly;
    }

    @Override
    public void changeIndex(BigInteger index) {
        return;
    }
}

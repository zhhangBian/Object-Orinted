import java.math.BigInteger;

public class Exponential implements Factor {
    private final Factor factor;
    private final int exp;

    public Exponential(Factor factor, int exp) {
        this.factor = factor;
        this.exp = exp;
    }

    @Override
    public Polynomial toPoly() {
        Polynomial poly = new Number(String.valueOf(exp)).toPoly();
        poly.multPoly(factor.toPoly());

        PolyKey polyKey = new PolyKey(0, poly.toString());
        return new Polynomial(polyKey, BigInteger.ONE);
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}

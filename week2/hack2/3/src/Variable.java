import java.math.BigInteger;

public class Variable implements Factor {
    private final int exp;

    public Variable(int exp) {
        this.exp = exp;
    }

    public Polynomial toPoly() {
        return new Polynomial(new PolyKey(exp, "0"), BigInteger.ONE);
    }

    @Override
    public String toString() {
        return this.toPoly().toString();
    }
}

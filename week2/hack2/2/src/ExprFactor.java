import java.math.BigInteger;

public class ExprFactor implements Factor {
    private Expr base;

    private BigInteger index;

    public ExprFactor(Expr base, BigInteger index) {
        this.base = base;
        this.index = index;
    }

    @Override
    public Poly toPoly() {
        Poly poly = base.toPoly().powPoly(index);
        return poly;
    }

    public String toString() {
        return "(" + base + ")^" + index;
    }
}

import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public BigInteger getNum() {
        return num;
    }

    public String toString() {
        return this.num.toString();
    }

    @Override
    public Poly toPoly() {
        Poly result = new Poly();
        Mono mono = new Mono(num, BigInteger.ZERO, null);
        result.addMono(mono);
        return result;
    }
}

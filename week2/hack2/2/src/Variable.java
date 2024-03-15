import java.math.BigInteger;

public class Variable implements Factor {
    private final String var;

    private BigInteger index; //次方

    public Variable(String varName, BigInteger index)
    {
        this.var = varName;
        this.index = index;
    }

    public BigInteger getIndex() {
        return index;
    }

    public String toString() {
        return this.var + " ^ " + this.index;
    }

    public Poly toPoly() {
        Poly result = new Poly();
        Mono mono = new Mono(BigInteger.ONE, index, null);
        result.addMono(mono);
        return result;
    }
}



import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Term {
    private final ArrayList<Factor> factors;

    private int sign;

    public Term(int sign) {
        this.sign = sign;
        this.factors = new ArrayList<>();
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public void addFactor(Factor factor) {
        //System.out.println(factor);
        this.factors.add(factor);
    }

    public Poly toPoly() {
        Mono mono = new Mono(BigInteger.ONE, BigInteger.ZERO, null);
        Poly poly = new Poly();
        poly.addMono(mono);
        for (Factor it: factors) {
            Poly temp = poly.multPoly(it.toPoly());
            poly = temp;
        }
        if (sign == -1) {
            poly = poly.negate();
        }
        return poly;
    }

    public String toString() {
        Iterator<Factor> iter = factors.iterator();
        /* for (Factor obj : factors)
        {
            System.out.println(obj);
        }*/
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append(" * ");
            sb.append(iter.next().toString());
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }
}

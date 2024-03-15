import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private int sign;
    private ArrayList<Factor> factors;
    private ArrayList<Poly> polys;
    private Poly myPoly;

    public Term(int sign) {
        this.sign = sign;
        this.factors = new ArrayList<>();
        this.polys = new ArrayList<>();
        this.myPoly = new Poly();
    }

    public void addFactor(Factor factor) {
        factors.add(factor);
    }

    public void addPoly(Poly poly) {
        polys.add(poly);
    }

    public Poly toPoly() {
        if (polys.isEmpty()) {
            myPoly.addMono(new Mono(BigInteger.ZERO, BigInteger.ZERO, Poly.getZero()));
            return myPoly;
        } else {
            myPoly = polys.get(0);
        }
        for (int i = 1; i < polys.size(); i++) {
            myPoly = Poly.polyMul(myPoly, polys.get(i));
        }
        if (sign == -1) {
            myPoly.neg();
        }
        return myPoly;
    }
}

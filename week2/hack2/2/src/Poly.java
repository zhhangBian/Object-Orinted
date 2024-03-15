import java.math.BigInteger;
import java.util.ArrayList;

public class Poly {
    //private ArrayList<HashMap<Expr, BigInteger>> polyList;
    //private HashMap<Model, BigInteger> polyList;
    private ArrayList<Mono> monos;

    public Poly() {
        monos = new ArrayList<>();
    }

    public ArrayList<Mono> getMonos() {
        return monos;
    }

    public Poly createSame() {
        Poly newPoly = new Poly();
        for (Mono mono : monos) {
            newPoly.addMono(mono.createSame());
        }
        return newPoly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Poly poly = (Poly) o;

        return monos.equals(poly.monos);
    }

    @Override
    public int hashCode() {
        return monos.hashCode();
    }

    public void addMono(Mono mono) {
        this.monos.add(mono);
    }

    public Poly addPoly(Poly poly) {
        Poly result = this.createSame();
        for (Mono that : poly.getMonos()) {
            int flag = 0; //未加入
            for (Mono it : result.monos) {
                if (it.canMerge(that)) {
                    it.setCoef(it.getCoef().add(that.getCoef()));
                    flag = 1; //已加入
                    break;
                }
            }
            if (flag == 0) { //没找到可以合并的,放在最后
                result.monos.add(that);
            }
        }
        return result;
    }

    public Poly multPoly(Poly poly) {
        Poly result = new Poly();
        for (Mono that : poly.getMonos()) {
            for (Mono it : monos) {
                BigInteger newCoef = that.getCoef().multiply(it.getCoef());
                BigInteger newIndex = that.getIndex().add(it.getIndex());
                Poly newExponent = new Poly();
                if (that.getExponent() != null && it.getExponent() != null) {
                    newExponent = that.getExponent().addPoly(it.getExponent());
                }
                else if (that.getExponent() == null && it.getExponent() == null) {
                    newExponent = null;
                }
                else if (that.getExponent() == null) {
                    newExponent = it.getExponent().createSame(); //要不要深拷贝？
                }
                else if (it.getExponent() == null) {
                    newExponent = that.getExponent().createSame();
                }

                Mono mono = new Mono(newCoef, newIndex, newExponent);
                Poly newPoly = new Poly();
                newPoly.addMono(mono);

                result = result.addPoly(newPoly);
            }
        }
        return result;
    }

    public Poly powPoly(BigInteger index) {
        Mono mono = new Mono(BigInteger.ONE, BigInteger.ZERO, null); //1
        Poly result = new Poly();
        result.addMono(mono); //result = 1 * x^0 * exp(0)

        if (index.equals(BigInteger.ZERO)) { //0次方
            return result; //result = 1
        }
        for (int i = 0; index.compareTo(BigInteger.valueOf(i)) > 0; i++) {
            result = result.multPoly(this);
        }
        return result;
    }

    public Poly negate() {
        return this.multNum(new BigInteger("-1"));
    }

    public Poly multNum(BigInteger n) {
        Poly poly = new Poly();
        Mono mono = new Mono(n, BigInteger.ZERO, null);
        poly.addMono(mono);
        return this.multPoly(poly);
    }

    public String toString() {
        if (this.monos.size() == 0) {
            return "0";
        }
        boolean isZero = true;
        for (int i = 0; i < this.monos.size(); i++) {
            if (!monos.get(i).getCoef().equals(BigInteger.ZERO)) {
                isZero = false;
            }
        }
        if (isZero) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.monos.size(); i++) {
            Mono it = monos.get(i);
            //String coefString = "";
            String indexString = "";
            String expString = "";

            if (it.getCoef().equals(BigInteger.ZERO)) {
                sb.append("");
                continue;
            }
            else if (it.getIndex().equals(BigInteger.ZERO)
                        && it.getExponent() == null) {
                sb.append("+" + it.getCoef()); //coef*x^0*exp(0)
                continue;
            }

            if (monos.get(i).getExponent() == null) { //exp(0)，不用乘进去
                expString = "";
            }
            else {
                expString = "*exp((" + monos.get(i).getExponent().toString() + "))";
            }


            if (monos.get(i).getIndex().equals(BigInteger.ONE)) {
                indexString = "*x";
            }
            else if (monos.get(i).getIndex().equals(BigInteger.ZERO)) { //coef*x^0*exp(!0)
                indexString = "";
            }
            else {
                indexString = "*x^" + it.getIndex();
            }

            sb.append("+" + it.getCoef() + indexString + expString);
        }
        return sb.toString();
    }
}

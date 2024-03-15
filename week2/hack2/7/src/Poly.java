import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Poly {
    private ArrayList<Mono> monos;

    public Poly() {
        this.monos = new ArrayList<>();
    }

    public static Poly getZero() {
        Poly poly = new Poly();
        poly.addMono(new Mono(BigInteger.ZERO, BigInteger.ZERO, null));
        return poly;
    }

    public Boolean containMono(Mono monoHas) {
        if (this.monos.isEmpty()) {
            return false;
        } else {
            for (Mono mono : monos) {
                if (monoHas.getCoe().equals(BigInteger.ZERO) || Mono.monoEqual(mono, monoHas)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Boolean polyEqual(Poly polyI, Poly polyJ) {
        if (polyI == null && polyJ == null) {
            return true;
        } else if (polyI == null && Poly.polyEqual(polyJ, Poly.getZero())) {
            return true;
        } else if (polyJ == null && Poly.polyEqual(polyI, Poly.getZero())) {
            return true;
        } else if (polyI == null || polyJ == null) {
            return false;
        }
        polyI.polyCal();
        polyJ.polyCal();
        int flag = 1;
        for (int i1 = 0; i1 < polyI.getNumMono(); i1++) {
            if (!polyJ.containMono(polyI.getMono(i1))) {
                flag = 0;
                break;
            }
        }
        if (flag == 0) {
            return false;
        } else {
            for (int j1 = 0; j1 < polyJ.getNumMono(); j1++) {
                if (!polyI.containMono(polyJ.getMono(j1))) {
                    flag = 0;
                    break;
                }
            }
            return flag != 0;
        }
    }

    public void addMono(Mono mono) {
        monos.add(mono);
    }

    public void neg() {
        for (Mono mono : monos) {
            mono.neg();
        }
    }

    public void mergePoly(Poly poly) {
        for (Mono mono : poly.monos) {
            monos.add(new Mono(mono.getCoe(), mono.getExp(), mono.getExpPoly()));
        }
    }

    public static Poly polyAdd(Poly poly1, Poly poly2) {
        Poly poly = new Poly();
        poly.mergePoly(poly1);
        poly.mergePoly(poly2);
        return poly;
    }

    public static Poly polyTime(int time, Poly poly) {
        Poly polyReturn = new Poly();
        if (poly == null) {
            return null;
        }
        ArrayList<Mono> monoList = poly.getMonos();
        for (Mono mono : monoList) {
            polyReturn.addMono(Mono.monoTime(time, mono));
        }
        return polyReturn;
    }

    public static Poly polyMul(Poly polyI, Poly polyJ) {
        Poly polyReturn = new Poly();
        if (polyJ == null) {
            return polyI;
        }
        polyI.polyCal();
        polyJ.polyCal();
        for (int i1 = 0; i1 < polyI.getNumMono(); i1++) {
            for (int j1 = 0; j1 < polyJ.getNumMono(); j1++) {
                polyReturn.addMono(Mono.monoMul(polyI.getMono(i1), polyJ.getMono(j1)));
            }
        }
        return polyReturn;
    }

    public static Poly polyPower(int exp, Poly poly) {
        Poly polyReturn = new Poly();
        Mono mono = new Mono(BigInteger.ONE, BigInteger.ZERO, Poly.getZero());
        polyReturn.addMono(mono);
        if (exp == 0) {
            return polyReturn;
        } else {
            for (int i = 0; i < exp; i++) {
                polyReturn = Poly.polyMul(polyReturn, poly);
            }
            return polyReturn;
        }
    }

    public void removeMono(int i) {
        monos.remove(i);
    }

    public static Boolean isBaseFactor(Poly poly) {
        if (poly.getNumMono() > 1) {
            int i = 0;
            while (i < poly.getNumMono()) {
                if (poly.getMono(i).getCoe().equals(BigInteger.ZERO)) {
                    poly.removeMono(i);
                } else {
                    i++;
                }
            }
        }
        poly.polyCal();
        if (poly.getNumMono() > 1) {
            return false;
        } else if (poly.getNumMono() == 0) {
            return true;
        } else {
            Mono mono = poly.getMono(0);
            if (mono.getCoe().abs().equals(BigInteger.ONE) &&
                    Poly.polyEqual(mono.getExpPoly(), Poly.getZero())) {
                return true;
            } else if (mono.getCoe().abs().equals(BigInteger.ONE) &&
                    mono.getExp().equals(BigInteger.ZERO)) {
                return true;
            } else if (Poly.polyEqual(mono.getExpPoly(), Poly.getZero()) &&
                    mono.getExp().equals(BigInteger.ZERO)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void polyCal() {
        int i = 0;
        while (i < monos.size() - 1) {
            int j = i + 1;
            while (j < monos.size()) {
                if (Mono.monoCanMerge(monos.get(i), monos.get(j))) {
                    monos.get(i).mergeMono(monos.get(j));
                    monos.remove(j);
                } else {
                    j++;
                }
            }
            i++;
        }
        Collections.sort(monos);
    }

    public ArrayList<Mono> getMonos() {
        return monos;
    }

    public int getNumMono() {
        return monos.size();
    }

    public Mono getMono(int i) {
        return monos.get(i);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.monos.isEmpty()) {
            return "0";
        } else {
            for (int i = 0; i < monos.size(); i++) {
                Mono mono = monos.get(i);
                if (mono.isPos()) {
                    Collections.swap(monos, i, 0);
                    break;
                }
            }
            if (monos.get(0).isPos()) {
                sb.append(monos.get(0).toString().substring(1));
            } else {
                sb.append(monos.get(0).toString());
            }
            for (int i = 1; i < monos.size(); i++) {
                sb.append(monos.get(i).toString());
            }
            if (sb.toString().isEmpty()) {
                return "0";
            } else {
                return sb.toString();
            }
        }
    }
}

package calc;

import java.util.ArrayList;
import java.util.Iterator;

public class Poly {
    private ArrayList<Mono> poly;

    public Poly() {
        poly = new ArrayList<>();
    }
    //protected Mono

    public void addMono(Mono input) {
        for (Mono temp : poly) {
            if (temp.likeTerm(input)) {
                temp.merge(input);
                return;
            }
        }
        poly.add(input.clone());
    }

    public void inverse() {
        for (Mono temp : poly) {
            temp.inverse();
        }
    }

    public boolean equals(Poly compare) {
        boolean find;
        for (Mono cont1 : this.poly) {
            find = false;
            for (Mono cont2 : compare.poly) {
                if (cont1.equals(cont2)) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                return false;
            }
        }
        return true;
    }

    public Poly add(Poly poly2) {
        Poly result = new Poly();
        for (Mono temp1:this.poly) {
            result.addMono(temp1);
        }
        if (poly2 != null) {
            for (Mono temp2 : poly2.poly) {
                result.addMono(temp2);
            }
        }
        return result;
    }

    public Poly mult(Poly poly2) {
        Poly result = new Poly();
        for (Mono temp1 : this.poly) {
            for (Mono temp2 : poly2.poly) {
                result.addMono(temp1.mult(temp2));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        //Iterator<Mono> iter = poly.iterator();
        //StringBuilder sb = new StringBuilder();
        //sb.append(iter.next().toString());
        //while (iter.hasNext()) {
        //    sb.append("+");
        //    sb.append(iter.next().toString());
        //}
        //return sb.toString();
        Iterator<Mono> iter = poly.iterator();
        StringBuilder sb = new StringBuilder();
        String temp = iter.next().toString();
        if (!temp.equals("0")) {
            sb.append(temp);
        }
        while (iter.hasNext()) {
            temp = iter.next().toString();
            if (!temp.equals("0")) {
                if (temp.charAt(0) == '-') {
                    sb.append(temp);
                } else {
                    sb.append("+");
                    sb.append(temp);
                }
            }
        }
        if (sb.length() == 0) {
            return "0";
        } else {
            return sb.toString();
        }
    }
}

package calc;

import java.math.BigInteger;

public class Mono {
    private int index;
    private BigInteger coffe;

    protected int getIndex() {
        return index;
    }

    protected BigInteger getCoffe() {
        return coffe;
    }

    public Mono(int index, BigInteger coffe) {
        this.index = index;
        this.coffe = coffe;
    }

    public boolean likeTerm(Mono check) {
        if (!(this instanceof Epow) && check instanceof Epow) {
            return false;
        }
        return this.index == check.index;
    }

    public void merge(Mono like) {
        this.coffe = this.coffe.add(like.coffe);
    }

    public void inverse() {
        this.coffe = this.coffe.negate();
    }

    public boolean equals(Mono compare) {
        if (compare instanceof Epow) {
            return false;
        } else {
            return this.index == compare.index && this.coffe.equals(compare.coffe);
        }
    }

    public Mono mult(Mono mono2) {
        if (mono2 instanceof Epow) {
            return mono2.mult(this);
        } else {
            return new Mono(this.index + mono2.index, this.coffe.multiply(mono2.coffe));
        }

    }

    @Override
    public Mono clone() {
        return new Mono(this.index, new BigInteger(this.coffe.toString()));
    }
    //public String toString() {
    //    return coffe.toString() +
    //            "*x^" +
    //            index;
    //    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        if (coffe.equals(new BigInteger("1"))) { //系数是1
            if (index == 0) {
                sb.append(1);   // 0次方
            } else if (index == 1) {
                sb.append("x"); // 1次方
            } else {
                sb.append("x^");
                sb.append(index);
            }
        } else if (coffe.equals(new BigInteger("-1"))) { //系数-1
            if (index == 0) {
                sb.append(-1);   // 0次方
            } else if (index == 1) {
                sb.append("-x"); // 1次方
            } else {
                sb.append("-x^");
                sb.append(index);
            }
        } else if (coffe.equals(new BigInteger("0"))) { //系数是0
            sb.append("0");
        } else { //带有系数
            if (index == 0) { // 0次方
                sb.append(coffe);
            } else if (index == 1) { // 1次方
                sb.append(coffe);
                sb.append("*x");
            } else {
                sb.append(coffe);
                sb.append("*x^");
                sb.append(index);
            }
        }
        return sb.toString();
    }
}

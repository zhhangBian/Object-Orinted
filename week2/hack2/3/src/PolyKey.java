public class PolyKey {
    private final int varExp;
    private final String expStr;

    public PolyKey() {
        this.varExp = 0;
        this.expStr = "0";
    }

    public PolyKey(int varExp, String expStr) {
        this.varExp = varExp;
        this.expStr = expStr;
    }

    @Override
    public int hashCode() {
        return 31 * expStr.hashCode() + Integer.hashCode(varExp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PolyKey)) {
            return false;
        }
        PolyKey polyKey = (PolyKey) obj;
        return polyKey.varExp == this.varExp && polyKey.expStr.equals(this.expStr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (varExp == 0 && expStr.equals("0")) {
            return "1";
        }
        if (varExp > 0) {
            sb.append("x");
            if (varExp > 1) {
                sb.append("^").append(varExp);
            }
        }
        if (varExp > 0 && !expStr.equals("0")) {
            sb.append("*");
        }
        if (!expStr.equals("0")) {
            sb.append("exp(");
            if (isExpNumOrVar()) {
                sb.append(expStr);
            } else {
                sb.append("(").append(expStr).append(")");
            }
            sb.append(")");
        }
        return sb.toString();
    }

    private boolean isExpNumOrVar() {
        int indexPlus = expStr.indexOf('+');
        int indexMinus = expStr.substring(1).indexOf('-');
        int indexMult = expStr.indexOf('*');
        return indexPlus == -1 && indexMinus == -1 && indexMult == -1;
    }

    public PolyKey multiplyKey(PolyKey right) {
        int varExp = this.varExp + right.varExp;
        if (this.expStr.equals("0") && right.expStr.equals("0")) { // EXIT
            return new PolyKey(varExp, "0");
        }
        String newExpr = "(" + this.expStr + ")+(" + right.expStr + ")";
        Parser parser = new Parser(new Lexer(newExpr));
        String expStr = parser.parseExpr().toPoly().toString();
        return new PolyKey(varExp, expStr);
    }
}

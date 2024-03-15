import java.util.ArrayList;
import java.util.Iterator;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    private final ArrayList<String> ops;//每个表达式中的+/-

    private int flag;//表示到哪个ops了

    public Expr() {
        this.terms = new ArrayList<>();
        this.ops = new ArrayList<>();
        this.flag = 0;
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public ArrayList<String> getOps() {
        return ops;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public Poly toPoly() {
        Poly poly = new Poly();
        for (Term it : terms) {
            Poly temp = poly.addPoly(it.toPoly());
            poly = temp;
        }
        return poly;
    }

    public String toString() {
        //flag = 0;
        Iterator<Term> iter = terms.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        //++flag;
        while (iter.hasNext()) {
            sb.append("+");
            //String op = ops.get(flag++);
            //sb.append(op + " ");
            sb.append(iter.next().toString());
        }
        return sb.toString();
    }
}

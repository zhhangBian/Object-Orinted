import java.util.ArrayList;

public class Expr implements Factor {
    private ArrayList<Term> terms;
    private ArrayList<Poly> polys;
    private Poly mypoly;

    public Expr() {
        this.terms = new ArrayList<>();
        this.polys = new ArrayList<>();
        this.mypoly = new Poly();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void addPoly(Poly poly) {
        polys.add(poly);
    }

    public Poly toPoly() {
        for (Poly poly : polys) {
            mypoly.mergePoly(poly);
        }

        return mypoly;
    }

}

package expression;

import simplify.Poly;

import java.math.BigInteger;

public interface Factor {

    public  Poly transPoly();

    public void changeIndex(BigInteger index);

}
